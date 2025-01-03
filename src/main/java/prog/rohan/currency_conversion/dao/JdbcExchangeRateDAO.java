package prog.rohan.currency_conversion.dao;

import prog.rohan.currency_conversion.exceptions.DatabaseException;
import prog.rohan.currency_conversion.model.Currency;
import prog.rohan.currency_conversion.model.ExchangeRate;
import prog.rohan.currency_conversion.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcExchangeRateDAO implements ExchangeRateDAO{
    private static final JdbcExchangeRateDAO INSTANCE = new JdbcExchangeRateDAO();

    private static final String INSERT_SQL = """
             INSERT INTO ExchangeRates(BaseCurrencyId, TargetCurrencyId, Rate)
            VALUES ((SELECT id FROM Currencies WHERE Code = ?),
            (SELECT id FROM Currencies WHERE Code = ?),
            ?)
            """;
    private static final String SELECT_SQL = """
      SELECT
        er.id AS id,
        bc.id AS base_id,
        bc.Code AS base_code,
        bc.FullName AS base_name,
        bc.Sign AS base_sign,
        tc.id AS target_id,
        tc.Code AS target_code,
        tc.FullName AS target_name,
        tc.Sign AS target_sign,
        er.Rate AS rate
      FROM ExchangeRates er
      JOIN Currencies bc ON er.BaseCurrencyId = bc.id
      JOIN Currencies tc ON er.TargetCurrencyId = tc.id
      """;

    private static final String SELECT_BY_CODE_SQL = SELECT_SQL + "\n" + """
           WHERE bc.Code = ? and tc.Code = ?;
           """;

    private static final String SELECT_BY_ID_SQL = """
            SELECT *
            FROM ExchangeRates
            WHERE id = ?;
            """;

    private static final String UPDATE_SQL = """
            UPDATE ExchangeRates
            SET Rate = ?
            WHERE BaseCurrencyId = (SELECT id FROM Currencies WHERE Code = ?)
            AND TargetCurrencyId = (SELECT id FROM Currencies WHERE Code = ?)
            """;

    private JdbcExchangeRateDAO(){}

    public static JdbcExchangeRateDAO getINSTANCE(){
        return INSTANCE;
    }

    @Override
    public ExchangeRate save(ExchangeRate exchangeRate) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL)) {
            preparedStatement.setDouble(3, exchangeRate.getRate());
            preparedStatement.setString(1, exchangeRate.getBaseCurrency().getCode());
            preparedStatement.setString(2, exchangeRate.getTargetCurrency().getCode());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()){
                exchangeRate.setId(generatedKeys.getInt(1));
            }
        }catch (SQLException e){
            throw new DatabaseException("Error with adding " + exchangeRate.getBaseCurrency().getCode()
                                        +exchangeRate.getTargetCurrency().getCode() + " to database!" );
        }
        return exchangeRate;
    }

    @Override
    public List<ExchangeRate> findAll(){
        List<ExchangeRate> exchangeRatesList = new ArrayList<>();
        ExchangeRate exchangeRate = null;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                exchangeRate = getExchangeRate(resultSet);
                exchangeRatesList.add(exchangeRate);
            }
            return exchangeRatesList;
        }catch (SQLException e) {
            throw new DatabaseException("Error with selecting exchange rates from database!");
        }
    }

    @Override
    public Optional<ExchangeRate> findByCodes(String baseCurrencyCode, String targetCurrencyCode) {
        ExchangeRate exchangeRate = null;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_CODE_SQL)) {
            preparedStatement.setString(1, baseCurrencyCode);
            preparedStatement.setString(2, targetCurrencyCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                exchangeRate = getExchangeRate(resultSet);
            }
        }catch (SQLException e){
            throw new DatabaseException("Error with selecting " + baseCurrencyCode
                                        + targetCurrencyCode + " from database!" );
        }
        return Optional.ofNullable(exchangeRate);
    }

    @Override
    public Optional<ExchangeRate> findByID(Integer id){
        ExchangeRate exchangeRate = null;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                exchangeRate = getExchangeRate(resultSet);
            }
        }catch (SQLException e){
            throw new DatabaseException("Error with selecting exchange rate with id " + id
                                        + " from database!" );
        }
        return Optional.ofNullable(exchangeRate);
    }

    public Optional<ExchangeRate> update(ExchangeRate exchangeRate){
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, exchangeRate.getBaseCurrency().getCode());
            preparedStatement.setString(2, exchangeRate.getTargetCurrency().getCode());
            preparedStatement.setDouble(3, exchangeRate.getRate());
            preparedStatement.executeUpdate();
            return findByCodes(exchangeRate.getBaseCurrency().getCode(),
                    exchangeRate.getTargetCurrency().getCode());
        }catch (SQLException e){
            throw new DatabaseException("Error with updating "
                                        + exchangeRate.getBaseCurrency().getCode()
                                        + exchangeRate.getTargetCurrency().getCode()
                                        + " in database!" );
        }
    }

    private static ExchangeRate getExchangeRate(ResultSet resultSet) throws SQLException {
        return new ExchangeRate(
                resultSet.getInt("id"),
                new Currency(
                        resultSet.getInt("base_id"),
                        resultSet.getString("base_code"),
                        resultSet.getString("base_name"),
                        resultSet.getString("base_sign")
                ),
                new Currency(
                        resultSet.getInt("target_id"),
                        resultSet.getString("target_code"),
                        resultSet.getString("target_name"),
                        resultSet.getString("target_sign")
                ),
                resultSet.getDouble("rate")
        );
    }

}
