package org.example.tests.calendars;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.dataprovider.CalendarDataProvider;
import org.example.enums.CalendarDate;
import org.example.enums.PageInfo;
import org.example.enums.TestData;
import org.example.pages.CalendarPage;
import org.example.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Calendars")
@Feature("Quality")
public class CalendarsQualityTest extends BaseTest {
    @Test(
            dataProvider = "validDates",
            dataProviderClass = CalendarDataProvider.class,
            description = "Корректная дата вводится в поле"
    )
    @Story("Ввод валидных дат")
    @Severity(SeverityLevel.NORMAL)
    public void testValidDateCanBeEntered(CalendarDate date) {
        CalendarPage page = new CalendarPage(getDriver());
        page.open(PageInfo.CALENDARS.urlKey());
        page.enterDate(date.value());

        Assert.assertEquals(page.getDateValue(), date.value(),
                "Корректная дата не была установлена: " + date.description());
    }

    @Test(description = "Другая корректная дата заменяет предыдущую")
    @Story("Изменение даты")
    @Severity(SeverityLevel.NORMAL)
    public void testDateCanBeChanged() {
        CalendarPage page = new CalendarPage(getDriver());
        page.open(PageInfo.CALENDARS.urlKey());
        page.enterDate(TestData.VALID_DATE.value())
                .enterDate(TestData.CHANGED_DATE.value());

        Assert.assertEquals(page.getDateValue(), TestData.CHANGED_DATE.value(), "Дата не была изменена");
    }
}