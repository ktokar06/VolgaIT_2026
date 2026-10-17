package org.example.tests.calendars;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.enums.PageInfo;
import org.example.enums.TestData;
import org.example.pages.CalendarPage;
import org.example.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Calendars")
@Feature("Regression")
public class CalendarsRegressionTest extends BaseTest {
    @Test(description = "Поле даты пустое при открытии страницы")
    @Story("Начальное состояние")
    @Severity(SeverityLevel.NORMAL)
    public void testDateFieldIsEmptyInitially() {
        CalendarPage page = new CalendarPage(getDriver());
        page.open(PageInfo.CALENDARS.urlKey());

        Assert.assertTrue(page.getDateValue().isEmpty(), "Поле даты не пустое при открытии страницы");
    }

    @Test(description = "После очистки дату можно ввести заново")
    @Story("Повторный ввод")
    @Severity(SeverityLevel.NORMAL)
    public void testDateCanBeReenteredAfterClearing() {
        CalendarPage page = new CalendarPage(getDriver());
        page.open(PageInfo.CALENDARS.urlKey());
        page.enterDate(TestData.VALID_DATE.value())
                .clearDate()
                .enterDate(TestData.VALID_DATE.value());

        Assert.assertEquals(page.getDateValue(), TestData.VALID_DATE.value(), "Дата не была установлена после очистки");
    }

    @Test(description = "Корректная дата успешно отправляется")
    @Story("Отправка даты")
    @Severity(SeverityLevel.NORMAL)
    public void testValidDateCanBeSubmitted() {
        CalendarPage page = new CalendarPage(getDriver());
        page.open(PageInfo.CALENDARS.urlKey());
        page.enterDate(TestData.VALID_DATE.value())
                .submitDate();

        Assert.assertEquals(page.getSuccessMessage(), TestData.FORM_SUCCESS_MESSAGE.value(), "Сообщение об успешной отправке не отображается");
    }
}