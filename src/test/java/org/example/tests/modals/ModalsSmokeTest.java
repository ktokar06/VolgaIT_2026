package org.example.tests.modals;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.pages.ModalPage;
import org.example.tests.BaseTest;
import org.example.utils.ParameterProvider;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Modals")
@Feature("Smoke")
public class ModalsSmokeTest extends BaseTest {
    private static final String EXPECTED_PAGE_TITLE = "Modals";
    private static final String EXPECTED_URL_PART = "/modals/";

    @Test(description = "Страница Modals открывается")
    @Story("Открытие страницы Modals")
    @Severity(SeverityLevel.CRITICAL)
    public void testModalsPageOpens() {
        driver.get(ParameterProvider.getRequired("modals.url"));

        ModalPage page = new ModalPage(driver);

        Assert.assertTrue(driver.getCurrentUrl().contains(EXPECTED_URL_PART), "URL не содержит /modals/");
        Assert.assertEquals(page.getPageTitle(), EXPECTED_PAGE_TITLE, "Заголовок страницы не равен Modals");
    }

    @Test(description = "Простой modal открывается")
    @Story("Открытие простого modal")
    @Severity(SeverityLevel.CRITICAL)
    public void testSimpleModalOpens() {
        driver.get(ParameterProvider.getRequired("modals.url"));

        ModalPage page = new ModalPage(driver);
        page.openSimpleModal();

        Assert.assertTrue(page.isSimpleModalDisplayed(), "Простой modal не отображается");
    }

    @Test(description = "Modal с формой открывается")
    @Story("Открытие modal с формой")
    @Severity(SeverityLevel.CRITICAL)
    public void testFormModalOpens() {
        driver.get(ParameterProvider.getRequired("modals.url"));

        ModalPage page = new ModalPage(driver);
        page.openFormModal();

        Assert.assertTrue(page.isFormModalDisplayed(), "Modal с формой не отображается");
    }
}