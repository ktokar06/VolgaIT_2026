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
@Feature("Smoke")
public class CalendarsSmokeTest extends BaseTest {
    private static final String EXPECTED_PAGE_TITLE = "Calendars";
    private static final String EXPECTED_URL_PART = "/calendars/";

    @Test(description = "Страница Calendars открывается")
    @Story("Открытие страницы Calendars")
    @Severity(SeverityLevel.CRITICAL)
    public void testCalendarsPageOpens() {
        driver.get(getRequired("calendars.url"));

        CalendarPage page = new CalendarPage(driver);

        Assert.assertTrue(driver.getCurrentUrl().contains(EXPECTED_URL_PART), "URL не содержит /calendars/");
        Assert.assertEquals(page.getPageTitle(), EXPECTED_PAGE_TITLE, "Заголовок страницы не равен Calendars");
    }

    @Test(description = "Поле даты отображается")
    @Story("Поле даты")
    @Severity(SeverityLevel.CRITICAL)
    public void testDateFieldDisplayed() {
        driver.get(getRequired("calendars.url"));

        CalendarPage page = new CalendarPage(driver);

        Assert.assertTrue(page.isDateFieldDisplayed(), "Поле даты не отображается");
    }
}