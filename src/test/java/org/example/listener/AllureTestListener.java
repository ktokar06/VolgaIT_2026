package org.example.listener;

import io.qameta.allure.Allure;
import org.example.tests.BaseTest;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.testng.IConfigurationListener;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Слушатель TestNG, интегрированный с Allure.
 */
public class AllureTestListener implements ITestListener, IInvokedMethodListener, IConfigurationListener {
    private static final Logger LOGGER = Logger.getLogger(AllureTestListener.class.getName());
    private static final String EVENTS_PROPERTY = "screenshot.events";
    private static final String DEFAULT_EVENTS = "failed,skipped";
    private static final String CAPTURED_ATTRIBUTE = AllureTestListener.class.getName() + ".captured";
    private final Set<String> screenshotEvents;

    /**
     * Создаёт слушатель и загружает настройки скриншотов
     * из системного свойства {@value #EVENTS_PROPERTY}.
     */
    public AllureTestListener() {
        screenshotEvents = loadScreenshotEvents();
        LOGGER.info(() -> "События для скриншотов: " + screenshotEvents);
    }

    /**
     * Вызывается перед стартом каждого тестового метода.
     *
     * <p>Добавляет в Allure параметры окружения (браузер, ОС)
     * и логирует начало теста.</p>
     *
     * @param result результат выполнения теста
     */
    @Override
    public void onTestStart(ITestResult result) {
        addEnvironment(result);
        LOGGER.info(() -> "START: " + result.getName());
    }

