package org.example.utils;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Провайдер параметров для чтения конфигурационных файлов.
 *
 * <p>Реализован как потокобезопасный Singleton с eager-инициализацией:
 * экземпляр создаётся при загрузке класса, что исключает гонки
 * и не требует синхронизации при чтении.</p>
 */
public final class ParameterProvider {
    private static final String CONFIG_PATH = "config.properties";
    private static final ParameterProvider INSTANCE = new ParameterProvider();
    private final Map<String, String> parameters;

    /**
     * Приватный конструктор. Загружает параметры из конфигурационного файла
     * {@value #CONFIG_PATH} и сохраняет их в неизменяемую карту.
     *
     * @throws RuntimeException если произошла ошибка при загрузке конфигурации
     */
    private ParameterProvider() {
        try (InputStream inputStream =
                     getClass().getClassLoader().getResourceAsStream(CONFIG_PATH)) {

            if (inputStream == null) {
                throw new IllegalStateException("Конфигурационный файл не найден: " + CONFIG_PATH);
            }

            Properties prop = new Properties();
            prop.load(inputStream);

            Map<String, String> temp = new HashMap<>();
            prop.stringPropertyNames()
                    .forEach(key -> temp.put(key, prop.getProperty(key)));

            this.parameters = Collections.unmodifiableMap(temp);

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при загрузке конфигурации", e);
        }
    }

    /**
     * Возвращает значение параметра по указанному ключу.
     *
     * <p>Метод потокобезопасен: экземпляр уже создан при загрузке класса
     *
     * @param key ключ параметра для поиска
     * @return значение параметра или {@code null}, если параметр не найден
     */
    public static String getRequired(String key) {
        return INSTANCE.parameters.get(key);
    }
}