package org.example.tests;

import org.example.factory.WebDriverFactory;
import org.example.listener.AllureEnvironmentListener;
import org.example.listener.AllureTestListener;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.util.logging.Logger;

import static org.example.utils.ParameterProvider.getBoolean;

/**
 * Базовый класс для всех тестовых классов.
 *
 * <p>Управляет жизненным циклом {@link WebDriver} через
 * {@link WebDriverFactory}, который хранит драйвер в {@link ThreadLocal}
 * для корректной параллельной работы.</p>
 */
@Listeners({
        AllureTestListener.class,
        AllureEnvironmentListener.class,
})
public abstract class BaseTest {
    private static final Logger LOGGER = Logger.getLogger(BaseTest.class.getName());

    public WebDriver getDriver() {
        return WebDriverFactory.getDriver();
    }

    /**
     * Инициализирует {@link WebDriver} перед каждым тестовым методом.
     *
     * <p>Браузер берётся из параметра TestNG {@code browser} (из
     * {@code testng.xml}); режим headless — из конфигурации
     * ({@code config.properties} с возможностью переопределения через
     * системные свойства).</p>
     *
     * @param browser имя браузера из параметра TestNG, по умолчанию {@code chrome}
     */
    @BeforeMethod(alwaysRun = true)
    @Parameters("browser")
    public void setUp(@Optional("chrome") String browser) {
        boolean headless = getBoolean("headless", false);

        LOGGER.info(() -> "setUp: browser=" + browser + ", headless=" + headless);
        WebDriverFactory.createDriver(browser, headless);
    }

    /**
     * Завершает работу {@link WebDriver} после каждого тестового метода.
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        WebDriverFactory.quitDriver();
    }
}