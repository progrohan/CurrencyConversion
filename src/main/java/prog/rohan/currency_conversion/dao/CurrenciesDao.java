package prog.rohan.currency_conversion.dao;

import prog.rohan.currency_conversion.model.CurrenciesModel;
import prog.rohan.currency_conversion.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrenciesDao {
    private static final String INSERT_SQL = """
            INSERT INTO Currencies (Code, FullName, Sign)
            VALUES ( ?, ?, ?);
            """;
    private static final String SELECT_SQL = """
            SELECT *
            FROM Currencies
            """;
    private static final String SELECT_BY_ID_SQL = SELECT_SQL
                                                   + "\n" + "WHERE id = ?";

    private static final String UPDATE_SQL = """
            UPDATE Currencies
            SET Code = ?,
                FullName = ?,
                Sign = ?
            WHERE id = ?
            """;

    public CurrenciesDao() {};

    public static CurrenciesModel insertCurrency(CurrenciesModel currency) throws SQLException {
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL)) {
            preparedStatement.setString(1, currency.getCode());
            preparedStatement.setString(2, currency.getFullName());
            preparedStatement.setString(3, currency.getSign());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()){
                currency.setId(generatedKeys.getInt("id"));
            }
            return currency;
        }
    }

    public static List<CurrenciesModel> selectCurrencies() throws SQLException {
        List<CurrenciesModel> currenciesList = new ArrayList<>();
        CurrenciesModel currency = null;
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                currency = new CurrenciesModel(
                        resultSet.getInt( "id"),
                        resultSet.getString("Code"),
                        resultSet.getString("FullName"),
                        resultSet.getString("Sign")
                );
                currenciesList.add(currency);
            }
        }
        return currenciesList;
    }

    public static CurrenciesModel selectCurrencyById(int id) throws SQLException {
        CurrenciesModel currency = null;
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                currency = new CurrenciesModel(
                        resultSet.getInt( "id"),
                        resultSet.getString("Code"),
                        resultSet.getString("FullName"),
                        resultSet.getString("Sign")
                );
            }
        }
        return currency;
    }

    public static void updateCurrency(CurrenciesModel currency) throws SQLException {
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, currency.getCode());
            preparedStatement.setString(2, currency.getFullName());
            preparedStatement.setString(3, currency.getSign());
            preparedStatement.setInt(4, currency.getId());
            preparedStatement.executeUpdate();
        }
    }

}
