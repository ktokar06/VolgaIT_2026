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
@Feature("Regression")
public class AdsRegressionTest extends BaseTest {
    @Test(description = "Рекламное окно не отображается сразу после открытия")
    @Story("Задержка появления")
    @Severity(SeverityLevel.NORMAL)
    public void testAdPopupNotDisplayedImmediately() {
        AdsPage page = new AdsPage(getDriver());
        page.open(PageInfo.ADS.urlKey());

        Assert.assertFalse(page.isAdPopupDisplayed(), "Рекламное окно появилось сразу после открытия страницы");
    }
}