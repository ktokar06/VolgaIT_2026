package org.example.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

public class AdsPage extends BasePage {
    @FindBy(css = ".pum-overlay.auto_open")
    private WebElement adOverlay;

    @FindBy(css = ".pum-overlay.auto_open .pum-container.popmake")
    private WebElement adContainer;

    @FindBy(css = ".pum-overlay.auto_open .pum-title.popmake-title")
    private WebElement adTitle;

    @FindBy(css = ".pum-overlay.auto_open .pum-content.popmake-content")
    private WebElement adContent;

    @FindBy(css = ".pum-overlay.auto_open .pum-close.popmake-close")
    private WebElement adCloseButton;

    @FindBy(xpath = "//h1[normalize-space()='Ads']")
    private WebElement pageTitle;

    public AdsPage(WebDriver driver) {
        super(driver);
    }

    @Step("Дождаться появления рекламного окна")
    public AdsPage waitForAdPopup() {
        awaitVisibility(adContainer);
        return this;
    }

    @Step("Закрыть рекламное окно")
    public AdsPage closeAdPopup() {
        closePopup(adCloseButton, adContainer);
        return this;
    }

    @Step("Нажать Escape")
    public AdsPage pressEscape() {
        pressEscapeKey();
        return this;
    }

    @Step("Нажать на overlay рекламного окна")
    public AdsPage clickOverlay() {
        new Actions(driver)
                .moveToElement(adOverlay)
                .click()
                .perform();
        return this;
    }

    @Step("Нажать на содержимое рекламного окна")
    public AdsPage clickAdContent() {
        clickElement(adContent);
        return this;
    }

    @Step("Получить заголовок страницы")
    public String getPageTitle() {
        return getElementText(pageTitle);
    }

    @Step("Получить заголовок рекламного окна")
    public String getAdPopupTitle() {
        return getElementText(adTitle);
    }

    @Step("Получить текст рекламного окна")
    public String getAdPopupText() {
        return getElementText(adContent);
    }

    @Step("Получить aria-label кнопки закрытия")
    public String getCloseButtonAriaLabel() {
        return awaitVisibility(adCloseButton).getDomAttribute("aria-label");
    }

    @Step("Проверить отображение рекламного окна")
    public boolean isAdPopupDisplayed() {
        return isElementDisplayed(adContainer);
    }

    @Step("Проверить отображение кнопки закрытия")
    public boolean isCloseButtonDisplayed() {
        return isElementDisplayed(adCloseButton);
    }
}