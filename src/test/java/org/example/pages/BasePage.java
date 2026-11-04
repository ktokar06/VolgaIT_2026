package org.example.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

import static org.example.utils.WaitUtils.isDisplayedSafe;
import static org.example.utils.WaitUtils.waitForInvisibility;
import static org.example.utils.WaitUtils.waitForClickable;
import static org.example.utils.WaitUtils.waitForVisibility;
import static org.example.utils.ParameterProvider.getRequired;

public abstract class BasePage {
    protected final WebDriver driver;

    protected BasePage(WebDriver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("WebDriver не может быть null");
        }

        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @Step("Открыть страницу: {0}")
    public void open(String urlKey) {
        driver.get(getRequired(urlKey));
    }

    protected WebElement awaitVisibility(WebElement element) {
        return waitForVisibility(driver, element);
    }

    protected WebElement awaitClickable(WebElement element) {
        return waitForClickable(driver, element);
    }

    protected boolean awaitInvisibility(WebElement element) {
        return waitForInvisibility(driver, element);
    }

    protected boolean isElementDisplayed(WebElement element) {
        return isDisplayedSafe(element);
    }

    @Step("Нажать на элемент")
    protected void clickElement(WebElement element) {
        awaitClickable(element).click();
    }

    protected String getElementText(WebElement element) {
        return awaitVisibility(element).getText();
    }

    protected String getInputValue(WebElement element) {
        return awaitVisibility(element).getDomProperty("value");
    }

    @Step("Очистить поле и ввести значение: {value}")
    protected void fillInput(WebElement element, String value) {
        awaitVisibility(element).clear();
        element.sendKeys(value);
    }

    @Step("Выделить весь текст в поле")
    protected void selectInputText(WebElement element) {
        clickElement(element);
        element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
    }

    @Step("Нажать кнопку открытия и дождаться появления окна")
    protected void openPopup(WebElement trigger, WebElement popup) {
        clickElement(trigger);
        awaitVisibility(popup);
    }

    @Step("Нажать кнопку закрытия и дождаться исчезновения окна")
    protected void closePopup(WebElement closeButton, WebElement popup) {
        clickElement(closeButton);
        awaitInvisibility(popup);
    }

    @Step("Отправить нажатие клавиши Escape")
    protected void pressEscapeKey() {
        new Actions(driver)
                .sendKeys(Keys.ESCAPE)
                .perform();
    }
}