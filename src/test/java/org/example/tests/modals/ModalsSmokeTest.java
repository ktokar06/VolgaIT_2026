package org.example.tests.modals;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.enums.PageInfo;
import org.example.pages.ModalPage;
import org.example.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Modals")
@Feature("Smoke")
public class ModalsSmokeTest extends BaseTest {
    @Test(description = "Страница Modals открывается")
    @Story("Открытие страницы Modals")
    @Severity(SeverityLevel.CRITICAL)
    public void testModalsPageOpens() {
        ModalPage page = new ModalPage(getDriver());
        page.open(PageInfo.MODALS.urlKey());

        Assert.assertTrue(getDriver().getCurrentUrl().contains(PageInfo.MODALS.urlPart()), "URL не содержит /modals/");
        Assert.assertEquals(page.getPageTitle(), PageInfo.MODALS.title(), "Заголовок страницы не равен Modals");
    }

    @Test(description = "Простой modal открывается")
    @Story("Открытие простого modal")
    @Severity(SeverityLevel.CRITICAL)
    public void testSimpleModalOpens() {
        ModalPage page = new ModalPage(getDriver());
        page.open(PageInfo.MODALS.urlKey());
        page.openSimpleModal();

        Assert.assertTrue(page.isSimpleModalDisplayed(), "Простой modal не отображается");
    }

    @Test(description = "Modal с формой открывается")
    @Story("Открытие modal с формой")
    @Severity(SeverityLevel.CRITICAL)
    public void testFormModalOpens() {
        ModalPage page = new ModalPage(getDriver());
        page.open(PageInfo.MODALS.urlKey());
        page.openFormModal();

        Assert.assertTrue(page.isFormModalDisplayed(), "Modal с формой не отображается");
    }
}