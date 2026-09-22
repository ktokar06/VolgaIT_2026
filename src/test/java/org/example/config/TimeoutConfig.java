package org.example.config;

/**
 * Конфигурация таймаутов для ожиданий в тестах.
 */
public final class TimeoutConfig {

    private TimeoutConfig() {
    }

    public static final int DEFAULT_TIMEOUT = 10;
    public static final int SHORT_TIMEOUT = 5;
    public static final int ALERT_TIMEOUT = 5;
}