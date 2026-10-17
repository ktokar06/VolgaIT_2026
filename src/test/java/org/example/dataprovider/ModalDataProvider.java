package org.example.dataprovider;

import org.testng.annotations.DataProvider;

import static org.example.utils.DataProviderUtils.toMatrix;

/**
 * Провайдер данных для параметризации тестов модальных окон.
 */
public final class ModalDataProvider {
    private ModalDataProvider() {
    }

    /**
     * Предоставляет набор типов модальных окон для параметризации.
     *
     * @return массив с типами модальных окон
     */
    @DataProvider(name = "modalTypes")
    public static Object[][] modalTypes() {
        return toMatrix(ModalType.values());
    }

    /**
     * Предоставляет поля формы модального окна для проверки ввода.
     *
     * @return массив значений {@link FormField}
     */
    @DataProvider(name = "formFields")
    public static Object[][] formFields() {
        return toMatrix(FormField.values());
    }

    /**
     * Предоставляет наборы невалидных данных формы (известные баги валидации).
     *
     * @return массив значений {@link InvalidFormData}
     */
    @DataProvider(name = "invalidFormData")
    public static Object[][] invalidFormData() {
        return toMatrix(InvalidFormData.values());
    }
}