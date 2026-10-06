package org.example.tests.modals;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.dataprovider.ModalDataProvider;
import org.example.dataprovider.ModalType;
import org.example.pages.ModalPage;
import org.example.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.example.utils.ParameterProvider.getRequired;

@Epic("Modals")
@Feature("Negative")
public class ModalsNegativeTest extends BaseTest {
    private static final String INVALID_EMAIL = "invalid-email";
    private static final String VALID_EMAIL = "kirill@example.com";
    private static final String VALID_NAME = "Kirill";
    private static final String MESSAGE = "Automation test";

    @Test(description = "Известный баг: форма отправляется без обязательного имени")
    @Story("Известный баг: нет валидации name")
    @Severity(SeverityLevel.CRITICAL)
    public void testEmptyNameSubmissionIsKnownBug() {
        driver.get(getRequired("modals.url"));

        ModalPage page = new ModalPage(driver);
        page.openFormModal()
                .enterName("")
                .enterEmail(VALID_EMAIL)
                .enterMessage(MESSAGE)
                .submitForm();

        Assert.assertTrue(page.isSuccessMessageDisplayed(), "Поведение изменилось: форма отклоняет пустое имя");
    }

    @Test(description = "Известный баг: форма отправляется с невалидным email")
    @Story("Известный баг: нет валидации email")
    @Severity(SeverityLevel.CRITICAL)
    public void testInvalidEmailSubmissionIsKnownBug() {
        driver.get(getRequired("modals.url"));

        ModalPage page = new ModalPage(driver);
        page.openFormModal()
                .enterName(VALID_NAME)
                .enterEmail(INVALID_EMAIL)
                .enterMessage(MESSAGE)
                .submitForm();

        Assert.assertTrue(page.isSuccessMessageDisplayed(), "Поведение изменилось: форма отклоняет невалидный email");
    }

    @Test(description = "Известный баг: форма отправляется без message")
    @Story("Известный баг: нет валидации message")
    @Severity(SeverityLevel.CRITICAL)
    public void testEmptyMessageSubmissionIsKnownBug() {
        driver.get(getRequired("modals.url"));

        ModalPage page = new ModalPage(driver);
        page.openFormModal()
                .enterName(VALID_NAME)
                .enterEmail(VALID_EMAIL)
                .enterMessage("")
                .submitForm();

        Assert.assertTrue(page.isSuccessMessageDisplayed(), "Поведение изменилось: форма отклоняет пустое сообщение");
    }

    @Test(dataProvider = "modalTypes",
            dataProviderClass = ModalDataProvider.class,
            description = "Modal не закрывается клавишей Escape")
    @Story("Некорректный способ закрытия modal")
    @Severity(SeverityLevel.MINOR)
    public void testModalNotClosedByEscape(ModalType modalType) {
        driver.get(getRequired("modals.url"));

        ModalPage page = new ModalPage(driver);
        page.openModal(modalType)
                .pressEscape();

        Assert.assertTrue(page.isModalDisplayed(modalType), "Modal закрылся клавишей Escape: " + modalType);
    }
}