package org.example.tests;

import org.example.factory.WebDriverFactory;
import org.example.listener.AllureTestListener;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Базовый класс для всех тестовых классов.
 */
@Listeners({
        AllureTestListener.class
})
public abstract class BaseTest {
    private static final Logger LOGGER = Logger.getLogger(BaseTest.class.getName());
    protected WebDriver driver;

    public WebDriver getDriver() {
        return driver;
    }

    /**
     * Инициализирует {@link WebDriver} перед каждым тестовым методом.
     *
     * @param browser имя браузера, по умолчанию {@code chrome}
     */
    @BeforeMethod
    @Parameters("browser")
    public void setUp(@Optional("chrome") String browser) {
        boolean headless = Boolean.parseBoolean(
                System.getProperty("headless", "false")
        );

        LOGGER.info(() -> "setUp: browser=" + browser + ", headless=" + headless);
        driver = WebDriverFactory.create(browser, headless);
    }

    /**
     * Завершает работу {@link WebDriver} после каждого тестового метода.
     */
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            try {
                driver.quit();
                LOGGER.info("WebDriver закрыт");
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Ошибка при закрытии WebDriver", e);
            } finally {
                driver = null;
            }
        }
    }
}