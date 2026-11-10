package lt_web.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/** Configuration order: JVM property, environment variable, classpath file. */
public class DBConnection {
    private static final Properties CONFIG = load();

    private static Properties load() {
        Properties properties = new Properties();
        try (var input = DBConnection.class.getResourceAsStream("/database.properties")) {
            if (input != null) properties.load(input);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
        return properties;
    }

    private static String setting(String key, String env) {
        String value = System.getProperty(key);
        if (value == null) value = System.getenv(env);
        return value == null ? CONFIG.getProperty(key, "") : value;
    }

    public Connection getConnection() throws SQLException {
        // Tomcat may initialize DriverManager before scanning WEB-INF/lib.
        // Load the driver with this web application's class loader explicitly.
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Thiếu JDBC driver SQL Server trong WEB-INF/lib.", e);
        }
        return DriverManager.getConnection(setting("db.url", "DB_URL"),
                setting("db.user", "DB_USER"), setting("db.password", "DB_PASSWORD"));
    }
}
