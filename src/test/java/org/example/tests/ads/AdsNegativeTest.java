package org.example.tests.ads;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.dataprovider.PageInfo;
import org.example.pages.AdsPage;
import org.example.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Ads")
@Feature("Negative")
public class AdsNegativeTest extends BaseTest {
    @Test(description = "Рекламное окно не закрывается клавишей Escape")
    @Story("Некорректный способ закрытия рекламного окна")
    @Severity(SeverityLevel.MINOR)
    public void testAdPopupNotClosedByEscape() {
        AdsPage page = new AdsPage(getDriver());
        page.open(PageInfo.ADS.urlKey());
        page.waitForAdPopup()
                .pressEscape();

        Assert.assertTrue(page.isAdPopupDisplayed(), "Рекламное окно закрылось клавишей Escape");
    }

    @Test(description = "Рекламное окно не закрывается кликом по overlay")
    @Story("Некорректный способ закрытия рекламного окна")
    @Severity(SeverityLevel.MINOR)
    public void testAdPopupNotClosedByOverlayClick() {
        AdsPage page = new AdsPage(getDriver());
        page.open(PageInfo.ADS.urlKey());
        page.waitForAdPopup()
                .clickOverlay();

        Assert.assertTrue(page.isAdPopupDisplayed(), "Рекламное окно закрылось кликом по overlay");
    }

    @Test(description = "Рекламное окно не закрывается кликом по содержимому")
    @Story("Некорректный способ закрытия рекламного окна")
    @Severity(SeverityLevel.MINOR)
    public void testAdPopupNotClosedByContentClick() {
        AdsPage page = new AdsPage(getDriver());
        page.open(PageInfo.ADS.urlKey());
        page.waitForAdPopup()
                .clickAdContent();

        Assert.assertTrue(page.isAdPopupDisplayed(), "Рекламное окно закрылось после клика по содержимому");
    }
}