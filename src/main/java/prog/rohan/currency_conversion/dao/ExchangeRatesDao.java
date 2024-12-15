package prog.rohan.currency_conversion.dao;

import prog.rohan.currency_conversion.exceptions.DatabaseException;
import prog.rohan.currency_conversion.model.ExchangeRatesModel;
import prog.rohan.currency_conversion.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRatesDao {
    private static final String INSERT_SQL = """
            INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate)
               SELECT bc.id, tc.id, ?
               FROM Currencies AS bc, Currencies AS tc
               WHERE bc.Code = ? AND tc.Code = ?;
            """;
    private static final String SELECT_SQL = """
           SELECT *
           FROM ExchangeRates as er
           LEFT JOIN Currencies as bc on er.BaseCurrencyId = bc.id
           LEFT JOIN Currencies as tc on er.TargetCurrencyId = tc.id
            """;

    private static final String SELECT_BY_CODE_SQL = """
           WHERE bc.Code = ? and tc.Code = ?;
           """;

    private static final String UPDATE_SQL = """
            UPDATE ExchangeRates
            SET Rate = ?
            WHERE BaseCurrencyId = (SELECT id FROM Currencies WHERE Code = ?)
            AND TargetCurrencyId = (SELECT id FROM Currencies WHERE Code = ?)
            """;

    public static ExchangeRatesModel insertExchangeRate(ExchangeRatesModel exchangeRate) {
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL)) {
            preparedStatement.setDouble(3, exchangeRate.getRate());
            preparedStatement.setString(1, exchangeRate.getBaseCurrencyCode());
            preparedStatement.setString(2, exchangeRate.getTargetCurrencyCode());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()){
                exchangeRate.setId(generatedKeys.getInt("id"));
            }
        }catch (SQLException e){
            throw new DatabaseException("Error with adding " + exchangeRate.getBaseCurrencyCode()
                                        +exchangeRate.getTargetCurrencyCode()+ " to database!" );
        }
        return exchangeRate;
    }

    public static List<ExchangeRatesModel> selectExchangeRates(){
        List<ExchangeRatesModel> exchangeRatesList = new ArrayList<>();
        ExchangeRatesModel exchangeRate = null;
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                exchangeRate = new ExchangeRatesModel(
                        resultSet.getInt(1),
                        resultSet.getString(6),
                        resultSet.getString(10),
                        resultSet.getDouble(4)
                );
                exchangeRatesList.add(exchangeRate);
            }
            return exchangeRatesList;
        }catch (SQLException e) {
            throw new DatabaseException("Error with selecting exchange rates from database!");
        }
    }

    public static Optional<ExchangeRatesModel> selectByCode(String baseCurrencyCode, String targetCurrencyCode) {
        ExchangeRatesModel exchangeRate = null;
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_CODE_SQL)) {
            preparedStatement.setString(1, baseCurrencyCode);
            preparedStatement.setString(2, targetCurrencyCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                exchangeRate = new ExchangeRatesModel(
                        resultSet.getInt("id"),
                        baseCurrencyCode,
                        targetCurrencyCode,
                        resultSet.getDouble("Rate")
                );
            }
        }catch (SQLException e){
            throw new DatabaseException("Error with selecting " + baseCurrencyCode + targetCurrencyCode + " from database!" );
        }
        return Optional.ofNullable(exchangeRate);
    }

    public static void updateExchangeRate(ExchangeRatesModel exchangeRate){
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, exchangeRate.getBaseCurrencyCode());
            preparedStatement.setString(2, exchangeRate.getTargetCurrencyCode());
            preparedStatement.setDouble(3, exchangeRate.getRate());
            preparedStatement.setInt(4, exchangeRate.getId());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            throw new DatabaseException("Error with updating " + exchangeRate.getBaseCurrencyCode()
                                        + exchangeRate.getTargetCurrencyCode() + " in database!" );
        }
    }


}
