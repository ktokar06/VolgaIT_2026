package org.example.utils;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.example.config.TimeoutConfig.DEFAULT_TIMEOUT;

/**
 * Класс для работы с ожиданиями Selenium.
 */
public final class WaitUtils {
    private WaitUtils() {
    }

    /**
     * Создаёт {@link WebDriverWait} с таймаутом по умолчанию.
     *
     * @param driver экземпляр {@link WebDriver}
     * @return настроенный {@link WebDriverWait}
     */
    public static WebDriverWait getWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    /**
     * Ожидает видимости элемента.
     *
     * @param driver  экземпляр {@link WebDriver}
     * @param element элемент для ожидания
     * @return видимый элемент
     */
    public static WebElement waitForVisibility(WebDriver driver, WebElement element) {
        return getWait(driver).until(
                ExpectedConditions.visibilityOf(element)
        );
    }

    /**
     * Ожидает кликабельности элемента.
     *
     * @param driver  экземпляр {@link WebDriver}
     * @param element элемент для ожидания
     * @return кликабельный элемент
     */
    public static WebElement waitForClickable(WebDriver driver, WebElement element) {
        return getWait(driver).until(
                ExpectedConditions.elementToBeClickable(element)
        );
    }

    /**
     * Ожидает исчезновения элемента.
     *
     * @param driver  экземпляр {@link WebDriver}
     * @param element элемент для ожидания
     * @return {@code true}, если элемент стал невидимым
     */
    public static boolean waitForInvisibility(WebDriver driver, WebElement element) {
        return getWait(driver).until(
                ExpectedConditions.invisibilityOf(element)
        );
    }

    /**
     * Ожидает, пока список элементов не станет непустым,
     * и все элементы не станут видимыми.
     *
     * @param driver   экземпляр {@link WebDriver}
     * @param elements список элементов для ожидания
     * @return список видимых элементов
     */
    public static List<WebElement> waitForElements(WebDriver driver, List<WebElement> elements) {
        getWait(driver).until(currentDriver -> !elements.isEmpty());
        return getWait(driver).until(
                ExpectedConditions.visibilityOfAllElements(elements)
        );
    }

    /**
     * Проверяет отображение элемента, перехватывая
     * исключения {@link NoSuchElementException} и
     * {@link StaleElementReferenceException}.
     *
     * @param element проверяемый элемент
     * @return {@code true}, если элемент отображается,
     *         иначе {@code false}
     */
    public static boolean isDisplayedSafe(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

}