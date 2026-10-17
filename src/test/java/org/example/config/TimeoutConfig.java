package org.example.config;

/**
 * Содержит значения таймаутов.
 */
public final class TimeoutConfig {
    private TimeoutConfig() {
    }

    /**
     * Таймаут по умолчанию для стандартных операций.
     */
    public static final int DEFAULT_TIMEOUT = 20;

    /**
     * Таймаут загрузки страницы.
     */
    public static final int PAGE_LOAD_TIMEOUT_SECONDS = 60;

    /**
     * Таймаут выполнения скрипта.
     */
    public static final int SCRIPT_TIMEOUT_SECONDS = 30;
}