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
 * Фабрика {@link WebDriver}, хранящая экземпляры в {@link ThreadLocal}.
 *
 * <p>Каждый поток получает собственный драйвер, что обеспечивает
 * корректную параллельную работу тестов. Создание, доступ и завершение
 * драйвера выполняются через {@link #createDriver(String, boolean)},
 * {@link #getDriver()} и {@link #quitDriver()}.</p>
 *
 * <p>Поддерживается локальный запуск Chrome и Firefox.</p>
 */
public final class WebDriverFactory {
    private static final Logger LOGGER = getLogger(WebDriverFactory.class.getName());
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private WebDriverFactory() {
    }

    /**
     * Создаёт драйвер для текущего потока и сохраняет его в {@link ThreadLocal}.
     *
     * @param browser  имя браузера ({@code chrome} или {@code firefox}),
     *                 регистр не важен
     * @param headless {@code true} для headless-режима
     * @return созданный {@link WebDriver} для текущего потока
     * @throws IllegalArgumentException если браузер не указан или не поддерживается
     */
    public static WebDriver createDriver(String browser, boolean headless) {
        if (browser == null || browser.isBlank()) {
            throw new IllegalArgumentException("Браузер не указан");
        }

        LOGGER.info(() -> "Создание WebDriver: browser=" + browser + ", headless=" + headless);

        WebDriver driver = buildDriver(browser.toLowerCase(Locale.ROOT), headless);
        DRIVER.set(driver);
        try {
            driver.manage().timeouts().pageLoadTimeout(ofSeconds(PAGE_LOAD_TIMEOUT_SECONDS));
            driver.manage().timeouts().scriptTimeout(ofSeconds(SCRIPT_TIMEOUT_SECONDS));
            if (!headless) {
                driver.manage().window().maximize();
            }
        } catch (RuntimeException e) {
            quitDriver();
            throw e;
        }
        LOGGER.info(() -> "WebDriver создан: " + driver.getClass().getSimpleName());
        return driver;
    }

    /**
     * Возвращает {@link WebDriver}, привязанный к текущему потоку.
     *
     * @return драйвер текущего потока или {@code null}, если он не создан
     */
    public static WebDriver getDriver() {
        return DRIVER.get();
    }

    /**
     * Завершает драйвер текущего потока и очищает {@link ThreadLocal}.
     *
     * <p>Исключения при закрытии логируются и не пробрасываются, чтобы
     * не мешать завершению теста. {@code remove()} вызывается всегда,
     * предотвращая утечку драйвера между тестами одного потока.</p>
     */
    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            return;
        }

        try {
            driver.quit();
            LOGGER.info("WebDriver закрыт");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Ошибка при закрытии WebDriver", e);
        } finally {
            DRIVER.remove();
        }
    }

    /**
     * Создаёт локальный драйвер для указанного браузера.
     *
     * @param browser  имя браузера в нижнем регистре
     * @param headless {@code true} для headless-режима
     * @return экземпляр {@link WebDriver}
     */
    private static WebDriver buildDriver(String browser, boolean headless) {
        switch (browser) {
            case "chrome":
                return new ChromeDriver(chromeOptions(headless));
            case "firefox":
                return new FirefoxDriver(firefoxOptions(headless));
            default:
                throw new IllegalArgumentException("Неподдерживаемый браузер: " + browser);
        }
    }

    /**
     * Формирует опции {@link ChromeOptions}.
     *
     * @param headless {@code true} для headless-режима
     * @return настроенные опции Chrome
     */
    private static ChromeOptions chromeOptions(boolean headless) {
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

        return options;
    }

    /**
     * Формирует опции {@link FirefoxOptions}.
     *
     * @param headless {@code true} для headless-режима
     * @return настроенные опции Firefox
     */
    private static FirefoxOptions firefoxOptions(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();

        if (headless) {
            options.addArguments("-headless");
            options.addArguments("--width=1920");
            options.addArguments("--height=1080");
        }

        return options;
    }
}