    /**
     * Вызывается после выполнения каждого invoked-метода (тестового
     * или конфигурационного).
     *
     * <p>Для тестовых методов запускает сбор информации в зависимости
     * от итогового статуса теста.</p>
     *
     * @param method invoked-метод
     * @param result результат выполнения теста
     */
    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult result) {
        if (method.isTestMethod()) {
            capture(result, getEvent(result));
        }
    }

    /**
     * Вызывается при падении теста.
     *
     * @param result результат выполнения теста
     */
    @Override
    public void onTestFailure(ITestResult result) {
        LOGGER.warning(() -> "FAILED: " + result.getName());
        capture(result, "failed");
    }

    /**
     * Вызывается при успешном прохождении теста.
     *
     * @param result результат выполнения теста
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        LOGGER.info(() -> "SUCCESS: " + result.getName());
        capture(result, "success");
    }

    /**
     * Вызывается при пропуске теста.
     *
     * @param result результат выполнения теста
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        LOGGER.warning(() -> "SKIPPED: " + result.getName());
        capture(result, "skipped");
    }

    /**
     * Вызывается, когда тест упал, но уложился в допустимый
     * процент успешности.
     *
     * @param result результат выполнения теста
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        LOGGER.warning(() -> "FAILED_BUT_WITHIN_SUCCESS: " + result.getName());
        capture(result, "failedButWithinSuccessPercentage");
    }

    /**
     * Вызывается при падении конфигурационного метода
     * (например, {@code @BeforeMethod}).
     *
     * @param result результат выполнения метода
     */
    @Override
    public void onConfigurationFailure(ITestResult result) {
        LOGGER.severe(() -> "CONFIGURATION_FAILED: " + result.getName());
        capture(result, "configurationfailure");
    }

    /**
     * Вызывается при пропуске конфигурационного метода.
     *
     * @param result результат выполнения метода
     */
    @Override
    public void onConfigurationSkip(ITestResult result) {
        LOGGER.warning(() -> "CONFIGURATION_SKIPPED: " + result.getName());
        capture(result, "skipped");
    }

    /**
     * Загружает множество событий для снятия скриншотов из системного
     * свойства {@value #EVENTS_PROPERTY}.
     *
     * <p>Значения разделяются запятыми, приводятся к нижнему регистру
     * и очищаются от пробелов. Пустые значения игнорируются.</p>
     *
     * @return множество событий в нижнем регистре; пустое множество,
     *         если ни одно событие не задано
     */
    private Set<String> loadScreenshotEvents() {
        Set<String> result = new HashSet<>();
        String raw = System.getProperty(EVENTS_PROPERTY, DEFAULT_EVENTS);
        String[] parts = raw.split(",");

        for (String part : parts) {
            String event = part.trim().toLowerCase(Locale.ROOT);
            if (!event.isEmpty()) {
                result.add(event);
            }
        }

        return result;
    }

    /**
     * Определяет строковое имя события по статусу результата теста.
     *
     * @param result результат выполнения теста
     * @return одно из значений: {@code success}, {@code skipped},
     *         {@code failedButWithinSuccessPercentage}, {@code failed}
     */
    private String getEvent(ITestResult result) {
        return switch (result.getStatus()) {
            case ITestResult.SUCCESS -> "success";
            case ITestResult.SKIP -> "skipped";
            case ITestResult.SUCCESS_PERCENTAGE_FAILURE -> "failedButWithinSuccessPercentage";
            default -> "failed";
        };
    }

    /**
     * Собирает всю доступную информацию о тесте: скриншот,
     * логи браузера, сведения об ОС.
     *
     * <p>Информация добавляется только один раз для каждого
     * {@link ITestResult} — повторные вызовы игнорируются.</p>
     *
     * @param result результат выполнения теста
     * @param event  имя события (например, {@code failed}, {@code skipped})
     */
    private void capture(ITestResult result, String event) {
        if (result.getAttribute(CAPTURED_ATTRIBUTE) != null) {
            return;
        }

        try {
            LOGGER.fine(() -> "Сбор информации для события: " + event);

            addScreenshotIfConfigured(result, event);
            addBrowserLogs(result);
            addOsInfo();

            result.setAttribute(CAPTURED_ATTRIBUTE, Boolean.TRUE);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Ошибка при сборе информации для Allure", e);
        }
    }

    /**
     * Добавляет скриншот в отчёт Allure, если событие включено
     * в настройках {@code screenshot.events}.
     *
     * <p>Если WebDriver не реализует {@link TakesScreenshot},
     * или скриншот пуст, вложение не добавляется.</p>
     *
     * @param result результат выполнения теста
     * @param event  имя события
     */
    private void addScreenshotIfConfigured(ITestResult result, String event) {
        String normalizedEvent = event.toLowerCase(Locale.ROOT);
        if (!screenshotEvents.contains(normalizedEvent) && !screenshotEvents.contains("all")) {
            return;
        }

        WebDriver driver = getDriver(result);
        if (!(driver instanceof TakesScreenshot)) {
            return;
        }

        try {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            if (screenshot.length == 0) {
                return;
            }

            Allure.addAttachment(
                    "Скриншот [" + normalizedEvent + "]",
                    "image/png",
                    new ByteArrayInputStream(screenshot),
                    ".png"
            );
            LOGGER.fine(() -> "Скриншот добавлен: " + normalizedEvent);

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Не удалось сделать скриншот", e);
        }
    }

    /**
     * Добавляет логи браузера в отчёт Allure.
     *
     * <p>Для Chrome логи доступны через {@link LogType#BROWSER}.
     * Для Firefox браузерные логи могут быть недоступны — в этом
     * случае в отчёт добавляется поясняющее сообщение, а не ошибка.</p>
     *
     * @param result результат выполнения теста
     */
    private void addBrowserLogs(ITestResult result) {
        WebDriver driver = getDriver(result);
        StringBuilder builder = new StringBuilder();
        String browserName = "unknown";

        if (driver instanceof HasCapabilities) {
            try {
                browserName = ((HasCapabilities) driver).getCapabilities().getBrowserName();
            } catch (Exception ignored) {
                LOGGER.fine("Не удалось получить имя браузера");
            }
        }

        builder.append("Browser: ").append(browserName).append(System.lineSeparator());

        if (driver == null) {
            builder.append("WebDriver is not available").append(System.lineSeparator());
            addTextAttachment("Browser logs", builder.toString());
            return;
        }

        try {
            LogEntries entries = driver.manage().logs().get(LogType.BROWSER);
            for (LogEntry entry : entries) {
                builder.append(entry).append(System.lineSeparator());
            }
            if (entries.getAll().isEmpty()) {
                builder.append("No browser log entries").append(System.lineSeparator());
            }
        } catch (Exception e) {
            LOGGER.log(Level.FINE, "Browser logs недоступны", e);
            builder.append("Browser logs are not available: ")
                    .append(e.getMessage())
                    .append(System.lineSeparator());
        }

        addTextAttachment("Browser logs", builder.toString());
    }

    /**
     * Добавляет сведения об операционной системе и Java в отчёт Allure.
     *
     * <p>Выполняется один раз за прогон — данные не меняются
     * между тестами, поэтому повторное добавление создавало бы
     * дублирующиеся вложения в Allure.</p>
     */
    private void addOsInfo() {

        StringBuilder builder = new StringBuilder();
        builder.append("os.name=").append(System.getProperty("os.name", "unknown")).append(System.lineSeparator());
        builder.append("os.version=").append(System.getProperty("os.version", "unknown")).append(System.lineSeparator());
        builder.append("os.arch=").append(System.getProperty("os.arch", "unknown")).append(System.lineSeparator());
        builder.append("java.version=").append(System.getProperty("java.version", "unknown")).append(System.lineSeparator());
        builder.append("java.vendor=").append(System.getProperty("java.vendor", "unknown")).append(System.lineSeparator());
        builder.append("user.country=").append(System.getProperty("user.country", "unknown")).append(System.lineSeparator());
        builder.append("user.timezone=").append(System.getProperty("user.timezone", "unknown")).append(System.lineSeparator());

        addTextAttachment("OS information", builder.toString());
    }

    /**
     * Добавляет параметры окружения (браузер, ОС) в отчёт Allure.
     *
     * <p>Вызывается один раз на тест — в {@link #onTestStart(ITestResult)}.</p>
     *
     * @param result результат выполнения теста
     */
    private void addEnvironment(ITestResult result) {
        String browserName = "unknown";
        String browserVersion = "unknown";
        WebDriver driver = getDriver(result);

        if (driver instanceof HasCapabilities) {
            try {
                browserName = ((HasCapabilities) driver).getCapabilities().getBrowserName();
                browserVersion = ((HasCapabilities) driver).getCapabilities().getBrowserVersion();
            } catch (Exception ignored) {
                LOGGER.fine("Не удалось получить capabilities браузера");
            }
        }

        String osName = System.getProperty("os.name", "unknown");
        String osVersion = System.getProperty("os.version", "unknown");

        Allure.parameter("Browser", browserName);
        Allure.parameter("Browser version", browserVersion);
        Allure.parameter("OS", osName);
        Allure.parameter("OS version", osVersion);

        addOsInfo();
    }

    /**
     * Добавляет текстовое вложение в отчёт Allure.
     *
     * @param name имя вложения
     * @param text текст вложения
     */
    private void addTextAttachment(String name, String text) {
        Allure.addAttachment(
                name,
                "text/plain",
                new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8)),
                ".txt"
        );
    }

    /**
     * Извлекает экземпляр {@link WebDriver} из результата теста.
     *
     * @param result результат выполнения теста
     * @return экземпляр {@link WebDriver} или {@code null},
     *         если тестовый класс не наследуется от {@link BaseTest}
     */
    private WebDriver getDriver(ITestResult result) {
        Object testInstance = result.getInstance();

        if (!(testInstance instanceof BaseTest)) {
            return null;
        }

        return ((BaseTest) testInstance).getDriver();
    }
}