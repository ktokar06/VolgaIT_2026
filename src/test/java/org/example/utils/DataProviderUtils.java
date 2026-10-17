package org.example.utils;

/**
 * Класс для формирования матриц параметров
 * {@link org.testng.annotations.DataProvider} из перечислений.
 *
 * <p>Избавляет тесты от дублирования шаблонного кода преобразования
 * {@code Enum[]} в {@code Object[][]}.</p>
 */
public final class DataProviderUtils {
    private DataProviderUtils() {
    }

    /**
     * Преобразует значения перечисления в матрицу параметров,
     * где каждая строка содержит одно значение enum.
     *
     * @param values значения перечисления
     * @param <E>    тип перечисления
     * @return матрица вида {@code Object[values.length][1]}
     */
    public static <E extends Enum<E>> Object[][] toMatrix(E[] values) {
        Object[][] data = new Object[values.length][1];
        for (int i = 0; i < values.length; i++) {
            data[i][0] = values[i];
        }
        return data;
    }

    /**
     * Преобразует значения перечисления указанной категории в матрицу
     * параметров. Используется, когда одно перечисление содержит данные
     * разных категорий.
     *
     * @param values   значения перечисления
     * @param category требуемая категория
     * @param <E>      тип перечисления, реализующего {@link Categorized}
     * @param <C>      тип категории
     * @return матрица параметров только с элементами нужной категории
     */
    public static <C, E extends Enum<E> & Categorized<C>> Object[][] toMatrix(E[] values, C category) {
        int count = 0;
        for (E value : values) {
            if (value.category() == category) {
                count++;
            }
        }

        Object[][] data = new Object[count][1];
        int index = 0;
        for (E value : values) {
            if (value.category() == category) {
                data[index][0] = value;
                index++;
            }
        }
        return data;
    }

    /**
     * Контракт для перечислений, значения которых разбиты на категории.
     *
     * @param <C> тип категории
     */
    public interface Categorized<C> {
        C category();
    }
}
