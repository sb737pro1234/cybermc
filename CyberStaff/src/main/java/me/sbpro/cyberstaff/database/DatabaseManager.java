package me.sbpro.cyberstaff.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.sbpro.cyberstaff.CyberStaff;

import java.io.File;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;

public final class DatabaseManager {

    private final CyberStaff plugin;

    private HikariDataSource dataSource;

    private DatabaseType type = DatabaseType.SQLITE;

    public DatabaseManager(CyberStaff plugin) {
        this.plugin = plugin;
    }

    public boolean start() {

        String configured = plugin.getConfig().getString("database.type", "sqlite");
        type = DatabaseType.fromConfig(configured);

        if (!configured.equalsIgnoreCase("sqlite") && !configured.equalsIgnoreCase("mysql")
                && !configured.equalsIgnoreCase("mariadb")) {
            plugin.getLogger().warning(
                    "Unknown database.type '" + configured + "'. Falling back to SQLite."
            );
        }

        return type == DatabaseType.MYSQL ? startMySql() : startSqlite();
    }

    private boolean startSqlite() {

        String fileName = plugin.getConfig()
                .getString("database.sqlite-file", "staff.db");

        File databaseFile = new File(plugin.getDataFolder(), fileName);

        try {

            File parent = databaseFile.getParentFile();
            if (parent != null) {
                Files.createDirectories(parent.toPath());
            }

            HikariConfig hikari = new HikariConfig();

            hikari.setDriverClassName("org.sqlite.JDBC");
            hikari.setJdbcUrl("jdbc:sqlite:" + databaseFile.getAbsolutePath());
            hikari.setPoolName("CyberStaff-SQLite");

            // SQLite allows a single writer, so one connection keeps access simple and safe.
            hikari.setMaximumPoolSize(1);
            hikari.setMinimumIdle(1);

            hikari.addDataSourceProperty("journal_mode", "WAL");
            hikari.addDataSourceProperty("busy_timeout", "10000");
            hikari.addDataSourceProperty("synchronous", "NORMAL");

            dataSource = new HikariDataSource(hikari);

            try (Connection ignored = dataSource.getConnection()) {
                plugin.getLogger().info("Using SQLite database: " + databaseFile.getName());
            }

            return true;

        } catch (Exception exception) {

            plugin.getLogger().severe("CyberStaff could not open its SQLite database.");
            plugin.getLogger().severe("File: " + databaseFile.getAbsolutePath());
            plugin.getLogger().severe("Error: " + exception.getMessage());

            if (dataSource != null && !dataSource.isClosed()) {
                dataSource.close();
            }
            dataSource = null;
            return false;
        }
    }

