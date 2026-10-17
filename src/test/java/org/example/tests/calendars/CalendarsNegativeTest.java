package org.example.tests.calendars;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.dataprovider.CalendarDataProvider;
import org.example.dataprovider.CalendarDate;
import org.example.dataprovider.PageInfo;
import org.example.pages.CalendarPage;
import org.example.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Calendars")
@Feature("Negative")
public class CalendarsNegativeTest extends BaseTest {
    @Test(dataProvider = "invalidDates",
            dataProviderClass = CalendarDataProvider.class,
            description = "Известный баг: поле принимает невалидный формат")
    @Story("Известный баг: нет валидации формата даты")
    @Severity(SeverityLevel.NORMAL)
    public void testInvalidDateFormatAcceptedByField(CalendarDate invalidDate) {
        CalendarPage page = new CalendarPage(getDriver());
        page.open(PageInfo.CALENDARS.urlKey());
        page.enterDate(invalidDate.value());

        Assert.assertEquals(page.getDateValue(), invalidDate.value(),
                "Поведение изменилось: поле больше не принимает " + invalidDate.description());
    }

    @Test(description = "Невалидный формат не отправляет форму")
    @Story("Невалидный формат даты")
    @Severity(SeverityLevel.NORMAL)
    public void testInvalidDateDoesNotProduceSuccessMessage() {
        CalendarPage page = new CalendarPage(getDriver());
        page.open(PageInfo.CALENDARS.urlKey());
        page.enterDate(CalendarDate.WRONG_FORMAT.value()).submitDate();

        Assert.assertFalse(page.isSuccessMessageDisplayed(), "Форма отправилась с невалидным форматом даты");
    }

    @Test(description = "Известный баг: поле принимает дату вне min")
    @Story("Известный баг: min не блокирует ввод")
    @Severity(SeverityLevel.MINOR)
    public void testOutOfRangeDateAcceptedByInputField() {
        CalendarPage page = new CalendarPage(getDriver());
        page.open(PageInfo.CALENDARS.urlKey());
        page.enterDate(CalendarDate.OUT_OF_RANGE.value());

        Assert.assertEquals(page.getDateValue(), CalendarDate.OUT_OF_RANGE.value(),
                "Поведение изменилось: поле больше не принимает дату вне min");
    }

    @Test(description = "Дата вне диапазона не отправляет форму")
    @Story("Ограничение диапазона дат")
    @Severity(SeverityLevel.NORMAL)
    public void testOutOfRangeDateBlocksSubmission() {
        CalendarPage page = new CalendarPage(getDriver());
        page.open(PageInfo.CALENDARS.urlKey());
        page.enterDate(CalendarDate.OUT_OF_RANGE.value())
                .submitDate();

        Assert.assertFalse(page.isSuccessMessageDisplayed(), "Форма отправилась с датой вне диапазона");
    }

    @Test(description = "Пустая дата не отправляется")
    @Story("Пустое значение даты")
    @Severity(SeverityLevel.NORMAL)
    public void testEmptyDateCannotBeSubmitted() {
        CalendarPage page = new CalendarPage(getDriver());
        page.open(PageInfo.CALENDARS.urlKey());
        page.clearDate()
                .submitDate();

        Assert.assertFalse(page.isSuccessMessageDisplayed(), "Форма отправилась без даты");
    }
}