package org.example.factory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.time.Duration.ofSeconds;
import static java.util.logging.Logger.getLogger;
import static org.example.config.TimeoutConfig.PAGE_LOAD_TIMEOUT_SECONDS;
import static org.example.config.TimeoutConfig.SCRIPT_TIMEOUT_SECONDS;

/**
 * Фабрика для создания экземпляров {@link WebDriver} в зависимости
 * от указанного браузера и режима работы (обычный или headless).
 */
public final class WebDriverFactory {
    private static final Logger LOGGER = getLogger(WebDriverFactory.class.getName());

    private WebDriverFactory() {
    }

    /**
     * Создаёт новый экземпляр {@link WebDriver} для указанного браузера.
     *
     * @param browser  имя браузера ({@code chrome} или {@code firefox}),
     *                 регистр не важен
     * @param headless {@code true} для запуска в headless-режиме,
     *                 {@code false} для обычного режима
     * @return настроенный экземпляр {@link WebDriver}
     * @throws IllegalArgumentException если браузер не указан
     *                                  или не поддерживается
     */
    public static WebDriver create(String browser, boolean headless) {
        if (browser == null || browser.isBlank()) {
            throw new IllegalArgumentException("Браузер не указан");
        }

        LOGGER.info(() -> "Создание WebDriver: browser=" + browser + ", headless=" + headless);

        WebDriver driver;
        switch (browser.toLowerCase(Locale.ROOT)) {
            case "chrome":
                driver = createChromeDriver(headless);
                break;
            case "firefox":
                driver = createFirefoxDriver(headless);
                break;
            default:
                throw new IllegalArgumentException("Неподдерживаемый браузер: " + browser);
        }

        driver.manage().timeouts().pageLoadTimeout(ofSeconds(PAGE_LOAD_TIMEOUT_SECONDS));
        driver.manage().timeouts().scriptTimeout(ofSeconds(SCRIPT_TIMEOUT_SECONDS));

        if (!headless) {
            driver.manage().window().maximize();
        }

        LOGGER.info(() -> "WebDriver создан: " + driver.getClass().getSimpleName());
        return driver;
    }

    /**
     * Создаёт экземпляр {@link ChromeDriver} с заданными опциями.
     *
     * @param headless {@code true} для запуска в headless-режиме
     * @return настроенный экземпляр {@link ChromeDriver}
     */
    private static WebDriver createChromeDriver(boolean headless) {
        ChromeOptions options = new ChromeOptions();

        options.addArguments(
                "--disable-blink-features=AutomationControlled",
                "--no-sandbox",
                "--disable-dev-shm-usage"
        );

        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        }

        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.BROWSER, Level.ALL);
        logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
        options.setCapability("goog:loggingPrefs", logPrefs);

        return new ChromeDriver(options);
    }

    /**
     * Создаёт экземпляр {@link FirefoxDriver} с заданными опциями.
     *
     * @param headless {@code true} для запуска в headless-режиме
     * @return настроенный экземпляр {@link FirefoxDriver}
     */
    private static WebDriver createFirefoxDriver(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();

        if (headless) {
            options.addArguments("-headless");
            options.addArguments("--width=1920");
            options.addArguments("--height=1080");
        }

        return new FirefoxDriver(options);
    }
}