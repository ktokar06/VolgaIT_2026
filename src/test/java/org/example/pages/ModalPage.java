package org.example.pages;

import io.qameta.allure.Step;
import org.example.dataprovider.ModalType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ModalPage extends BasePage {
    @FindBy(id = "simpleModal")
    private WebElement simpleModalButton;

    @FindBy(id = "formModal")
    private WebElement formModalButton;

    @FindBy(id = "popmake-1318")
    private WebElement simpleModal;

    @FindBy(id = "popmake-674")
    private WebElement formModal;

    @FindBy(css = "#popmake-1318 .pum-content")
    private WebElement simpleModalContent;

    @FindBy(css = "#popmake-1318 .pum-close")
    private WebElement simpleModalCloseButton;

    @FindBy(css = "#popmake-674 .pum-close")
    private WebElement formModalCloseButton;

    @FindBy(id = "g1051-name")
    private WebElement nameInput;

    @FindBy(id = "g1051-email")
    private WebElement emailInput;

    @FindBy(id = "contact-form-comment-g1051-message")
    private WebElement messageInput;

    @FindBy(css = "#popmake-674 button.pushbutton-wide")
    private WebElement submitButton;

    @FindBy(css = "#popmake-674 .contact-form-submission h4")
    private WebElement successMessage;

    @FindBy(css = "h1")
    private WebElement pageTitle;

    public ModalPage(WebDriver driver) {
        super(driver);
    }

    @Step("Получить заголовок страницы")
    public String getPageTitle() {
        return awaitVisibility(driver, pageTitle).getText();
    }

    @Step("Открыть простой modal")
    public ModalPage openSimpleModal() {
        awaitClickable(driver, simpleModalButton).click();
        awaitVisibility(driver, simpleModal);
        return this;
    }

    @Step("Открыть modal с формой")
    public ModalPage openFormModal() {
        awaitClickable(driver, formModalButton).click();
        awaitVisibility(driver, formModal);
        return this;
    }

    @Step("Закрыть простой modal")
    public ModalPage closeSimpleModal() {
        awaitClickable(driver, simpleModalCloseButton).click();
        awaitInvisibility(driver, simpleModal);
        return this;
    }

    @Step("Закрыть modal с формой")
    public ModalPage closeFormModal() {
        awaitClickable(driver, formModalCloseButton).click();
        awaitInvisibility(driver, formModal);
        return this;
    }

    @Step("Получить содержимое простого modal")
    public String getSimpleModalText() {
        return awaitVisibility(driver, simpleModalContent).getText();
    }

    @Step("Проверить отображение простого modal")
    public boolean isSimpleModalDisplayed() {
        return isElementDisplayed(simpleModal);
    }

    @Step("Проверить отображение modal с формой")
    public boolean isFormModalDisplayed() {
        return isElementDisplayed(formModal);
    }

    @Step("Ввести имя: {0}")
    public ModalPage enterName(String name) {
        awaitVisibility(driver, nameInput).clear();
        nameInput.sendKeys(name);
        return this;
    }

    @Step("Ввести email: {0}")
    public ModalPage enterEmail(String email) {
        awaitVisibility(driver, emailInput).clear();
        emailInput.sendKeys(email);
        return this;
    }

    @Step("Ввести сообщение: {0}")
    public ModalPage enterMessage(String message) {
        awaitVisibility(driver, messageInput).clear();
        messageInput.sendKeys(message);
        return this;
    }

    @Step("Получить значение имени")
    public String getNameValue() {
        return awaitVisibility(driver, nameInput).getDomProperty("value");
    }

    @Step("Получить значение email")
    public String getEmailValue() {
        return awaitVisibility(driver, emailInput).getDomProperty("value");
    }

    @Step("Получить значение сообщения")
    public String getMessageValue() {
        return awaitVisibility(driver, messageInput).getDomProperty("value");
    }

    @Step("Отправить форму")
    public ModalPage submitForm() {
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

    @Step("Нажать Escape")
    public ModalPage pressEscape() {
        pressEscapeKey();
        return this;
    }

    @Step("Открыть modal: {0}")
    public ModalPage openModal(ModalType modalType) {
        if (modalType == null) {
            throw new IllegalArgumentException("Тип modal не указан");
        }

        return switch (modalType) {
            case SIMPLE -> openSimpleModal();
            case FORM -> openFormModal();
        };
    }

    @Step("Проверить отображение modal: {0}")
    public boolean isModalDisplayed(ModalType modalType) {
        if (modalType == null) {
            throw new IllegalArgumentException("Тип modal не указан");
        }

        return switch (modalType) {
            case SIMPLE -> isSimpleModalDisplayed();
            case FORM -> isFormModalDisplayed();
        };
    }
}