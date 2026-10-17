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
     * @param key ключ параметра для поиска
     * @return значение параметра или {@code null}, если параметр не найден
     */
    public static String getRequired(String key) {
        return INSTANCE.parameters.get(key);
    }

    /**
     * Возвращает значение параметра с учётом переопределения через
     * системное свойство. Приоритет: {@code -Dkey} &gt; config.properties
     * &gt; значение по умолчанию.
     *
     * @param key          ключ параметра
     * @param defaultValue значение по умолчанию
     * @return итоговое значение параметра
     */
    public static String get(String key, String defaultValue) {
        String fromSystem = System.getProperty(key);
        if (fromSystem != null && !fromSystem.isBlank()) {
            return fromSystem;
        }

        String fromConfig = INSTANCE.parameters.get(key);
        if (fromConfig != null && !fromConfig.isBlank()) {
            return fromConfig;
        }

        return defaultValue;
    }

    /**
     * Возвращает логическое значение параметра с учётом переопределения
     * через системное свойство.
     *
     * @param key          ключ параметра
     * @param defaultValue значение по умолчанию
     * @return {@code true}/{@code false} в зависимости от конфигурации
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        return Boolean.parseBoolean(get(key, Boolean.toString(defaultValue)));
    }

    /**
     * Возвращает целочисленное значение параметра с учётом переопределения
     * через системное свойство. При ошибке парсинга возвращается значение
     * по умолчанию.
     *
     * @param key          ключ параметра
     * @param defaultValue значение по умолчанию
     * @return целочисленное значение параметра
     */
    public static int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(get(key, Integer.toString(defaultValue)).trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}