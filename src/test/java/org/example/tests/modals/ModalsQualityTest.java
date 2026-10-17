package org.example.tests.modals;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.dataprovider.FormField;
import org.example.dataprovider.ModalDataProvider;
import org.example.dataprovider.PageInfo;
import org.example.dataprovider.TestData;
import org.example.pages.ModalPage;
import org.example.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Modals")
@Feature("Quality")
public class ModalsQualityTest extends BaseTest {
    @Test(description = "Простой modal содержит ожидаемый текст")
    @Story("Содержимое простого modal")
    @Severity(SeverityLevel.NORMAL)
    public void testSimpleModalText() {
        ModalPage page = new ModalPage(getDriver());
        page.open(PageInfo.MODALS.urlKey());
        page.openSimpleModal();

        Assert.assertEquals(page.getSimpleModalText(), TestData.SIMPLE_MODAL_TEXT.value(), "Текст простого modal не совпадает");
    }

    @Test(description = "Простой modal закрывается кнопкой")
    @Story("Закрытие простого modal")
    @Severity(SeverityLevel.NORMAL)
    public void testSimpleModalCloses() {
        ModalPage page = new ModalPage(getDriver());
        page.open(PageInfo.MODALS.urlKey());
        page.openSimpleModal()
                .closeSimpleModal();

        Assert.assertFalse(page.isSimpleModalDisplayed(), "Простой modal не закрылся");
    }

    @Test(dataProvider = "formFields",
            dataProviderClass = ModalDataProvider.class,
            description = "Форма modal принимает значение поля")
    @Story("Форма modal")
    @Severity(SeverityLevel.NORMAL)
    public void testFormAcceptsFieldValue(FormField field) {
        ModalPage page = new ModalPage(getDriver());
        page.open(PageInfo.MODALS.urlKey());
        page.openFormModal();
        field.fill(page);

        Assert.assertEquals(field.actualValue(page), field.value(),
                "Значение не было установлено в поле: " + field);
    }
}