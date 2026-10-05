package org.example.pages;

import org.example.utils.WaitUtils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

public abstract class BasePage {
    protected final WebDriver driver;

    protected BasePage(WebDriver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("WebDriver не может быть null");
        }

        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    protected WebElement waitForVisibility(WebDriver driver, WebElement element) {
        return WaitUtils.waitForVisibility(driver, element);
    }

    protected WebElement waitForClickable(WebDriver driver, WebElement element) {
        return WaitUtils.waitForClickable(driver, element);
    }

    protected boolean waitForInvisibility(WebDriver driver, WebElement element) {
        return WaitUtils.waitForInvisibility(driver, element);
    }

    protected boolean isDisplayedSafeElement(WebElement element) {
        return WaitUtils.isDisplayedSafe(element);
    }

    protected void pressEscapeKey() {
        new Actions(driver)
                .sendKeys(Keys.ESCAPE)
                .perform();
    }
}