package org.example.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.example.config.TimeoutConfig.DEFAULT_TIMEOUT;

/**
 * Вспомогательные методы для ожидания состояний элементов.
 */
public final class WaitUtils {

    private WaitUtils() {
    }

    /**
     * Создаёт WebDriverWait со стандартным таймаутом.
     *
     * @param driver экземпляр WebDriver
     * @return настроенный WebDriverWait
     */
    public static WebDriverWait getWait(WebDriver driver) {
        return new WebDriverWait(
                driver,
                Duration.ofSeconds(DEFAULT_TIMEOUT)
        );
    }

    /**
     * Ожидает отображения элемента.
     *
     * @param driver экземпляр WebDriver
     * @param element элемент для ожидания
     * @return отображаемый элемент
     */
    public static WebElement waitForVisibility(WebDriver driver, WebElement element) {
        return getWait(driver)
                .until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Ожидает, пока элемент станет доступен для взаимодействия.
     *
     * @param driver экземпляр WebDriver
     * @param element элемент для ожидания
     * @return доступный для взаимодействия элемент
     */
    public static WebElement waitForClickable(WebDriver driver, WebElement element) {
        return getWait(driver)
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Ожидает исчезновения элемента.
     *
     * @param driver экземпляр WebDriver
     * @param element элемент для ожидания
     * @return true, если элемент исчез
     */
    public static boolean waitForInvisibility(WebDriver driver, WebElement element) {
        return getWait(driver)
                .until(ExpectedConditions.invisibilityOf(element));
    }
}