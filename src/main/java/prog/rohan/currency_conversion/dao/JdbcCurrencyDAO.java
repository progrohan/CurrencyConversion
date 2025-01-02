package prog.rohan.currency_conversion.dao;

import prog.rohan.currency_conversion.exceptions.DatabaseException;
import prog.rohan.currency_conversion.model.Currency;
import prog.rohan.currency_conversion.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcCurrencyDAO implements CurrencyDAO{
    private static JdbcCurrencyDAO INSTANCE = new JdbcCurrencyDAO();
    private static final String INSERT_SQL = """
            INSERT INTO Currencies (Code, FullName, Sign)
            VALUES ( ?, ?, ?);
            """;
    private static final String SELECT_SQL = """
            SELECT *
            FROM Currencies
            """;
    private static final String SELECT_BY_CODE_SQL = SELECT_SQL
                                                   + "\n" + "WHERE Code = ?";

    private static final String SELECT_BY_ID_SQL = SELECT_SQL
                                                     + "\n" + "WHERE id = ?";

    private static final String UPDATE_SQL = """
            UPDATE Currencies
            SET Code = ?,
                FullName = ?,
                Sign = ?
            WHERE id = ?
            """;

    private JdbcCurrencyDAO() {};

    public static JdbcCurrencyDAO getINSTANCE(){
        return INSTANCE;
    }

    @Override
    public Currency save(Currency currency){
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL)) {
            preparedStatement.setString(1, currency.getCode());
            preparedStatement.setString(2, currency.getFullName());
            preparedStatement.setString(3, currency.getSign());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()){
                currency.setId(generatedKeys.getInt(1));
            }
            return currency;
        }catch (SQLException e){
            throw new DatabaseException("Error with adding " + currency.getCode()  + " to database!");
        }

    }

    @Override
    public List<Currency> findAll(){
        List<Currency> currenciesList = new ArrayList<>();
        Currency currency = null;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                currency = new Currency(
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

    @Override
    public Optional<Currency> findByCode(String code) {
        Currency currency = null;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_CODE_SQL)) {
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                currency = new Currency(
                        resultSet.getInt( "id"),
                        resultSet.getString("Code"),
                        resultSet.getString("FullName"),
                        resultSet.getString("Sign")
                );
            }
        }catch (SQLException e){
            throw new DatabaseException("Error with selecting " + code + " from database!" );
        }
        return Optional.ofNullable(currency);
    }

    @Override
    public Optional<Currency> findByID(Integer id) {
        Currency currency = null;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                currency = new Currency(
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

    @Override
    public Optional<Currency> update(Currency currency) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {

            preparedStatement.setString(1, currency.getCode());
            preparedStatement.setString(2, currency.getFullName());
            preparedStatement.setString(3, currency.getSign());
            preparedStatement.setInt(4, currency.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update currency '" + currency.getCode() + "' in the database");
        }
        return Optional.of(currency);
    }
}
