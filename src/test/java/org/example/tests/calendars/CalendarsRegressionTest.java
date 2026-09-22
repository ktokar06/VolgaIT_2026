package org.example.tests.calendars;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.pages.CalendarPage;
import org.example.tests.BaseTest;
import org.example.utils.ParameterProvider;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Calendars")
@Feature("Regression")
public class CalendarsRegressionTest extends BaseTest {
    private static final String VALID_DATE = "2026-10-15";

    @Test(description = "Поле даты пустое при открытии страницы")
    @Story("Начальное состояние")
    @Severity(SeverityLevel.NORMAL)
    public void testDateFieldIsEmptyInitially() {
        driver.get(ParameterProvider.get("calendars.url"));

        CalendarPage page = new CalendarPage(driver);

        Assert.assertTrue(page.getDateValue().isEmpty(), "Поле даты не пустое при открытии страницы");
    }

    @Test(description = "Поле даты можно очистить")
    @Story("Очистка даты")
    @Severity(SeverityLevel.MINOR)
    public void testDateFieldCanBeCleared() {
        driver.get(ParameterProvider.get("calendars.url"));

        CalendarPage page = new CalendarPage(driver);
        page.enterDate(VALID_DATE)
                .clearDate();

        Assert.assertTrue(page.getDateValue().isEmpty(), "Поле даты не очистилось");
    }

    @Test(description = "После очистки поле даты остаётся пустым")
    @Story("Очистка даты")
    @Severity(SeverityLevel.MINOR)
    public void testDateFieldRemainsEmptyAfterClearing() {
        driver.get(ParameterProvider.get("calendars.url"));

        CalendarPage page = new CalendarPage(driver);
        page.enterDate(VALID_DATE)
                .clearDate();

        Assert.assertEquals(page.getDateValue(), "", "После очистки поле даты содержит значение");
    }

    @Test(description = "После повторного ввода даты устанавливается новое значение")
    @Story("Повторный ввод")
    @Severity(SeverityLevel.NORMAL)
    public void testDateCanBeReenteredAfterClearing() {
        driver.get(ParameterProvider.get("calendars.url"));

        CalendarPage page = new CalendarPage(driver);
        page.enterDate(VALID_DATE)
                .clearDate()
                .enterDate(VALID_DATE);

        Assert.assertEquals(page.getDateValue(), VALID_DATE, "Дата не была установлена после очистки");
    }
}