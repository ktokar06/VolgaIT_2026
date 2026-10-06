package org.example.tests.modals;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.pages.ModalPage;
import org.example.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.example.utils.ParameterProvider.getRequired;

@Epic("Modals")
@Feature("Quality")
public class ModalsQualityTest extends BaseTest {
    private static final String EXPECTED_SIMPLE_MODAL_TEXT = "Hi, I’m a simple modal.";
    private static final String NAME = "Kirill";
    private static final String EMAIL = "kirill@example.com";
    private static final String MESSAGE = "Automation test";

    @Test(description = "Простой modal содержит ожидаемый текст")
    @Story("Содержимое простого modal")
    @Severity(SeverityLevel.NORMAL)
    public void testSimpleModalText() {
        driver.get(getRequired("modals.url"));

        ModalPage page = new ModalPage(driver);
        page.openSimpleModal();

        Assert.assertEquals(page.getSimpleModalText(), EXPECTED_SIMPLE_MODAL_TEXT, "Текст простого modal не совпадает");
    }

    @Test(description = "Простой modal закрывается кнопкой")
    @Story("Закрытие простого modal")
    @Severity(SeverityLevel.NORMAL)
    public void testSimpleModalCloses() {
        driver.get(getRequired("modals.url"));

        ModalPage page = new ModalPage(driver);
        page.openSimpleModal()
                .closeSimpleModal();

        Assert.assertFalse(page.isSimpleModalDisplayed(), "Простой modal не закрылся");
    }

    @Test(description = "Форма modal принимает имя")
    @Story("Форма modal")
    @Severity(SeverityLevel.NORMAL)
    public void testFormAcceptsName() {
        driver.get(getRequired("modals.url"));

        ModalPage page = new ModalPage(driver);
        page.openFormModal()
                .enterName(NAME);

        Assert.assertEquals(page.getNameValue(), NAME, "Имя не было установлено в поле");
    }

    @Test(description = "Форма modal принимает email")
    @Story("Форма modal")
    @Severity(SeverityLevel.NORMAL)
    public void testFormAcceptsEmail() {
        driver.get(getRequired("modals.url"));

        ModalPage page = new ModalPage(driver);
        page.openFormModal()
                .enterEmail(EMAIL);

        Assert.assertEquals(page.getEmailValue(), EMAIL, "Email не был установлен в поле");
    }

    @Test(description = "Форма modal принимает сообщение")
    @Story("Форма modal")
    @Severity(SeverityLevel.NORMAL)
    public void testFormAcceptsMessage() {
        driver.get(getRequired("modals.url"));

        ModalPage page = new ModalPage(driver);
        page.openFormModal()
                .enterMessage(MESSAGE);

        Assert.assertEquals(page.getMessageValue(), MESSAGE, "Сообщение не было установлено в поле");
    }
}