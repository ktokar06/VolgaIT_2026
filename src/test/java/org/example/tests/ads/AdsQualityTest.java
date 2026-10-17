package org.example.tests.ads;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.dataprovider.PageInfo;
import org.example.dataprovider.TestData;
import org.example.pages.AdsPage;
import org.example.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Ads")
@Feature("Quality")
public class AdsQualityTest extends BaseTest {
    @Test(description = "Заголовок рекламного окна равен Hi")
    @Story("Содержимое рекламного окна")
    @Severity(SeverityLevel.NORMAL)
    public void testAdPopupTitle() {
        AdsPage page = new AdsPage(getDriver());
        page.open(PageInfo.ADS.urlKey());
        page.waitForAdPopup();

        Assert.assertEquals(page.getAdPopupTitle(), TestData.ADS_POPUP_TITLE.value(), "Заголовок рекламного окна не равен Hi");
    }

    @Test(description = "Текст рекламного окна соответствует ожидаемому")
    @Story("Содержимое рекламного окна")
    @Severity(SeverityLevel.NORMAL)
    public void testAdPopupText() {
        AdsPage page = new AdsPage(getDriver());
        page.open(PageInfo.ADS.urlKey());
        page.waitForAdPopup();

        Assert.assertEquals(page.getAdPopupText(), TestData.ADS_POPUP_TEXT.value(), "Текст рекламного окна не совпадает");
    }

    @Test(description = "Кнопка закрытия имеет aria-label Close")
    @Story("Доступность рекламного окна")
    @Severity(SeverityLevel.NORMAL)
    public void testAdPopupCloseButtonAccessible() {
        AdsPage page = new AdsPage(getDriver());
        page.open(PageInfo.ADS.urlKey());
        page.waitForAdPopup();

        Assert.assertEquals(page.getCloseButtonAriaLabel(), TestData.CLOSE_ARIA_LABEL.value(), "Кнопка закрытия не имеет aria-label Close");
    }

    @Test(description = "Кнопка закрытия отображается")
    @Story("Рекламное окно")
    @Severity(SeverityLevel.NORMAL)
    public void testAdPopupCloseButtonDisplayed() {
        AdsPage page = new AdsPage(getDriver());
        page.open(PageInfo.ADS.urlKey());
        page.waitForAdPopup();

        Assert.assertTrue(page.isCloseButtonDisplayed(), "Кнопка закрытия не отображается");
    }
}