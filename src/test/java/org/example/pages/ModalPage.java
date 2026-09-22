package org.example.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.interactions.Actions;
import org.example.utils.WaitUtils;

import static org.example.utils.WaitUtils.waitForClickable;
import static org.example.utils.WaitUtils.waitForVisibility;

public class ModalPage extends BasePage {
    @FindBy(css = "#simpleModal")
    private WebElement simpleModalButton;

    @FindBy(css = "#formModal")
    private WebElement formModalButton;

    @FindBy(css = "#popmake-1318")
    private WebElement simpleModal;

    @FindBy(css = "#popmake-674")
    private WebElement formModal;

    @FindBy(css = "#popmake-1318 .pum-title")
    private WebElement simpleModalTitle;

    @FindBy(css = "#popmake-1318 .pum-content")
    private WebElement simpleModalContent;

    @FindBy(css = "#popmake-1318 .pum-close")
    private WebElement simpleModalCloseButton;

    @FindBy(css = "#popmake-674 .pum-title")
    private WebElement formModalTitle;

    @FindBy(css = "#popmake-674 .pum-close")
    private WebElement formModalCloseButton;

    @FindBy(css = "#g1051-name")
    private WebElement nameInput;

    @FindBy(css = "#g1051-email")
    private WebElement emailInput;

    @FindBy(css = "#contact-form-comment-g1051-message")
    private WebElement messageInput;

    @FindBy(css = "#popmake-674 button.pushbutton-wide")
    private WebElement submitButton;

    @FindBy(css = "#popmake-674 .contact-form__error-message")
    private WebElement formErrorMessage;

    @FindBy(css = "#popmake-674 .contact-form-submission h4")
    private WebElement successMessage;

    @FindBy(css = "h1[itemprop='headline']")
    private WebElement pageTitle;

    public ModalPage(WebDriver driver) {
        super(driver);
    }

    @Step("Дождаться загрузки страницы Modals")
    public ModalPage waitForPageLoad() {
        waitForVisibility(driver, pageTitle);
        return this;
    }

    @Step("Получить заголовок страницы")
    public String getPageTitle() {
        return waitForVisibility(driver, pageTitle).getText();
    }

    @Step("Открыть простой modal")
    public ModalPage openSimpleModal() {
        waitForClickable(driver, simpleModalButton).click();
        waitForVisibility(driver, simpleModal);
        return this;
    }

    @Step("Открыть modal с формой")
    public ModalPage openFormModal() {
        waitForClickable(driver, formModalButton).click();
        waitForVisibility(driver, formModal);
        return this;
    }

    @Step("Закрыть простой modal")
    public ModalPage closeSimpleModal() {
        waitForClickable(driver, simpleModalCloseButton).click();
        WaitUtils.waitForInvisibility(driver, simpleModal);
        return this;
    }

    @Step("Закрыть modal с формой")
    public ModalPage closeFormModal() {
        waitForClickable(driver, formModalCloseButton).click();
        WaitUtils.waitForInvisibility(driver, formModal);
        return this;
    }

    @Step("Получить заголовок простого modal")
    public String getSimpleModalTitle() {
        return waitForVisibility(driver, simpleModalTitle).getText();
    }

    @Step("Получить содержимое простого modal")
    public String getSimpleModalText() {
        return waitForVisibility(driver, simpleModalContent).getText();
    }

    @Step("Получить заголовок modal с формой")
    public String getFormModalTitle() {
        return waitForVisibility(driver, formModalTitle).getText();
    }

    @Step("Проверить отображение простого modal")
    public boolean isSimpleModalDisplayed() {
        return simpleModal.isDisplayed();
    }

    @Step("Проверить отображение modal с формой")
    public boolean isFormModalDisplayed() {
        return formModal.isDisplayed();
    }

    @Step("Ввести имя: {0}")
    public ModalPage enterName(String name) {
        waitForVisibility(driver, nameInput)
                .clear();
        nameInput.sendKeys(name);
        return this;
    }

    @Step("Ввести email: {0}")
    public ModalPage enterEmail(String email) {
        waitForVisibility(driver, emailInput)
                .clear();
        emailInput.sendKeys(email);
        return this;
    }

    @Step("Ввести сообщение: {0}")
    public ModalPage enterMessage(String message) {
        waitForVisibility(driver, messageInput)
                .clear();
        messageInput.sendKeys(message);
        return this;
    }

    @Step("Отправить форму")
    public ModalPage submitForm() {
        waitForClickable(driver, submitButton).click();
        return this;
    }

    @Step("Получить сообщение об ошибке формы")
    public String getFormErrorMessage() {
        return waitForVisibility(driver, formErrorMessage).getText();
    }

    @Step("Получить сообщение об успешной отправке")
    public String getSuccessMessage() {
        return waitForVisibility(driver, successMessage).getText();
    }

    @Step("Нажать Escape")
    public ModalPage pressEscape() {
        new Actions(driver)
                .sendKeys(Keys.ESCAPE)
                .perform();
        return this;
    }
}