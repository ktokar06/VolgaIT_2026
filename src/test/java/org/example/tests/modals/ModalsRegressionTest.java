package org.example.tests.modals;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.pages.FormFieldsPage;
import org.example.pages.ModalPage;
import org.example.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.example.utils.ParameterProvider.getRequired;

@Epic("Modals")
@Feature("Regression")
public class ModalsRegressionTest extends BaseTest {
    private static final String NAME = "Kirill";
    private static final String EMAIL = "kirill@example.com";
    private static final String EXPECTED_SUCCESS_MESSAGE = "Thank you for your response. ✨";

    private String getAutomationToolsMessage() {
        driver.get(getRequired("form-fields.url"));

        FormFieldsPage page = new FormFieldsPage(driver);
        return page.getAutomationToolsAsText();
    }

    @Test(description = "Modal с формой отправляется с заполненными данными")
    @Story("Отправка формы")
    @Severity(SeverityLevel.NORMAL)
    public void testFormSubmission() {
        String message = getAutomationToolsMessage();
        driver.get(getRequired("modals.url"));

        ModalPage page = new ModalPage(driver);
        page.openFormModal()
                .enterName(NAME)
                .enterEmail(EMAIL)
                .enterMessage(message);

        Assert.assertEquals(page.getMessageValue(), message, "Поле Message не содержит Automation Tools");

        page.submitForm();

        Assert.assertEquals(page.getSuccessMessage(), EXPECTED_SUCCESS_MESSAGE, "Сообщение об успешной отправке не отображается");
    }

    @Test(description = "Modal с формой закрывается кнопкой")
    @Story("Закрытие modal")
    @Severity(SeverityLevel.MINOR)
    public void testFormModalCloses() {
        driver.get(getRequired("modals.url"));

        ModalPage page = new ModalPage(driver);
        page.openFormModal()
                .closeFormModal();

        Assert.assertFalse(page.isFormModalDisplayed(), "Modal с формой не закрылся");
    }
}