    private boolean startMySql() {

        String host = plugin.getConfig()
                .getString("database.host", "localhost");

        int port = plugin.getConfig()
                .getInt("database.port", 3306);

        String database = plugin.getConfig()
                .getString("database.database", "grassgg_staff");

        String username = plugin.getConfig()
                .getString("database.username", "root");

        String password = plugin.getConfig()
                .getString("database.password", "");

        int poolSize = Math.max(
                2,
                plugin.getConfig()
                        .getInt("database.pool-size", 10)
        );

        boolean ssl = plugin.getConfig()
                .getBoolean("database.use-ssl", false);

        int connectionTimeout = plugin.getConfig()
                .getInt(
                        "database.connection-timeout-ms",
                        10000
                );

        int idleTimeout = plugin.getConfig()
                .getInt(
                        "database.idle-timeout-ms",
                        600000
                );

        int maxLifetime = plugin.getConfig()
                .getInt(
                        "database.max-lifetime-ms",
                        1800000
                );

        /*
         * Prevent the plugin from trying to connect with
         * the obvious placeholder values.
         */
        if (host.equalsIgnoreCase("YOUR-MYSQL-HOST")) {

            plugin.getLogger().severe(
                    "================================================"
            );

            plugin.getLogger().severe(
                    "MySQL has not been configured!"
            );

            plugin.getLogger().severe(
                    "Edit plugins/CyberStaff/config.yml"
            );

            plugin.getLogger().severe(
                    "and enter your MySQL connection details."
            );

            plugin.getLogger().severe(
                    "================================================"
            );

            return false;
        }

        if (username.equalsIgnoreCase("YOUR-MYSQL-USERNAME")) {

            plugin.getLogger().severe(
                    "MySQL username has not been configured!"
            );

            return false;
        }

        if (password.equals("YOUR-MYSQL-PASSWORD")) {

            plugin.getLogger().severe(
                    "MySQL password has not been configured!"
            );

            return false;
        }

        String jdbcUrl =
                "jdbc:mysql://"
                        + host
                        + ":"
                        + port
                        + "/"
                        + database
                        + "?useSSL="
                        + ssl
                        + "&serverTimezone=UTC"
                        + "&characterEncoding=utf8";

        try {

            HikariConfig hikari = new HikariConfig();

            hikari.setJdbcUrl(jdbcUrl);
            hikari.setUsername(username);
            hikari.setPassword(password);

            hikari.setMaximumPoolSize(poolSize);
            hikari.setMinimumIdle(
                    Math.min(2, poolSize)
            );

            hikari.setPoolName(
                    "CyberStaff-MySQL"
            );

            hikari.setConnectionTimeout(
                    connectionTimeout
            );

            hikari.setIdleTimeout(
                    idleTimeout
            );

            hikari.setMaxLifetime(
                    maxLifetime
            );

            hikari.addDataSourceProperty(
                    "cachePrepStmts",
                    "true"
            );

            hikari.addDataSourceProperty(
                    "prepStmtCacheSize",
                    "250"
            );

            hikari.addDataSourceProperty(
                    "prepStmtCacheSqlLimit",
                    "2048"
            );

            dataSource =
                    new HikariDataSource(hikari);

            /*
             * Test the connection before allowing
             * the plugin to finish enabling.
             */
            try (Connection ignored =
                         dataSource.getConnection()) {

                plugin.getLogger().info(
                        "Successfully connected to MySQL/MariaDB."
                );

                plugin.getLogger().info(
                        "Database: " + database
                );

                plugin.getLogger().info(
                        "Host: " + host + ":" + port
                );
            }

            return true;

        } catch (Exception exception) {

            plugin.getLogger().severe(
                    "================================================"
            );

            plugin.getLogger().severe(
                    "CyberStaff could not connect to MySQL/MariaDB."
            );

            plugin.getLogger().severe(
                    "Host: " + host + ":" + port
            );

            plugin.getLogger().severe(
                    "Database: " + database
            );

            plugin.getLogger().severe(
                    "Username: " + username
            );

            plugin.getLogger().severe(
                    "Error: " + exception.getMessage()
            );

            plugin.getLogger().severe(
                    "================================================"
            );

            if (dataSource != null &&
                    !dataSource.isClosed()) {

                dataSource.close();
            }

            dataSource = null;

            return false;
        }
    }

    public Connection connection()
            throws SQLException {

        if (dataSource == null ||
                dataSource.isClosed()) {

            throw new SQLException(
                    "Database connection pool is not available."
            );
        }

        return dataSource.getConnection();
    }

    public DatabaseType type() {
        return type;
    }

    public boolean isSqlite() {
        return type == DatabaseType.SQLITE;
    }

    /** SQL expression for the current time with millisecond precision, matching how timestamps are stored. */
    public String now() {
        return isSqlite()
                ? "CAST(unixepoch('subsec') * 1000 AS INTEGER)"
                : "CURRENT_TIMESTAMP(3)";
    }

    /** Adapts the portable SQL used by the repositories to the selected backend. */
    public String adapt(String sql) {
        return isSqlite() ? sql.replace("CURRENT_TIMESTAMP(3)", now()) : sql;
    }

    public boolean isConnected() {

        return dataSource != null &&
                !dataSource.isClosed();
    }

    public void close() {

        if (dataSource != null &&
                !dataSource.isClosed()) {

            dataSource.close();

            plugin.getLogger().info(
                    (isSqlite() ? "SQLite" : "MySQL") + " connection pool closed."
            );
        }
    }
}