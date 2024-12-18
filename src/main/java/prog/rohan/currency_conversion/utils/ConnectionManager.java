package prog.rohan.currency_conversion.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    public static Connection openConnection() {

        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(
                    PropertiesUtil.get("db.url")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
