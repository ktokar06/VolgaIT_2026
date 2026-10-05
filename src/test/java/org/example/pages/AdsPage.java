package org.example.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

public class AdsPage extends BasePage {
    @FindBy(id = "pum-1272")
    private WebElement adOverlay;

    @FindBy(id = "popmake-1272")
    private WebElement adContainer;

    @FindBy(id = "pum_popup_title_1272")
    private WebElement adTitle;

    @FindBy(css = "#popmake-1272 .pum-content")
    private WebElement adContent;

    @FindBy(css = "#popmake-1272 .pum-close[aria-label='Close']")
    private WebElement adCloseButton;

    @FindBy(xpath = "//h1[normalize-space()='Ads']")
    private WebElement pageTitle;

    public AdsPage(WebDriver driver) {
        super(driver);
    }

    @Step("Дождаться появления рекламного окна")
    public AdsPage waitForAdPopup() {
        waitForVisibility(driver, adContainer);
        return this;
    }

    @Step("Закрыть рекламное окно")
    public AdsPage closeAdPopup() {
        waitForClickable(this.driver, adCloseButton).click();
        waitForInvisibility(driver, adContainer);
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
        waitForVisibility(driver, adContent).click();
        return this;
    }

    @Step("Получить заголовок страницы")
    public String getPageTitle() {
        return waitForVisibility(driver, pageTitle).getText();
    }

    @Step("Получить заголовок рекламного окна")
    public String getAdPopupTitle() {
        return waitForVisibility(driver, adTitle).getText();
    }

    @Step("Получить текст рекламного окна")
    public String getAdPopupText() {
        return waitForVisibility(driver, adContent).getText();
    }

    @Step("Получить aria-label кнопки закрытия")
    public String getCloseButtonAriaLabel() {
        return waitForVisibility(driver, adCloseButton).getAttribute("aria-label");
    }

    @Step("Проверить отображение рекламного окна")
    public boolean isAdPopupDisplayed() {
        return isDisplayedSafeElement(adContainer);
    }

    @Step("Проверить отображение кнопки закрытия")
    public boolean isCloseButtonDisplayed() {
        return isDisplayedSafeElement(adCloseButton);
    }
}