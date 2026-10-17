package org.example.tests.modals;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.dataprovider.InvalidFormData;
import org.example.dataprovider.ModalDataProvider;
import org.example.dataprovider.ModalType;
import org.example.dataprovider.PageInfo;
import org.example.pages.ModalPage;
import org.example.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Modals")
@Feature("Negative")
public class ModalsNegativeTest extends BaseTest {
    @Test(dataProvider = "invalidFormData",
            dataProviderClass = ModalDataProvider.class,
            description = "Известный баг: форма отправляется с невалидными данными")
    @Story("Известный баг: нет валидации полей формы")
    @Severity(SeverityLevel.CRITICAL)
    public void testFormSubmittedWithInvalidData(InvalidFormData testData) {
        ModalPage page = new ModalPage(getDriver());
        page.open(PageInfo.MODALS.urlKey());
        page.openFormModal()
                .enterName(testData.userName())
                .enterEmail(testData.email())
                .enterMessage(testData.message())
                .submitForm();

        Assert.assertTrue(page.isSuccessMessageDisplayed(),
                "Поведение изменилось: " + testData.expectationIfFixed());
    }

    @Test(dataProvider = "modalTypes",
            dataProviderClass = ModalDataProvider.class,
            description = "Modal не закрывается клавишей Escape")
    @Story("Некорректный способ закрытия modal")
    @Severity(SeverityLevel.MINOR)
    public void testModalNotClosedByEscape(ModalType modalType) {
        ModalPage page = new ModalPage(getDriver());
        page.open(PageInfo.MODALS.urlKey());
        page.openModal(modalType)
                .pressEscape();

        Assert.assertTrue(page.isModalDisplayed(modalType), "Modal закрылся клавишей Escape: " + modalType);
    }
}