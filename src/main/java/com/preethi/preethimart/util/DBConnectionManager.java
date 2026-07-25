package com.preethi.preethimart.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBConnectionManager {

    private static final Logger logger = LoggerFactory.getLogger(DBConnectionManager.class);
    private static volatile DBConnectionManager instance;
    private final HikariDataSource dataSource;

    private DBConnectionManager() {
        Properties props = loadProperties();

        String jdbcUrl = System.getenv("DB_URL");
        if (jdbcUrl == null || jdbcUrl.trim().isEmpty()) {
            jdbcUrl = props.getProperty("db.url", "jdbc:h2:mem:preethimart;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE");
        }

        String username = System.getenv("DB_USERNAME");
        if (username == null) {
            username = props.getProperty("db.username", "sa");
        }

        String password = System.getenv("DB_PASSWORD");
        if (password == null) {
            password = props.getProperty("db.password", "");
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("org.h2.Driver");
        config.setMaximumPoolSize(Integer.parseInt(props.getProperty("db.pool.maximum", "10")));
        config.setMinimumIdle(Integer.parseInt(props.getProperty("db.pool.minimum", "2")));
        config.setPoolName("PreethiMartHikariPool");
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        this.dataSource = new HikariDataSource(config);
        logger.info("HikariCP connection pool initialized for PreethiMart at URL: {}", jdbcUrl);

        initSchemaAndSeed();
    }

    public static DBConnectionManager getInstance() {
        if (instance == null) {
            synchronized (DBConnectionManager.class) {
                if (instance == null) {
                    instance = new DBConnectionManager();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("HikariCP connection pool closed successfully.");
        }
    }

    private Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (Exception e) {
            logger.warn("Could not load config.properties from classpath, using defaults: {}", e.getMessage());
        }
        return props;
    }

    private void initSchemaAndSeed() {
        try (Connection conn = getConnection()) {
            executeSqlScript(conn, "schema.sql");
            executeSqlScript(conn, "migrations/V2__order_status_workflow.sql");

            // Check if users table has records, if not populate seed.sql
            boolean hasUsers = false;
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM users")) {
                if (rs.next() && rs.getInt(1) > 0) {
                    hasUsers = true;
                }
            } catch (SQLException ignored) {
            }

            if (!hasUsers) {
                logger.info("Populating database with seed.sql data...");
                executeSqlScript(conn, "seed.sql");
            }

            // Ensure seed user passwords have fresh valid BCrypt hashes
            updateSeedPassword(conn, "admin@preethimart.com", "Admin@123");
            updateSeedPassword(conn, "seller1@preethimart.com", "Seller@123");
            updateSeedPassword(conn, "seller2@preethimart.com", "Seller@123");
            updateSeedPassword(conn, "buyer1@preethimart.com", "Buyer@123");
            updateSeedPassword(conn, "buyer2@preethimart.com", "Buyer@123");

            logger.info("Database schema and seed initialization completed successfully.");
        } catch (SQLException e) {
            logger.error("Database initialization failed: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initialize database schema: " + e.getMessage(), e);
        }
    }

    private void updateSeedPassword(Connection conn, String email, String rawPassword) {
        String hash = PasswordUtil.hash(rawPassword);
        String sql = "UPDATE users SET password_hash = ? WHERE email = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hash);
            ps.setString(2, email);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.warn("Could not update seed password for {}: {}", email, e.getMessage());
        }
    }

    private void executeSqlScript(Connection conn, String resourcePath) {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) {
                logger.warn("SQL script not found: {}", resourcePath);
                return;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                 Statement st = conn.createStatement()) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("--")) {
                        continue;
                    }
                    sb.append(line).append(" ");
                    if (line.endsWith(";")) {
                        String sql = sb.toString().trim();
                        if (sql.endsWith(";")) {
                            sql = sql.substring(0, sql.length() - 1);
                        }
                        if (!sql.isEmpty()) {
                            try {
                                st.execute(sql);
                            } catch (SQLException e) {
                                logger.debug("Notice during {}: {}", resourcePath, e.getMessage());
                            }
                        }
                        sb.setLength(0);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error executing SQL script {}: {}", resourcePath, e.getMessage(), e);
        }
    }
}
