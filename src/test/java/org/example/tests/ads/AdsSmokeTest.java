package org.example.tests.ads;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.enums.PageInfo;
import org.example.pages.AdsPage;
import org.example.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Ads")
@Feature("Smoke")
public class AdsSmokeTest extends BaseTest {
    @Test(description = "Страница Ads открывается")
    @Story("Открытие страницы Ads")
    @Severity(SeverityLevel.CRITICAL)
    public void testAdsPageOpens() {
        AdsPage page = new AdsPage(getDriver());
        page.open(PageInfo.ADS.urlKey());

        Assert.assertTrue(getDriver().getCurrentUrl().contains(PageInfo.ADS.urlPart()), "URL не содержит /ads/");
        Assert.assertEquals(page.getPageTitle(), PageInfo.ADS.title(), "Заголовок страницы не равен Ads");
    }

    @Test(description = "Рекламное окно появляется автоматически")
    @Story("Рекламное окно")
    @Severity(SeverityLevel.CRITICAL)
    public void testAdPopupAppearsAutomatically() {
        AdsPage page = new AdsPage(getDriver());
        page.open(PageInfo.ADS.urlKey());
        page.waitForAdPopup();

        Assert.assertTrue(page.isAdPopupDisplayed(), "Рекламное окно не появилось");
    }

    @Test(description = "Рекламное окно закрывается кнопкой Close")
    @Story("Закрытие рекламного окна")
    @Severity(SeverityLevel.CRITICAL)
    public void testAdPopupCloses() {
        AdsPage page = new AdsPage(getDriver());
        page.open(PageInfo.ADS.urlKey());
        page.waitForAdPopup()
                .closeAdPopup();

        Assert.assertFalse(page.isAdPopupDisplayed(), "Рекламное окно не закрылось");
    }
}