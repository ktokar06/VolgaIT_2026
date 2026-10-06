package org.example.tests.calendars;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.pages.CalendarPage;
import org.example.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.example.utils.ParameterProvider.getRequired;

@Epic("Calendars")
@Feature("Regression")
public class CalendarsRegressionTest extends BaseTest {
    private static final String VALID_DATE = "2026-10-15";
    private static final String EXPECTED_SUCCESS_MESSAGE = "Thank you for your response. ✨";

    @Test(description = "Поле даты пустое при открытии страницы")
    @Story("Начальное состояние")
    @Severity(SeverityLevel.NORMAL)
    public void testDateFieldIsEmptyInitially() {
        driver.get(getRequired("calendars.url"));

        CalendarPage page = new CalendarPage(driver);

        Assert.assertTrue(page.getDateValue().isEmpty(), "Поле даты не пустое при открытии страницы");
    }

    @Test(description = "После очистки дату можно ввести заново")
    @Story("Повторный ввод")
    @Severity(SeverityLevel.NORMAL)
    public void testDateCanBeReenteredAfterClearing() {
        driver.get(getRequired("calendars.url"));

        CalendarPage page = new CalendarPage(driver);
        page.enterDate(VALID_DATE)
                .clearDate()
                .enterDate(VALID_DATE);

        Assert.assertEquals(page.getDateValue(), VALID_DATE, "Дата не была установлена после очистки");
    }

    @Test(description = "Корректная дата успешно отправляется")
    @Story("Отправка даты")
    @Severity(SeverityLevel.NORMAL)
    public void testValidDateCanBeSubmitted() {
        driver.get(getRequired("calendars.url"));

        CalendarPage page = new CalendarPage(driver);
        page.enterDate(VALID_DATE)
                .submitDate();

        Assert.assertEquals(page.getSuccessMessage(), EXPECTED_SUCCESS_MESSAGE, "Сообщение об успешной отправке не отображается");
    }
}