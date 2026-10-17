package org.example.dataprovider;

import org.example.enums.CalendarDate;
import org.example.utils.DataProviderUtils;
import org.testng.annotations.DataProvider;

import static org.example.utils.DataProviderUtils.toMatrix;

/**
 * Провайдер данных для тестирования календаря.
 *
 * <p>Данные берутся из перечисления {@link CalendarDate} и фильтруются
 * по категории {@link CalendarDate.Category} через {@link DataProviderUtils}.</p>
 */
public final class CalendarDataProvider {
    private CalendarDataProvider() {
    }

    /**
     * Предоставляет набор корректных дат для параметризации тестов.
     *
     * @return массив валидных значений {@link CalendarDate}
     */
    @DataProvider(name = "validDates")
    public static Object[][] validDates() {
        return toMatrix(CalendarDate.values(), CalendarDate.Category.VALID);
    }

    /**
     * Предоставляет набор дат с некорректным форматом/значением.
     *
     * @return массив значений {@link CalendarDate} категории
     *         {@link CalendarDate.Category#INVALID_FORMAT}
     */
    @DataProvider(name = "invalidDates")
    public static Object[][] invalidDates() {
        return toMatrix(CalendarDate.values(), CalendarDate.Category.INVALID_FORMAT);
    }
}