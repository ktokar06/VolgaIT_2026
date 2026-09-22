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
@Feature("Smoke")
public class CalendarsSmokeTest extends BaseTest {
    private static final String EXPECTED_PAGE_TITLE = "Calendars";
    private static final String EXPECTED_URL_PART = "/calendars/";

    @Test(description = "Страница Calendars открывается")
    @Story("Открытие страницы Calendars")
    @Severity(SeverityLevel.CRITICAL)
    public void testCalendarsPageOpens() {
        driver.get(ParameterProvider.get("calendars.url"));

        CalendarPage page = new CalendarPage(driver);

        Assert.assertTrue(driver.getCurrentUrl().contains(EXPECTED_URL_PART), "URL не содержит /calendars/");
        Assert.assertEquals(page.getPageTitle(), EXPECTED_PAGE_TITLE, "Заголовок страницы не равен Calendars");
    }

    @Test(description = "Поле даты отображается")
    @Story("Поле даты")
    @Severity(SeverityLevel.CRITICAL)
    public void testDateFieldDisplayed() {
        driver.get(ParameterProvider.get("calendars.url"));

        CalendarPage page = new CalendarPage(driver);

        Assert.assertTrue(page.isDateFieldDisplayed(), "Поле даты не отображается");
    }

    @Test(description = "Кнопка Submit отображается")
    @Story("Форма календаря")
    @Severity(SeverityLevel.NORMAL)
    public void testSubmitButtonDisplayed() {
        driver.get(ParameterProvider.get("calendars.url"));

        CalendarPage page = new CalendarPage(driver);

        Assert.assertTrue(page.isSubmitButtonDisplayed(), "Кнопка Submit не отображается");
    }
}