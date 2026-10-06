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
@Feature("Negative")
public class CalendarsNegativeTest extends BaseTest {
    private static final String OUT_OF_RANGE_DATE = "1900-01-01";

    @Test(dataProvider = "invalidDates",
            dataProviderClass = CalendarDataProvider.class,
            description = "Известный баг: поле принимает невалидный формат")
    @Story("Известный баг: нет валидации формата даты")
    @Severity(SeverityLevel.NORMAL)
    public void testInvalidDateFormatAcceptedByField(String invalidDate) {
        driver.get(getRequired("calendars.url"));

        CalendarPage page = new CalendarPage(driver);
        page.enterDate(invalidDate);

        Assert.assertEquals(page.getDateValue(), invalidDate, "Поведение изменилось: поле больше не принимает невалидный формат: " + invalidDate);
    }

    @Test(description = "Невалидный формат не отправляет форму")
    @Story("Невалидный формат даты")
    @Severity(SeverityLevel.NORMAL)
    public void testInvalidDateDoesNotProduceSuccessMessage() {
        driver.get(getRequired("calendars.url"));

        CalendarPage page = new CalendarPage(driver);
        page.enterDate("15-10-2026").submitDate();

        Assert.assertFalse(page.isSuccessMessageDisplayed(), "Форма отправилась с невалидным форматом даты");
    }

    @Test(description = "Известный баг: поле принимает дату вне min")
    @Story("Известный баг: min не блокирует ввод")
    @Severity(SeverityLevel.MINOR)
    public void testOutOfRangeDateAcceptedByInputField() {
        driver.get(getRequired("calendars.url"));

        CalendarPage page = new CalendarPage(driver);
        page.enterDate(OUT_OF_RANGE_DATE);

        Assert.assertEquals(page.getDateValue(), OUT_OF_RANGE_DATE, "Поведение изменилось: поле больше не принимает дату вне min");
    }

    @Test(description = "Дата вне диапазона не отправляет форму")
    @Story("Ограничение диапазона дат")
    @Severity(SeverityLevel.NORMAL)
    public void testOutOfRangeDateBlocksSubmission() {
        driver.get(getRequired("calendars.url"));

        CalendarPage page = new CalendarPage(driver);
        page.enterDate(OUT_OF_RANGE_DATE)
                .submitDate();

        Assert.assertFalse(page.isSuccessMessageDisplayed(), "Форма отправилась с датой вне диапазона");
    }

    @Test(description = "Пустая дата не отправляется")
    @Story("Пустое значение даты")
    @Severity(SeverityLevel.NORMAL)
    public void testEmptyDateCannotBeSubmitted() {
        driver.get(getRequired("calendars.url"));

        CalendarPage page = new CalendarPage(driver);
        page.clearDate()
                .submitDate();

        Assert.assertFalse(page.isSuccessMessageDisplayed(), "Форма отправилась без даты");
    }
}