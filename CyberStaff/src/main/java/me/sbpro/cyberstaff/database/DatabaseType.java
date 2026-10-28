package me.sbpro.cyberstaff.database;

import java.util.Locale;

/** Storage backends supported by CyberStaff. */
public enum DatabaseType {

    /** Local file in the plugin data folder (default). */
    SQLITE,

    /** Shared MySQL / MariaDB server (optional). */
    MYSQL;

    /**
     * Reads the backend name from the configuration. Missing or unknown values fall back to SQLite.
     */
    public static DatabaseType fromConfig(String value) {
        if (value == null) {
            return SQLITE;
        }

        return switch (value.trim().toLowerCase(Locale.ROOT)) {
            case "mysql", "mariadb" -> MYSQL;
            default -> SQLITE;
        };
    }
}
