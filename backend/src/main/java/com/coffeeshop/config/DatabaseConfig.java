package com.coffeeshop.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Database configuration and connection management
 */
public class DatabaseConfig {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);
    private static DatabaseConfig instance;
    private HikariDataSource dataSource;
    
    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/coffeeshop?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DB_USERNAME = "coffeeuser";
    private static final String DB_PASSWORD = "coffee123";
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    
    private DatabaseConfig() {}
    
    public static synchronized DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }
    
    public void initializeDatabase() throws SQLException {
        if (dataSource == null) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(DB_URL);
            config.setUsername(DB_USERNAME);
            config.setPassword(DB_PASSWORD);
            config.setDriverClassName(DB_DRIVER);
            
            // Connection pool settings
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setConnectionTimeout(30000);
            config.setIdleTimeout(600000);
            config.setMaxLifetime(1800000);
            config.setLeakDetectionThreshold(60000);
            
            // Connection validation
            config.setConnectionTestQuery("SELECT 1");
            config.setValidationTimeout(5000);
            
            dataSource = new HikariDataSource(config);
            
            // Test the connection
            try (Connection conn = getConnection()) {
                logger.info("Database connection established successfully");
            }
        }
    }
    
    public Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("DataSource not initialized");
        }
        return dataSource.getConnection();
    }
    
    public DataSource getDataSource() {
        return dataSource;
    }
    
    public void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("Database connection pool closed");
        }
    }
}
