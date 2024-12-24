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
             INSERT INTO ExchangeRates(BaseCurrencyId, TargetCurrencyId, Rate)
            VALUES ((SELECT id FROM Currencies WHERE Code = ?),
            (SELECT id FROM Currencies WHERE Code = ?),
            ?)
            """;
    private static final String SELECT_SQL = """
           SELECT *
           FROM ExchangeRates as er
           LEFT JOIN Currencies as bc on er.BaseCurrencyId = bc.id
           LEFT JOIN Currencies as tc on er.TargetCurrencyId = tc.id
           """;

    private static final String SELECT_BY_CODE_SQL = SELECT_SQL + "\n" + """
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
                exchangeRate.setId(generatedKeys.getInt(1));
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

    public static Optional<ExchangeRatesModel> selectByCode(ExchangeRatesModel exchangeRatesModel) {
        ExchangeRatesModel exchangeRate = null;
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_CODE_SQL)) {
            preparedStatement.setString(1, exchangeRatesModel.getBaseCurrencyCode());
            preparedStatement.setString(2, exchangeRatesModel.getTargetCurrencyCode());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                exchangeRate = new ExchangeRatesModel(
                        resultSet.getInt("id"),
                        exchangeRatesModel.getBaseCurrencyCode(),
                        exchangeRatesModel.getTargetCurrencyCode(),
                        resultSet.getDouble("Rate")
                );
            }
        }catch (SQLException e){
            throw new DatabaseException("Error with selecting " + exchangeRate.getBaseCurrencyCode()
                                        + exchangeRate.getTargetCurrencyCode() + " from database!" );
        }
        return Optional.ofNullable(exchangeRate);
    }

    public static Optional<ExchangeRatesModel> updateExchangeRate(ExchangeRatesModel exchangeRate){
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, exchangeRate.getBaseCurrencyCode());
            preparedStatement.setString(2, exchangeRate.getTargetCurrencyCode());
            preparedStatement.setDouble(3, exchangeRate.getRate());
            preparedStatement.executeUpdate();
            return selectByCode(exchangeRate);
        }catch (SQLException e){
            throw new DatabaseException("Error with updating " + exchangeRate.getBaseCurrencyCode()
                                        + exchangeRate.getTargetCurrencyCode() + " in database!" );
        }
    }


}
