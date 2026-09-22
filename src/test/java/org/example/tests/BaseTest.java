package org.example.tests;

import org.example.factory.WebDriverFactory;
import org.example.listener.AllureTestListener;
import org.example.listener.RetryTestListener;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.util.Locale;

/**
 * Базовый класс для UI-тестов.
 */
@Listeners({
        AllureTestListener.class,
        RetryTestListener.class
})
public abstract class BaseTest {

    protected WebDriver driver;

    public WebDriver getDriver() {
        return driver;
    }

    /**
     * Создаёт браузер перед выполнением каждого теста.
     *
     * @param browser название браузера
     */
    @BeforeMethod
    @Parameters("browser")
    public void setUp(@Optional("chrome") String browser) {
        boolean headless = Boolean.parseBoolean(
                System.getProperty("headless", "false")
        );

        driver = WebDriverFactory.create(
                browser.toLowerCase(Locale.ROOT),
                headless
        );
    }

    /**
     * Закрывает браузер после выполнения каждого теста.
     */
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                driver = null;
            }
        }
    }
}