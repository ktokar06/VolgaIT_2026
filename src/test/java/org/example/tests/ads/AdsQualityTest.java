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
@Feature("Quality")
public class AdsQualityTest extends BaseTest {
    private static final String EXPECTED_POPUP_TITLE = "Hi";
    private static final String EXPECTED_POPUP_TEXT = "I am an ad.";
    private static final String EXPECTED_CLOSE_BUTTON_ARIA_LABEL = "Close";

    @Test(description = "Заголовок рекламного окна равен Hi")
    @Story("Содержимое рекламного окна")
    @Severity(SeverityLevel.NORMAL)
    public void testAdPopupTitle() {
        driver.get(ParameterProvider.get("ads.url"));

        AdsPage page = new AdsPage(driver);
        page.waitForAdPopup();

        Assert.assertEquals(page.getAdPopupTitle(), EXPECTED_POPUP_TITLE, "Заголовок рекламного окна не равен Hi");
    }

    @Test(description = "Текст рекламного окна соответствует ожидаемому")
    @Story("Содержимое рекламного окна")
    @Severity(SeverityLevel.NORMAL)
    public void testAdPopupText() {
        driver.get(ParameterProvider.get("ads.url"));

        AdsPage page = new AdsPage(driver);
        page.waitForAdPopup();

        Assert.assertEquals(page.getAdPopupText(), EXPECTED_POPUP_TEXT, "Текст рекламного окна не совпадает");
    }

    @Test(description = "Кнопка закрытия имеет aria-label Close")
    @Story("Доступность рекламного окна")
    @Severity(SeverityLevel.NORMAL)
    public void testAdPopupCloseButtonAccessible() {
        driver.get(ParameterProvider.get("ads.url"));

        AdsPage page = new AdsPage(driver);
        page.waitForAdPopup();

        Assert.assertEquals(page.getCloseButtonAriaLabel(), EXPECTED_CLOSE_BUTTON_ARIA_LABEL, "Кнопка закрытия не имеет aria-label Close");
    }

    @Test(description = "Кнопка закрытия отображается")
    @Story("Рекламное окно")
    @Severity(SeverityLevel.NORMAL)
    public void testAdPopupCloseButtonDisplayed() {
        driver.get(ParameterProvider.get("ads.url"));

        AdsPage page = new AdsPage(driver);
        page.waitForAdPopup();

        Assert.assertTrue(page.isCloseButtonDisplayed(), "Кнопка закрытия не отображается");
    }
}