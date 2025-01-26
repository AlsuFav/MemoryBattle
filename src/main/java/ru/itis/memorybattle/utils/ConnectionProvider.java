package ru.itis.memorybattle.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import ru.itis.memorybattle.exceptions.DbConfigException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionProvider {
    private static HikariDataSource dataSource;
    private static ConnectionProvider instance;

    private ConnectionProvider() throws DbConfigException {
        try {

            Properties properties = new Properties();
            try {
                properties.load(Thread.currentThread()
                        .getContextClassLoader()
                        .getResourceAsStream("application.properties"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String url = properties.getProperty("database.url");
            String username =  properties.getProperty("database.username");
            String password = properties.getProperty("database.password");
            String driver = properties.getProperty("database.driver");


            int maxPoolSize = Integer.parseInt(properties.getProperty("database.hikari.max-pool-size"));

            Class.forName(driver);

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(url);
            config.setUsername(username);
            config.setPassword(password);
            config.setConnectionTimeout(50000);
            config.setMaximumPoolSize(maxPoolSize);

            dataSource = new HikariDataSource(config);

            Flyway flyway = Flyway.configure().dataSource(dataSource).load();
            flyway.migrate();
        } catch (ClassNotFoundException e) {
            throw new DbConfigException(LogMessages.ESTABLISH_CONNECTION_DB_EXCEPTION);
        }

    }

    public static ConnectionProvider getInstance() throws DbConfigException {
        if (instance == null) {
            synchronized (ConnectionProvider.class) {
                if (instance == null) {
                    instance = new ConnectionProvider();
                }
            }
        }
        return instance;
    }


    public synchronized Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }


    public synchronized void releaseConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }


    public void destroy() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

}