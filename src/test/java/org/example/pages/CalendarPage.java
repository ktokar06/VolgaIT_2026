package org.example.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CalendarPage extends BasePage {
    @FindBy(css = "h1[itemprop='headline']")
    private WebElement pageTitle;

    @FindBy(css = "input[name$='selectorenteradate']")
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
        return getElementText(pageTitle);
    }

    @Step("Проверить отображение поля даты")
    public boolean isDateFieldDisplayed() {
        return awaitVisibility(dateInput).isDisplayed();
    }

    @Step("Получить значение даты")
    public String getDateValue() {
        return getInputValue(dateInput);
    }

    @Step("Ввести дату: {0}")
    public CalendarPage enterDate(String date) {
        selectInputText(dateInput);
        dateInput.sendKeys(date);
        return this;
    }

    @Step("Очистить поле даты")
    public CalendarPage clearDate() {
        selectInputText(dateInput);
        dateInput.sendKeys(Keys.BACK_SPACE);
        return this;
    }

    @Step("Нажать кнопку Submit")
    public CalendarPage submitDate() {
        clickElement(submitButton);
        return this;
    }

    @Step("Получить сообщение об успешной отправке")
    public String getSuccessMessage() {
        return getElementText(successMessage);
    }

    @Step("Проверить отображение сообщения об успешной отправке")
    public boolean isSuccessMessageDisplayed() {
        return isElementDisplayed(successMessage);
    }
}