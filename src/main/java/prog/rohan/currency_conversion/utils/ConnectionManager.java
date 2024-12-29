package prog.rohan.currency_conversion.utils;

import java.sql.Connection;
import java.sql.SQLException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import prog.rohan.currency_conversion.exceptions.DatabaseException;

public class ConnectionManager {
    private static final HikariConfig config = new HikariConfig("/aplication.properties");
    private static final HikariDataSource dataSource = new HikariDataSource(config);

    private ConnectionManager(){};

    public static Connection getConnection() {

        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new DatabaseException("Can not get connection!");
        }

    }
}
