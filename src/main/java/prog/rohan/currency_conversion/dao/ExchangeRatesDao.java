package prog.rohan.currency_conversion.dao;

import prog.rohan.currency_conversion.model.ExchangeRatesModel;
import prog.rohan.currency_conversion.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRatesDao {
    private static final String INSERT_SQL = """
            INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate)
            VALUES (?, ?, ?);
            """;
    private static final String SELECT_SQL = """
            SELECT *
            FROM ExchangeRates;
            """;

    private static final String SELECT_BY_ID_SQL = SELECT_SQL +
                                                   "\n" + "WHERE id = ?";

    private static final String UPDATE_SQL = """
            UPDATE ExchangeRates
            SET BaseCurrencyId = ?,
                TargetCurrencyId = ?,
                Rate = ?
            WHERE id = ?
            """;

    public static ExchangeRatesModel insertExchangeRate(ExchangeRatesModel exchangeRate) throws SQLException {
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL)) {
            preparedStatement.setInt(1, exchangeRate.getBaseCurrencyId());
            preparedStatement.setInt(2, exchangeRate.getTargetCurrencyId());
            preparedStatement.setDouble(3, exchangeRate.getRate());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()){
                exchangeRate.setId(generatedKeys.getInt("id"));
            }
        }
        return exchangeRate;
    }

    public static List<ExchangeRatesModel> selectExchangeRates() throws SQLException {
        List<ExchangeRatesModel> exchangeRatesList = new ArrayList<>();
        ExchangeRatesModel exchangeRate = null;
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                exchangeRate = new ExchangeRatesModel(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3),
                        resultSet.getDouble(4)
                );
                exchangeRatesList.add(exchangeRate);
            }
        }
        return exchangeRatesList;
    }

    public static ExchangeRatesModel selectById(int id) throws SQLException {
        ExchangeRatesModel exchangeRate = null;
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                exchangeRate = new ExchangeRatesModel(
                        resultSet.getInt("id"),
                        resultSet.getInt("BaseCurrencyId"),
                        resultSet.getInt("TargetCurrencyId"),
                        resultSet.getDouble("Rate")
                );
            }
        }
        return exchangeRate;
    }

    public static void updateExchangeRate(ExchangeRatesModel exchangeRate) throws SQLException {
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setInt(1, exchangeRate.getBaseCurrencyId());
            preparedStatement.setInt(2, exchangeRate.getTargetCurrencyId());
            preparedStatement.setDouble(3, exchangeRate.getRate());
            preparedStatement.setInt(4, exchangeRate.getId());
            preparedStatement.executeUpdate();
        }
    }

}
