package prog.rohan.currency_conversion.dao;

import prog.rohan.currency_conversion.exceptions.DatabaseException;
import prog.rohan.currency_conversion.model.CurrenciesModel;
import prog.rohan.currency_conversion.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public static CurrenciesModel insertCurrency(CurrenciesModel currency){
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
        }catch (SQLException e){
            throw new DatabaseException("Error with adding " + currency.getCode() + " to database!" );
        }
        return currency;
    }

    public static List<CurrenciesModel> selectCurrencies(){
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
        }catch (SQLException e){
            throw new DatabaseException("Error with selecting currencies from database!");
        }
        return currenciesList;
    }

    public static Optional<CurrenciesModel> selectCurrencyById(int id) {
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
        }catch (SQLException e){
            throw new DatabaseException("Error with selecting " + id + " from database!" );
        }
        return Optional.ofNullable(currency);
    }

    public static void updateCurrency(CurrenciesModel currency) {
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, currency.getCode());
            preparedStatement.setString(2, currency.getFullName());
            preparedStatement.setString(3, currency.getSign());
            preparedStatement.setInt(4, currency.getId());
            preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            throw new DatabaseException("Error with updating" + currency.getCode() + " in database!" );
        }
    }

}
