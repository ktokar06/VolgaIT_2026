package org.example.listener;

import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.model.Label;
import org.example.tests.BaseTest;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.testng.*;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

import static io.qameta.allure.Allure.parameter;
import static java.lang.Boolean.TRUE;
import static java.lang.System.getProperty;
import static java.lang.System.lineSeparator;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.WARNING;
import static java.util.logging.Logger.getLogger;
import static org.example.utils.ParameterProvider.getBoolean;
import static org.example.utils.TestIdentifierUtils.buildTestCommand;
import static org.openqa.selenium.OutputType.BYTES;
import static org.openqa.selenium.logging.LogType.BROWSER;
import static org.testng.ITestResult.SUCCESS;
import static org.testng.ITestResult.SKIP;
import static org.testng.ITestResult.SUCCESS_PERCENTAGE_FAILURE;

/**
 * Слушатель TestNG, интегрированный с Allure.
 */
public class AllureTestListener implements ITestListener, IInvokedMethodListener,
        IConfigurationListener, ISuiteListener {
    private static final Logger LOGGER = getLogger(AllureTestListener.class.getName());
    private static final String CAPTURED_ATTRIBUTE = AllureTestListener.class.getName() + ".captured";

    private static final String EVENT_FAILURE = "failure";
    private static final String EVENT_SUCCESS = "success";
    private static final String EVENT_SKIP = "skip";
    private static final String EVENT_RETRY = "retry";

    private static final DateTimeFormatter TIMESTAMP =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private final boolean screenshotOnFailure = getBoolean("screenshot.on.failure", true);
    private final boolean screenshotOnSuccess = getBoolean("screenshot.on.success", false);
    private final boolean screenshotOnSkip = getBoolean("screenshot.on.skip", false);
    private final boolean screenshotOnRetry = getBoolean("screenshot.on.retry", true);

    /**
     * Создаёт слушатель и логирует конфигурацию скриншотов.
     */
    public AllureTestListener() {
        LOGGER.info(() -> "Скриншоты: failure=" + screenshotOnFailure
                + ", success=" + screenshotOnSuccess
                + ", skip=" + screenshotOnSkip
                + ", retry=" + screenshotOnRetry);
    }

    /**
     * Вызывается перед стартом каждого тестового метода.
     *
     * <p>Логирует начало теста. Метаданные добавляются в beforeInvocation.</p>
     *
     * @param result результат выполнения теста
     */
    @Override
    public void onTestStart(ITestResult result) {
        LOGGER.info(() -> "START: " + result.getName());
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult result) {
        if (!method.isTestMethod()) {
            return;
        }
        // Здесь Allure уже создал test case; onTestStart зависит от порядка слушателей.
        addEnvironment(result);
        RetryAnalyzer.currentAttempt(result);
        Class<?> testClass = result.getTestClass().getRealClass();
        Epic epic = testClass.getAnnotation(Epic.class);
        Feature feature = testClass.getAnnotation(Feature.class);
        Allure.getLifecycle().updateTestCase(test -> {
            test.getLabels().removeIf(label -> "parentSuite".equals(label.getName())
                    || "suite".equals(label.getName()) || "subSuite".equals(label.getName()));
            test.getLabels().add(new Label().setName("parentSuite")
                    .setValue(result.getTestContext().getName()));
            test.getLabels().add(new Label().setName("suite")
                    .setValue(epic == null ? testClass.getSimpleName() : epic.value()));
            test.getLabels().add(new Label().setName("subSuite")
                    .setValue(feature == null ? testClass.getSimpleName() : feature.value()));
            if (result.getParameters().length > 0) {
                test.setName(test.getName() + " " + Arrays.toString(result.getParameters()));
            }
        });
        if (feature != null) {
            Allure.label("tag", feature.value());
        }
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
        String event = getEvent(result);
        LOGGER.warning(() -> (EVENT_RETRY.equals(event) ? "RETRY-FAIL: " : "FAILED: ") + result.getName());
        capture(result, event);
    }

    /**
     * Вызывается при успешном прохождении теста.
     *
     * @param result результат выполнения теста
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        LOGGER.info(() -> "SUCCESS: " + result.getName());
        capture(result, EVENT_SUCCESS);
    }

    /**
     * Вызывается при пропуске теста.
     *
     * @param result результат выполнения теста
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        LOGGER.warning(() -> "SKIPPED: " + result.getName());
        capture(result, result.wasRetried() ? EVENT_RETRY : EVENT_SKIP);
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
        capture(result, EVENT_FAILURE);
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
        capture(result, EVENT_FAILURE);
    }

    /**
     * Вызывается при пропуске конфигурационного метода.
     *
     * @param result результат выполнения метода
     */
    @Override
    public void onConfigurationSkip(ITestResult result) {
        LOGGER.warning(() -> "CONFIGURATION_SKIPPED: " + result.getName());
        capture(result, EVENT_SKIP);
    }

    /**
     * Определяет имя события по статусу результата теста.
     *
     * <p>Падение теста, после которого последует повторный запуск,
     * трактуется как событие {@value #EVENT_RETRY}; финальное падение —
     * как {@value #EVENT_FAILURE}.</p>
     *
     * @param result результат выполнения теста
     * @return одно из значений: {@value #EVENT_SUCCESS},
     *         {@value #EVENT_SKIP}, {@value #EVENT_RETRY},
     *         {@value #EVENT_FAILURE}
     */
    private String getEvent(ITestResult result) {
        return switch (result.getStatus()) {
            case SUCCESS -> EVENT_SUCCESS;
            case SKIP -> EVENT_SKIP;
            case SUCCESS_PERCENTAGE_FAILURE -> EVENT_FAILURE;
            default -> willRetry(result) ? EVENT_RETRY : EVENT_FAILURE;
        };
    }

    /**
     * Определяет, будет ли тест перезапущен после текущего падения.
     *
     * @param result результат выполнения теста
     * @return {@code true}, если ещё остались попытки повтора
     */
    private boolean willRetry(ITestResult result) {
        return RetryAnalyzer.willRetry(result);
    }

    /**
     * Проверяет, включены ли скриншоты для указанного события.
     *
     * @param event имя события
     * @return {@code true}, если скриншот для события настроен
     */
    private boolean isScreenshotEnabled(String event) {
        return switch (event) {
            case EVENT_FAILURE -> screenshotOnFailure;
            case EVENT_SUCCESS -> screenshotOnSuccess;
            case EVENT_SKIP -> screenshotOnSkip;
            case EVENT_RETRY -> screenshotOnRetry;
            default -> false;
        };
    }

    /**
     * Собирает информацию о тесте: скриншот (по конфигурации события)
     * и логи браузера. В рамках одной попытки выполняется один раз.
     *
     * @param result результат выполнения теста
     * @param event  имя события
     */
    private void capture(ITestResult result, String event) {
        if (result.getAttribute(CAPTURED_ATTRIBUTE) != null) {
            return;
        }

        try {
            LOGGER.fine(() -> "Сбор информации для события: " + event);

            addScreenshotIfConfigured(result, event);
            addDiagnostics(result, event);
            addBrowserLogs(result);

            result.setAttribute(CAPTURED_ATTRIBUTE, TRUE);
        } catch (Exception e) {
            LOGGER.log(WARNING, "Ошибка при сборе информации для Allure", e);
        }
    }

    /**
     * Добавляет скриншот в Allure, если событие включено в конфигурации
     * ({@code screenshot.on.*}).
     *
     * <p>Имя файла уникально и содержит имя теста, номер попытки и
     * метку времени: {@code testName_attemptN_timestamp.png}. Благодаря
     * этому вложения разных попыток не перезатирают друг друга.</p>
     *
     * @param result результат выполнения теста
     * @param event  имя события
     */
    private void addScreenshotIfConfigured(ITestResult result, String event) {
        if (!isScreenshotEnabled(event)) {
            return;
        }

        WebDriver driver = getDriver(result);
        if (!(driver instanceof TakesScreenshot)) {
            return;
        }

        try {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(BYTES);
            if (screenshot.length == 0) {
                return;
            }

            String fileName = buildScreenshotName(result);
            Allure.addAttachment(fileName, "image/png", new ByteArrayInputStream(screenshot), ".png");
            LOGGER.fine(() -> "Скриншот добавлен: " + fileName);

        } catch (Exception e) {
            LOGGER.log(WARNING, "Не удалось сделать скриншот", e);
        }
    }

    /**
     * Формирует уникальное имя скриншота вида
     * {@code testName_attemptN_timestamp.png}.
     *
     * @param result результат выполнения теста
     * @return уникальное имя файла скриншота
     */
    private String buildScreenshotName(ITestResult result) {
        int attempt = RetryAnalyzer.currentAttempt(result);
        String timestamp = LocalDateTime.now().format(TIMESTAMP);
        return result.getName() + "_attempt" + attempt + "_" + timestamp + ".png";
    }

    private void addDiagnostics(ITestResult result, String event) {
        StringBuilder context = new StringBuilder()
                .append("Тест: ").append(result.getTestClass().getName()).append('.').append(result.getName())
                .append("\nДанные: ").append(Arrays.toString(result.getParameters()))
                .append("\nСобытие: ").append(event)
                .append("\nПопытка: ").append(RetryAnalyzer.currentAttempt(result))
                .append("\nЛимит повторов: ").append(RetryAnalyzer.maxRetries())
                .append("\nПоток: ").append(Thread.currentThread().getName())
                .append("\nКоманда повторного запуска метода:\n./gradlew test -Dheadless=")
                .append(getBoolean("headless", false)).append(' ').append(buildTestCommand(result));
        WebDriver driver = getDriver(result);
        if (driver != null) {
            try {
                String url = driver.getCurrentUrl();
                context.append("\nURL: ").append(url).append("\nЗаголовок: ").append(driver.getTitle());
                Allure.link("Страница в конце теста", url);
                if (EVENT_FAILURE.equals(event) || EVENT_RETRY.equals(event)) {
                    Allure.addAttachment("DOM в момент ошибки", "text/html", driver.getPageSource(), ".html");
                    if (driver instanceof HasCapabilities) {
                        addTextAttachment("WebDriver capabilities", ((HasCapabilities) driver).getCapabilities().toString());
                    }
                }
            } catch (Exception e) {
                context.append("\nДиагностика WebDriver недоступна: ").append(e.getMessage());
            }
        }
        addTextAttachment("Контекст выполнения", context.toString());
        if (result.getThrowable() != null) {
            StringWriter trace = new StringWriter();
            result.getThrowable().printStackTrace(new PrintWriter(trace));
            addTextAttachment("Ошибка и stack trace", trace.toString());
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
        String browserName = "неизвестно";

        if (driver instanceof HasCapabilities) {
            try {
                browserName = ((HasCapabilities) driver).getCapabilities().getBrowserName();
            } catch (Exception ignored) {
                LOGGER.fine("Не удалось получить имя браузера");
            }
        }

        builder.append("Браузер: ")
                .append(browserName)
                .append(lineSeparator());

        if (driver == null) {
            builder.append("WebDriver недоступен").append(lineSeparator());
            addTextAttachment("Логи браузера", builder.toString());
            return;
        }

        try {
            LogEntries entries = driver.manage().logs().get(BROWSER);
            for (LogEntry entry : entries) {
                builder.append(entry).append(lineSeparator());
            }
            if (entries.getAll().isEmpty()) {
                builder.append("Записи в логах браузера отсутствуют").append(lineSeparator());
            }
        } catch (Exception e) {
            LOGGER.log(FINE, "Логи браузера недоступны", e);
            builder.append("Логи браузера недоступны: ")
                    .append(e.getMessage())
                    .append(lineSeparator());
        }

        addTextAttachment("Логи браузера", builder.toString());
    }

    /**
     * Добавляет параметры окружения (браузер, ОС) в отчёт Allure.
     *
     * <p>Вызывается перед телом тестового метода.
     * Благодаря этому при параллельном запуске Chrome и Firefox
     * каждый тест получает корректные параметры своего браузера.</p>
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

        String osName = getProperty("os.name", "unknown");
        String osVersion = getProperty("os.version", "unknown");

        parameter("Browser", browserName);
        parameter("Browser version", browserVersion, true);
        parameter("OS", osName, true);
        parameter("OS version", osVersion, true);
        parameter("Headless", getBoolean("headless", false), true);
    }

    /**
     * Добавляет текстовое вложение в отчёт Allure.
     *
     * @param name имя вложения
     * @param text текст вложения
     */
    private void addTextAttachment(String name, String text) {
        Allure.addAttachment(name, "text/plain", new ByteArrayInputStream(text.getBytes(UTF_8)), ".txt");
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