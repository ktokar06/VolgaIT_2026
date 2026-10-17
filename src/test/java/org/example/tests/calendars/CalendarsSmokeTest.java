package org.example.tests.calendars;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.dataprovider.PageInfo;
import org.example.pages.CalendarPage;
import org.example.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Calendars")
@Feature("Smoke")
public class CalendarsSmokeTest extends BaseTest {
    @Test(description = "Страница Calendars открывается")
    @Story("Открытие страницы Calendars")
    @Severity(SeverityLevel.CRITICAL)
    public void testCalendarsPageOpens() {
        CalendarPage page = new CalendarPage(getDriver());
        page.open(PageInfo.CALENDARS.urlKey());

        Assert.assertTrue(getDriver().getCurrentUrl().contains(PageInfo.CALENDARS.urlPart()), "URL не содержит /calendars/");
        Assert.assertEquals(page.getPageTitle(), PageInfo.CALENDARS.title(), "Заголовок страницы не равен Calendars");
    }

    @Test(description = "Поле даты отображается")
    @Story("Поле даты")
    @Severity(SeverityLevel.CRITICAL)
    public void testDateFieldDisplayed() {
        CalendarPage page = new CalendarPage(getDriver());
        page.open(PageInfo.CALENDARS.urlKey());

        Assert.assertTrue(page.isDateFieldDisplayed(), "Поле даты не отображается");
    }
}