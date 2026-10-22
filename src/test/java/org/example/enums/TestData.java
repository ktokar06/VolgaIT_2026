package org.example.enums;

/**
 * Общие тестовые данные и ожидаемые значения, переиспользуемые
 * в нескольких тестовых классах.
 *
 * <p>Каждая константа хранит одно значение, доступное через {@link #value()}.
 * Вынесение строк в перечисление убирает «магические» литералы из тестов
 * и даёт единую точку правки общих данных.</p>
 */
public enum TestData {
    VALID_DATE("2026-10-15"),
    CHANGED_DATE("2026-11-20"),

    USER_NAME("Kirill"),
    USER_EMAIL("kirill@example.com"),

    FORM_SUCCESS_MESSAGE("Thank you for your response. ✨"),
    SIMPLE_MODAL_TEXT("Hi, I’m a simple modal."),

    ADS_POPUP_TITLE("Hi"),
    ADS_POPUP_TEXT("I am an ad."),
    CLOSE_ARIA_LABEL("Close");

    private final String value;

    TestData(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}