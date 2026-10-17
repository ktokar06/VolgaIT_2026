package org.example.tests.modals;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.enums.PageInfo;
import org.example.enums.TestData;
import org.example.pages.FormFieldsPage;
import org.example.pages.ModalPage;
import org.example.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Modals")
@Feature("Regression")
public class ModalsRegressionTest extends BaseTest {
    private String getAutomationToolsMessage() {
        FormFieldsPage page = new FormFieldsPage(getDriver());
        page.open("form-fields.url");
        return page.getAutomationToolsAsText();
    }

    @Test(description = "Modal с формой отправляется с заполненными данными")
    @Story("Отправка формы")
    @Severity(SeverityLevel.NORMAL)
    public void testFormSubmission() {
        String message = getAutomationToolsMessage();
        ModalPage page = new ModalPage(getDriver());
        page.open(PageInfo.MODALS.urlKey());
        page.openFormModal()
                .enterName(TestData.USER_NAME.value())
                .enterEmail(TestData.USER_EMAIL.value())
                .enterMessage(message);

        Assert.assertEquals(page.getMessageValue(), message, "Поле Message не содержит Automation Tools");

        page.submitForm();

        Assert.assertEquals(page.getSuccessMessage(), TestData.FORM_SUCCESS_MESSAGE.value(), "Сообщение об успешной отправке не отображается");
    }

    @Test(description = "Modal с формой закрывается кнопкой")
    @Story("Закрытие modal")
    @Severity(SeverityLevel.MINOR)
    public void testFormModalCloses() {
        ModalPage page = new ModalPage(getDriver());
        page.open(PageInfo.MODALS.urlKey());
        page.openFormModal()
                .closeFormModal();

        Assert.assertFalse(page.isFormModalDisplayed(), "Modal с формой не закрылся");
    }
}