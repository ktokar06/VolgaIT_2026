package org.example.tests.ads;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.pages.AdsPage;
import org.example.tests.BaseTest;
import org.example.utils.ParameterProvider;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Ads")
@Feature("Regression")
public class AdsRegressionTest extends BaseTest {

    @Test(description = "Рекламное окно не отображается сразу после открытия")
    @Story("Задержка появления")
    @Severity(SeverityLevel.NORMAL)
    public void testAdPopupNotDisplayedImmediately() {
        driver.get(ParameterProvider.get("ads.url"));

        AdsPage page = new AdsPage(driver);

        Assert.assertFalse(page.isAdPopupDisplayed(), "Рекламное окно появилось сразу после открытия страницы");
    }

    @Test(description = "Рекламное окно не закрывается клавишей Escape")
    @Story("Способы закрытия")
    @Severity(SeverityLevel.MINOR)
    public void testAdPopupNotClosedByEscape() {
        driver.get(ParameterProvider.get("ads.url"));

        AdsPage page = new AdsPage(driver);
        page.waitForAdPopup()
                .pressEscape();

        Assert.assertTrue(page.isAdPopupDisplayed(), "Рекламное окно закрылось клавишей Escape");
    }

    @Test(description = "Рекламное окно не закрывается кликом по overlay")
    @Story("Способы закрытия")
    @Severity(SeverityLevel.MINOR)
    public void testAdPopupNotClosedByOverlayClick() {
        driver.get(ParameterProvider.get("ads.url"));

        AdsPage page = new AdsPage(driver);
        page.waitForAdPopup()
                .clickOverlay();

        Assert.assertTrue(page.isAdPopupDisplayed(), "Рекламное окно закрылось кликом по overlay");
    }
}