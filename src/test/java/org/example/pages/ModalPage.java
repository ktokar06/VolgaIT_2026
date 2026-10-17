package org.example.pages;

import io.qameta.allure.Step;
import org.example.enums.ModalType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ModalPage extends BasePage {
    @FindBy(id = "simpleModal")
    private WebElement simpleModalButton;

    @FindBy(id = "formModal")
    private WebElement formModalButton;

    @FindBy(css = ".pum-overlay[data-popmake*='simple-modal'] .pum-container.popmake")
    private WebElement simpleModal;

    @FindBy(css = ".pum-overlay[data-popmake*='sandbox-form-modal'] .pum-container.popmake")
    private WebElement formModal;

    @FindBy(css = ".pum-overlay[data-popmake*='simple-modal'] .pum-content.popmake-content")
    private WebElement simpleModalContent;

    @FindBy(css = ".pum-overlay[data-popmake*='simple-modal'] .pum-close.popmake-close")
    private WebElement simpleModalCloseButton;

    @FindBy(css = ".pum-overlay[data-popmake*='sandbox-form-modal'] .pum-close.popmake-close")
    private WebElement formModalCloseButton;

    @FindBy(css = ".pum-overlay[data-popmake*='sandbox-form-modal'] input[name$='-name']")
    private WebElement nameInput;

    @FindBy(css = ".pum-overlay[data-popmake*='sandbox-form-modal'] input[name$='-email']")
    private WebElement emailInput;

    @FindBy(css = ".pum-overlay[data-popmake*='sandbox-form-modal'] textarea[name$='-message']")
    private WebElement messageInput;

    @FindBy(css = ".pum-overlay[data-popmake*='sandbox-form-modal'] button[type='submit']")
    private WebElement submitButton;

    @FindBy(css = ".pum-overlay[data-popmake*='sandbox-form-modal'] .contact-form-submission h4")
    private WebElement successMessage;

    @FindBy(css = "h1")
    private WebElement pageTitle;

    public ModalPage(WebDriver driver) {
        super(driver);
    }

    @Step("Получить заголовок страницы")
    public String getPageTitle() {
        return getElementText(pageTitle);
    }

    @Step("Открыть простой modal")
    public ModalPage openSimpleModal() {
        openPopup(simpleModalButton, simpleModal);
        return this;
    }

    @Step("Открыть modal с формой")
    public ModalPage openFormModal() {
        openPopup(formModalButton, formModal);
        return this;
    }

    @Step("Закрыть простой modal")
    public ModalPage closeSimpleModal() {
        closePopup(simpleModalCloseButton, simpleModal);
        return this;
    }

    @Step("Закрыть modal с формой")
    public ModalPage closeFormModal() {
        closePopup(formModalCloseButton, formModal);
        return this;
    }

    @Step("Получить содержимое простого modal")
    public String getSimpleModalText() {
        return getElementText(simpleModalContent);
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
        fillInput(nameInput, name);
        return this;
    }

    @Step("Ввести email: {0}")
    public ModalPage enterEmail(String email) {
        fillInput(emailInput, email);
        return this;
    }

    @Step("Ввести сообщение: {0}")
    public ModalPage enterMessage(String message) {
        fillInput(messageInput, message);
        return this;
    }

    @Step("Получить значение имени")
    public String getNameValue() {
        return getInputValue(nameInput);
    }

    @Step("Получить значение email")
    public String getEmailValue() {
        return getInputValue(emailInput);
    }

    @Step("Получить значение сообщения")
    public String getMessageValue() {
        return getInputValue(messageInput);
    }

    @Step("Отправить форму")
    public ModalPage submitForm() {
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