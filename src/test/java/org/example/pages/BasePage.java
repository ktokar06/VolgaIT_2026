package org.example.pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

import static org.example.utils.WaitUtils.isDisplayedSafe;
import static org.example.utils.WaitUtils.waitForInvisibility;
import static org.example.utils.WaitUtils.waitForClickable;
import static org.example.utils.WaitUtils.waitForVisibility;

public abstract class BasePage {
    protected final WebDriver driver;

    protected BasePage(WebDriver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("WebDriver не может быть null");
        }

        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    protected WebElement awaitVisibility(WebDriver driver, WebElement element) {
        return waitForVisibility(driver, element);
    }

    protected WebElement awaitClickable(WebDriver driver, WebElement element) {
        return waitForClickable(driver, element);
    }

    protected boolean awaitInvisibility(WebDriver driver, WebElement element) {
        return waitForInvisibility(driver, element);
    }

    protected boolean isElementDisplayed(WebElement element) {
        return isDisplayedSafe(element);
    }

    protected void pressEscapeKey() {
        new Actions(driver)
                .sendKeys(Keys.ESCAPE)
                .perform();
    }
}