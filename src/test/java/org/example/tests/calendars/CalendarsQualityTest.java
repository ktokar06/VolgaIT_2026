package org.example.tests.calendars;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.dataprovider.CalendarDataProvider;
import org.example.pages.CalendarPage;
import org.example.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.example.utils.ParameterProvider.getRequired;

@Epic("Calendars")
@Feature("Quality")
public class CalendarsQualityTest extends BaseTest {
    private static final String VALID_DATE = "2026-10-15";
    private static final String CHANGED_DATE = "2026-11-20";

    @Test(
            dataProvider = "validDates",
            dataProviderClass = CalendarDataProvider.class,
            description = "Корректная дата вводится в поле"
    )
    @Story("Ввод валидных дат")
    @Severity(SeverityLevel.NORMAL)
    public void testValidDateCanBeEntered(String date) {
        driver.get(getRequired("calendars.url"));

        CalendarPage page = new CalendarPage(driver);
        page.enterDate(date);

        Assert.assertEquals(page.getDateValue(), date, "Корректная дата не была установлена: " + date);
    }

    @Test(description = "Другая корректная дата заменяет предыдущую")
    @Story("Изменение даты")
    @Severity(SeverityLevel.NORMAL)
    public void testDateCanBeChanged() {
        driver.get(getRequired("calendars.url"));

        CalendarPage page = new CalendarPage(driver);
        page.enterDate(VALID_DATE)
                .enterDate(CHANGED_DATE);

        Assert.assertEquals(page.getDateValue(), CHANGED_DATE, "Дата не была изменена");
    }
}