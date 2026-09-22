package org.example.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.example.config.TimeoutConfig.DEFAULT_TIMEOUT;

public final class WaitUtils {
    private WaitUtils() {
    }

    public static WebDriverWait getWait(WebDriver driver) {
        return new WebDriverWait(
                driver,
                Duration.ofSeconds(DEFAULT_TIMEOUT)
        );
    }

    public static WebElement waitForVisibility(WebDriver driver, WebElement element) {
        return getWait(driver)
                .until(ExpectedConditions.visibilityOf(element));
    }

    public static WebElement waitForClickable(WebDriver driver, WebElement element) {
        return getWait(driver)
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    public static boolean waitForInvisibility(WebDriver driver, WebElement element) {
        return getWait(driver)
                .until(ExpectedConditions.invisibilityOf(element));
    }
}