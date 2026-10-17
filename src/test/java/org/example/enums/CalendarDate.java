package org.example.enums;

import org.example.utils.DataProviderUtils;

/**
 * Тестовые даты для страницы Calendars.
 *
 * <p>Каждая константа хранит значение даты, категорию и короткое
 * описание сценария.</p>
 */
public enum CalendarDate implements DataProviderUtils.Categorized<CalendarDate.Category> {
    TODAY_LIKE("2026-10-15", Category.VALID, "корректная дата"),
    FUTURE_MONTH("2026-12-15", Category.VALID, "корректная дата другого месяца"),
    LEAP_DAY("2028-02-29", Category.VALID, "29 февраля високосного года"),

    WRONG_FORMAT("15-10-2026", Category.INVALID_FORMAT, "неверный формат dd-MM-yyyy"),
    NON_EXISTENT_DAY("2026-02-30", Category.INVALID_FORMAT, "несуществующий день"),
    INVALID_MONTH("2026-13-10", Category.INVALID_FORMAT, "несуществующий месяц"),

    OUT_OF_RANGE("1900-01-01", Category.OUT_OF_RANGE, "дата вне допустимого диапазона (min)");

    public enum Category {
        VALID,
        INVALID_FORMAT,
        OUT_OF_RANGE
    }

    private final String value;
    private final Category category;
    private final String description;

    CalendarDate(String value, Category category, String description) {
        this.value = value;
        this.category = category;
        this.description = description;
    }

    public String value() {
        return value;
    }

    @Override
    public Category category() {
        return category;
    }

    public String description() {
        return description;
    }
}