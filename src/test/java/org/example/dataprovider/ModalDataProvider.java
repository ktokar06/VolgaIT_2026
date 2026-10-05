package org.example.dataprovider;

import org.testng.annotations.DataProvider;

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
        return new Object[][]{
                {ModalType.SIMPLE},
                {ModalType.FORM}
        };
    }
}