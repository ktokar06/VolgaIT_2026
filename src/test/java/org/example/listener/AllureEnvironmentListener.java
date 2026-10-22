package org.example.listener;

import org.testng.ISuite;
import org.testng.ISuiteListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;
import java.util.Properties;
import java.util.Locale;
import java.time.ZoneId;

import static org.example.utils.ParameterProvider.getBoolean;

import static java.util.logging.Logger.getLogger;

/**
 * Копирует метаданные Allure и записывает фактическое окружение запуска.
 */
public class AllureEnvironmentListener implements ISuiteListener {
    private static final Logger LOGGER = getLogger(AllureEnvironmentListener.class.getName());
    private static final String[] FILES_TO_COPY = {
            "executor.json",
            "categories.json"
    };
    private static final String DEFAULT_RESULTS_DIR = "build/allure-results";
    private static final String ALLURE_RESULTS_DIR_PROPERTY = "allure.results.directory";

    /**
     * Копирует статические файлы Allure перед стартом набора тестов.
     *
     * @param suite запускаемый набор тестов
     */
    @Override
    public void onStart(ISuite suite) {
        LOGGER.info(() -> "Копирование файлов Allure для набора: " + suite.getName());

        Path targetDir = resolveResultsDirectory();

        try {
            Files.createDirectories(targetDir);
        } catch (IOException e) {
            LOGGER.severe(() -> "Не удалось создать директорию результатов Allure: " + e.getMessage());
            return;
        }

        for (String fileName : FILES_TO_COPY) {
            copyResource(fileName, targetDir);
        }
        writeEnvironment(suite, targetDir);
    }

    private void writeEnvironment(ISuite suite, Path targetDir) {
        Properties environment = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("environment.properties")) {
            if (input != null) {
                environment.load(input);
            }
        } catch (IOException e) {
            LOGGER.warning("Не удалось прочитать шаблон окружения: " + e.getMessage());
        }
        environment.setProperty("OS", System.getProperty("os.name"));
        environment.setProperty("OS.Version", System.getProperty("os.version"));
        environment.setProperty("OS.Arch", System.getProperty("os.arch"));
        environment.setProperty("Java.Version", System.getProperty("java.version"));
        environment.setProperty("Java.Vendor", System.getProperty("java.vendor"));
        environment.setProperty("Country", Locale.getDefault().getCountry());
        environment.setProperty("Language", Locale.getDefault().getLanguage());
        environment.setProperty("Timezone", ZoneId.systemDefault().getId());
        environment.setProperty("Suite", suite.getName());
        environment.setProperty("Headless", String.valueOf(getBoolean("headless", false)));
        environment.setProperty("Retry.Max", String.valueOf(RetryAnalyzer.maxRetries()));
        for (String event : new String[]{"failure", "success", "skip", "retry"}) {
            String key = "screenshot.on." + event;
            environment.setProperty(key, String.valueOf(getBoolean(key,
                    "failure".equals(event) || "retry".equals(event))));
        }
        try (OutputStream output = Files.newOutputStream(targetDir.resolve("environment.properties"))) {
            environment.store(output, "Actual test runtime environment");
        } catch (IOException e) {
            LOGGER.warning("Не удалось записать окружение Allure: " + e.getMessage());
        }
    }

    /**
     * Определяет директорию результатов Allure.
     *
     * @return путь к директории результатов
     */
    private Path resolveResultsDirectory() {
        String resultsDir = System.getProperty(ALLURE_RESULTS_DIR_PROPERTY, DEFAULT_RESULTS_DIR);
        return Paths.get(resultsDir);
    }

    /**
     * Копирует ресурс из classpath в целевую директорию,
     * перезаписывая существующий файл.
     *
     * @param fileName  имя файла в {@code src/test/resources}
     * @param targetDir целевая директория
     */
    private void copyResource(String fileName, Path targetDir) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {

            if (inputStream == null) {
                LOGGER.warning(() -> "Ресурс не найден в classpath: " + fileName);
                return;
            }

            Path target = targetDir.resolve(fileName);
            Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);

            LOGGER.info(() -> "Скопирован: " + fileName + " -> " + target);

        } catch (IOException e) {
            LOGGER.severe(() -> "Ошибка копирования " + fileName + ": " + e.getMessage());
        }
    }
}