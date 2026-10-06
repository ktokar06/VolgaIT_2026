package org.example.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CalendarPage extends BasePage {
    @FindBy(css = "h1[itemprop='headline']")
    private WebElement pageTitle;

    @FindBy(id = "g1065-1-selectorenteradate")
    private WebElement dateInput;

    @FindBy(css = "button[type='submit'].pushbutton-wide")
    private WebElement submitButton;

    @FindBy(css = "h4[id^='contact-form-success-header']")
    private WebElement successMessage;

    public CalendarPage(WebDriver driver) {
        super(driver);
    }

    @Step("Получить заголовок страницы")
    public String getPageTitle() {
        return awaitVisibility(driver, pageTitle).getText();
    }

    @Step("Проверить отображение поля даты")
    public boolean isDateFieldDisplayed() {
        return awaitVisibility(driver, dateInput).isDisplayed();
    }

    @Step("Получить значение даты")
    public String getDateValue() {
        return awaitVisibility(driver, dateInput).getDomProperty("value");
    }

    @Step("Ввести дату: {0}")
    public CalendarPage enterDate(String date) {
        awaitVisibility(driver, dateInput).click();
        dateInput.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        dateInput.sendKeys(date);
        return this;
    }

    @Step("Очистить поле даты")
    public CalendarPage clearDate() {
        awaitVisibility(driver, dateInput).click();
        dateInput.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        dateInput.sendKeys(Keys.BACK_SPACE);
        return this;
    }

    @Step("Нажать кнопку Submit")
    public CalendarPage submitDate() {
        awaitClickable(driver, submitButton).click();
        return this;
    }

    @Step("Получить сообщение об успешной отправке")
    public String getSuccessMessage() {
        return awaitVisibility(driver, successMessage).getText();
    }

    @Step("Проверить отображение сообщения об успешной отправке")
    public boolean isSuccessMessageDisplayed() {
        return isElementDisplayed(successMessage);
    }
}