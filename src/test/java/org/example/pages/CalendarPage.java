package org.example.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CalendarPage extends BasePage {
    @FindBy(xpath = "//h1[normalize-space()='Calendars']")
    private WebElement pageTitle;

    @FindBy(xpath = "//label[contains(normalize-space(), 'Select or enter a date')]/following-sibling::input[1]")
    private WebElement dateInput;

    @FindBy(xpath = "//button[normalize-space()='Submit']")
    private WebElement submitButton;

    @FindBy(xpath = "//h4[contains(normalize-space(), 'Thank you for your response')]")
    private WebElement successMessage;

    public CalendarPage(WebDriver driver) {
        super(driver);
    }

    @Step("Получить заголовок страницы")
    public String getPageTitle() {
        return waitForVisibility(driver, pageTitle).getText();
    }

    @Step("Проверить отображение поля даты")
    public boolean isDateFieldDisplayed() {
        return waitForVisibility(driver, dateInput).isDisplayed();
    }

    @Step("Получить значение даты")
    public String getDateValue() {
        return waitForVisibility(driver, dateInput).getDomProperty("value");
    }

    @Step("Ввести дату: {0}")
    public CalendarPage enterDate(String date) {
        waitForVisibility(driver, dateInput).click();
        dateInput.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        dateInput.sendKeys(date);
        return this;
    }

    @Step("Очистить поле даты")
    public CalendarPage clearDate() {
        waitForVisibility(driver, dateInput).click();
        dateInput.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        dateInput.sendKeys(Keys.BACK_SPACE);
        return this;
    }

    @Step("Нажать кнопку Submit")
    public CalendarPage submitDate() {
        waitForClickable(this.driver, submitButton).click();
        return this;
    }

    @Step("Получить сообщение об успешной отправке")
    public String getSuccessMessage() {
        return waitForVisibility(driver, successMessage).getText();
    }

    @Step("Проверить отображение сообщения об успешной отправке")
    public boolean isSuccessMessageDisplayed() {
        return isDisplayedSafeElement(successMessage);
    }
}