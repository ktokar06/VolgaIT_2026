package org.example.enums;

/**
 * Наборы данных для формы с багами валидации.
 */
public enum InvalidFormData {
    EMPTY_NAME("", "kirill@example.com", "Automation test",
            "форма отклоняет пустое имя"),
    INVALID_EMAIL("Kirill", "invalid-email", "Automation test",
            "форма отклоняет невалидный email"),
    EMPTY_MESSAGE("Kirill", "kirill@example.com", "",
            "форма отклоняет пустое сообщение");

    private final String name;
    private final String email;
    private final String message;
    private final String expectationIfFixed;

    InvalidFormData(String name, String email, String message, String expectationIfFixed) {
        this.name = name;
        this.email = email;
        this.message = message;
        this.expectationIfFixed = expectationIfFixed;
    }

    public String userName() {
        return name;
    }

    public String email() {
        return email;
    }

    public String message() {
        return message;
    }

    public String expectationIfFixed() {
        return expectationIfFixed;
    }
}