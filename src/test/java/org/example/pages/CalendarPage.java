package org.example.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.example.utils.WaitUtils.waitForVisibility;

public class CalendarPage extends BasePage {
    @FindBy(xpath = "//h1[normalize-space()='Calendars']")
    private WebElement pageTitle;

    @FindBy(xpath = "//label[contains(normalize-space(), 'Select or enter a date')]/following::input[1]")
    private WebElement dateInput;

    @FindBy(xpath = "//button[normalize-space()='Submit']")
    private WebElement submitButton;

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
        return waitForVisibility(driver, dateInput).getAttribute("value");
    }

    @Step("Ввести дату: {0}")
    public CalendarPage enterDate(String date) {
        waitForVisibility(driver, dateInput);

        dateInput.click();
        dateInput.sendKeys(Keys.CONTROL + "a");
        dateInput.sendKeys(date);

        return this;
    }

    @Step("Получить дату текущего года")
    public String getCurrentYearDate() {
        return java.time.LocalDate.now()
                .withMonth(12)
                .withDayOfMonth(15)
                .toString();
    }

    @Step("Очистить поле даты")
    public CalendarPage clearDate() {
        waitForVisibility(driver, dateInput);

        dateInput.click();
        dateInput.sendKeys(Keys.CONTROL + "a");
        dateInput.sendKeys(Keys.BACK_SPACE);

        return this;
    }

    @Step("Проверить отображение кнопки Submit")
    public boolean isSubmitButtonDisplayed() {
        return waitForVisibility(driver, submitButton).isDisplayed();
    }
}