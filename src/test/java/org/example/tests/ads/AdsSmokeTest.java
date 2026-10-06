package org.example.tests.ads;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.pages.AdsPage;
import org.example.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.example.utils.ParameterProvider.getRequired;

@Epic("Ads")
@Feature("Smoke")
public class AdsSmokeTest extends BaseTest {
    private static final String EXPECTED_URL_PART = "/ads/";
    private static final String EXPECTED_PAGE_TITLE = "Ads";

    @Test(description = "Страница Ads открывается")
    @Story("Открытие страницы Ads")
    @Severity(SeverityLevel.CRITICAL)
    public void testAdsPageOpens() {
        driver.get(getRequired("ads.url"));

        AdsPage page = new AdsPage(driver);

        Assert.assertTrue(driver.getCurrentUrl().contains(EXPECTED_URL_PART), "URL не содержит /ads/");
        Assert.assertEquals(page.getPageTitle(), EXPECTED_PAGE_TITLE, "Заголовок страницы не равен Ads");
    }

    @Test(description = "Рекламное окно появляется автоматически")
    @Story("Рекламное окно")
    @Severity(SeverityLevel.CRITICAL)
    public void testAdPopupAppearsAutomatically() {
        driver.get(getRequired("ads.url"));

        AdsPage page = new AdsPage(driver);
        page.waitForAdPopup();

        Assert.assertTrue(page.isAdPopupDisplayed(), "Рекламное окно не появилось");
    }

    @Test(description = "Рекламное окно закрывается кнопкой Close")
    @Story("Закрытие рекламного окна")
    @Severity(SeverityLevel.CRITICAL)
    public void testAdPopupCloses() {
        driver.get(getRequired("ads.url"));

        AdsPage page = new AdsPage(driver);
        page.waitForAdPopup()
                .closeAdPopup();

        Assert.assertFalse(page.isAdPopupDisplayed(), "Рекламное окно не закрылось");
    }
}