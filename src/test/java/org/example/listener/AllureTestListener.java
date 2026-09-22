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

public class AllureTestListener implements ITestListener {
    private static final String EVENTS_PROPERTY = "screenshot.events";
    private static final String DEFAULT_EVENTS = "failed,skipped";
    private final Set<String> screenshotEvents;

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

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("START: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("SUCCESS: " + result.getName());
        handleScreenshot(result, "success");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("FAILED: " + result.getName());
        handleScreenshot(result, "failed");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("SKIPPED: " + result.getName());
        handleScreenshot(result, "skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("FAILED_BUT_WITHIN_SUCCESS: " + result.getName());
        handleScreenshot(result, "failedButWithinSuccessPercentage");
    }

    private void handleScreenshot(ITestResult result, String event) {
        String normalizedEvent = event.toLowerCase(Locale.ROOT);
        if (!screenshotEvents.contains(normalizedEvent)) {
            return;
        }

        attachScreenshot(result, normalizedEvent);
    }

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