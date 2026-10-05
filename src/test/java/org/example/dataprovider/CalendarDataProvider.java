package org.example.dataprovider;

import org.testng.annotations.DataProvider;

/**
 * Провайдер данных для тестирования календаря.
 */
public final class CalendarDataProvider {
    private CalendarDataProvider() {
    }

    /**
     * Предоставляет набор корректных дат для параметризации тестов.
     *
     * @return массив корректных дат в формате {@code yyyy-MM-dd}
     */
    @DataProvider(name = "validDates")
    public static Object[][] validDates() {
        return new Object[][]{
                {"2026-10-15"},
                {"2026-12-15"},
                {"2028-02-29"}
        };
    }

    /**
     * Предоставляет набор некорректных дат для параметризации тестов.
     *
     * @return массив некорректных дат
     */
    @DataProvider(name = "invalidDates")
    public static Object[][] invalidDates() {
        return new Object[][]{
                {"15-10-2026"},
                {"2026-02-30"},
                {"2026-13-10"}
        };
    }
}