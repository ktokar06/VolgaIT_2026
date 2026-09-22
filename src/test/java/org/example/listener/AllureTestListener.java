package org.example.listener;

import io.qameta.allure.Allure;
import org.example.tests.BaseTest;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * TestNG listener для логирования результатов тестов
 * и создания скриншотов для заданных событий.
 */
public class AllureTestListener implements ITestListener {
    private static final String EVENTS_PROPERTY = "screenshot.events";
    private static final String DEFAULT_EVENTS = "failed,skipped";
    private final Set<String> screenshotEvents;

    /**
     * Создаёт listener и загружает список событий,
     * для которых необходимо делать скриншоты.
     */
    public AllureTestListener() {
        String raw = System.getProperty(EVENTS_PROPERTY, DEFAULT_EVENTS);
        screenshotEvents = new HashSet<>();

        String[] parts = raw.split(",");
        for (String part : parts) {
            String event = part.trim().toLowerCase(Locale.ROOT);
            if (!event.isEmpty()) {
                screenshotEvents.add(event);
            }
        }
    }

    /**
     * Выводит сообщение о начале теста.
     *
     * @param result результат теста
     */
    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("START: " + result.getName());
    }

    /**
     * Выводит сообщение об успешном завершении теста
     * и создаёт скриншот, если событие включено в настройки.
     *
     * @param result результат теста
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("SUCCESS: " + result.getName());
        handleScreenshot(result, "success");
    }

    /**
     * Выводит сообщение о падении теста
     * и создаёт скриншот, если событие включено в настройки.
     *
     * @param result результат теста
     */
    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("FAILED: " + result.getName());
        handleScreenshot(result, "failed");
    }

    /**
     * Выводит сообщение о пропущенном тесте
     * и создаёт скриншот, если событие включено в настройки.
     *
     * @param result результат теста
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("SKIPPED: " + result.getName());
        handleScreenshot(result, "skipped");
    }

    /**
     * Обрабатывает тест, который завершился с успехом
     * в рамках разрешённого процента успешности.
     *
     * @param result результат теста
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("FAILED_BUT_WITHIN_SUCCESS: " + result.getName());
        handleScreenshot(result, "failedButWithinSuccessPercentage");
    }

    /**
     * Проверяет, включено ли создание скриншота
     * для указанного события.
     *
     * @param result результат теста
     * @param event событие теста
     */
    private void handleScreenshot(ITestResult result, String event) {
        String normalizedEvent = event.toLowerCase(Locale.ROOT);
        if (!screenshotEvents.contains(normalizedEvent)) {
            return;
        }

        attachScreenshot(result, normalizedEvent);
    }

    /**
     * Создаёт скриншот текущего состояния браузера
     * и добавляет его в Allure.
     *
     * @param result результат теста
     * @param event событие, при котором создан скриншот
     */
    private void attachScreenshot(ITestResult result, String event) {
        Object testInstance = result.getInstance();
        if (!(testInstance instanceof BaseTest baseTest)) {
            return;
        }

        WebDriver driver = baseTest.getDriver();
        if (!(driver instanceof TakesScreenshot)) {
            return;
        }

        try {
            byte[] screenshot = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.BYTES);

            if (screenshot.length == 0) {
                return;
            }

            Allure.addAttachment(
                    "Скриншот [" + event + "]",
                    "image/png",
                    new ByteArrayInputStream(screenshot),
                    ".png"
            );
        } catch (Exception e) {
            System.err.println(
                    "Не удалось сделать скриншот: " + e.getMessage()
            );
        }
    }
}