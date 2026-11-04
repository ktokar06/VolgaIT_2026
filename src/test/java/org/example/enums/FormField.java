package org.example.enums;

import org.example.pages.ModalPage;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Поля формы модального окна и ожидаемые значения для проверки ввода.
 */
public enum FormField {
    NAME("Kirill", ModalPage::enterName, ModalPage::getNameValue),
    EMAIL("kirill@example.com", ModalPage::enterEmail, ModalPage::getEmailValue),
    MESSAGE("Automation test", ModalPage::enterMessage, ModalPage::getMessageValue);

    private final String value;
    private final BiConsumer<ModalPage, String> setter;
    private final Function<ModalPage, String> getter;

    FormField(String value,
              BiConsumer<ModalPage, String> setter,
              Function<ModalPage, String> getter) {
        this.value = value;
        this.setter = setter;
        this.getter = getter;
    }

    public String value() {
        return value;
    }

    public void fill(ModalPage page) {
        setter.accept(page, value);
    }

    public String actualValue(ModalPage page) {
        return getter.apply(page);
    }
}