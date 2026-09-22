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
@Feature("Quality")
public class CalendarsQualityTest extends BaseTest {
    private static final String VALID_DATE = "2026-10-15";
    private static final String CHANGED_DATE = "2026-11-20";
    private static final String LEAP_YEAR_DATE = "2028-02-29";

    @Test(description = "Корректная дата вводится в поле")
    @Story("Ввод даты")
    @Severity(SeverityLevel.NORMAL)
    public void testValidDateCanBeEntered() {
        driver.get(ParameterProvider.get("calendars.url"));

        CalendarPage page = new CalendarPage(driver);
        page.enterDate(VALID_DATE);

        Assert.assertEquals(page.getDateValue(), VALID_DATE, "Корректная дата не была установлена");
    }

    @Test(description = "Другая корректная дата заменяет предыдущую")
    @Story("Изменение даты")
    @Severity(SeverityLevel.NORMAL)
    public void testDateCanBeChanged() {
        driver.get(ParameterProvider.get("calendars.url"));

        CalendarPage page = new CalendarPage(driver);
        page.enterDate(VALID_DATE)
                .enterDate(CHANGED_DATE);

        Assert.assertEquals(page.getDateValue(), CHANGED_DATE, "Дата не была изменена");
    }

    @Test(description = "Дата текущего года принимается")
    @Story("Работа с датой")
    @Severity(SeverityLevel.NORMAL)
    public void testCurrentYearDateCanBeEntered() {
        driver.get(ParameterProvider.get("calendars.url"));

        CalendarPage page = new CalendarPage(driver);
        String date = page.getCurrentYearDate();
        page.enterDate(date);

        Assert.assertEquals(page.getDateValue(), date, "Дата текущего года не была установлена");
    }

    @Test(description = "Високосная дата принимается")
    @Story("Граничные даты")
    @Severity(SeverityLevel.NORMAL)
    public void testLeapYearDateCanBeEntered() {
        driver.get(ParameterProvider.get("calendars.url"));

        CalendarPage page = new CalendarPage(driver);
        page.enterDate(LEAP_YEAR_DATE);

        Assert.assertEquals(page.getDateValue(), LEAP_YEAR_DATE, "Корректная високосная дата не была установлена");
    }
}