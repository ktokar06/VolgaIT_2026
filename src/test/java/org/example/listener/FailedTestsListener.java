package org.example.listener;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import static java.lang.System.getProperty;
import static java.lang.System.lineSeparator;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.writeString;
import static java.util.logging.Logger.getLogger;
import static org.example.utils.TestIdentifierUtils.buildKey;
import static org.example.utils.TestIdentifierUtils.buildTestCommand;

/**
 * Слушатель TestNG, который собирает информацию об упавших
 * и пропущенных тестах и сохраняет список команд для их повторного
 * запуска в файл {@code FailedTests.txt}.
 */
public class FailedTestsListener implements ITestListener, ISuiteListener {
    private static final Logger LOGGER = getLogger(FailedTestsListener.class.getName());
    private static final Map<String, String> failedTests = new ConcurrentHashMap<>();

    /**
     * Добавляет тест в список упавших при его падении.
     *
     * @param result результат выполнения теста
     */
    @Override
    public void onTestFailure(ITestResult result) {
        LOGGER.warning(() -> "Тест упал: " + result.getName());
        addToFailedSet(result);
    }

    /**
     * Добавляет тест в список упавших при его пропуске.
     *
     * @param result результат выполнения теста
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        LOGGER.warning(() -> "Тест пропущен: " + result.getName());
        addToFailedSet(result);
    }

    /**
     * Удаляет тест из списка упавших при успешном прохождении.
     *
     * @param result результат выполнения теста
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        failedTests.remove(buildKey(result));
    }

    /**
     * Очищает список упавших тестов перед началом набора.
     *
     * @param suite запускаемый набор тестов
     */
    @Override
    public void onStart(ISuite suite) {
        LOGGER.info(() -> "Старт набора: " + suite.getName());
        failedTests.clear();
    }

    /**
     * Сохраняет список упавших и пропущенных тестов в файл
     * после завершения набора.
     *
     * @param suite завершившийся набор тестов
     */
    @Override
    public void onFinish(ISuite suite) {
        LOGGER.info(() -> "Завершение набора: " + suite.getName());

        if (!failedTests.isEmpty()) {
            saveFailedTests();
        } else {
            LOGGER.info("Упавших и пропущенных тестов нет");
        }
    }

    /**
     * Добавляет тест в список упавших.
     *
     * @param result результат выполнения теста
     */
    private void addToFailedSet(ITestResult result) {
        failedTests.put(buildKey(result), buildTestCommand(result));
    }

    /**
     * Сохраняет список упавших тестов в файл
     * {@code src/test/resources/FailedTests.txt}.
     *
     * <p>Если во время записи возникает {@link IOException},
     * ошибка логируется, но не пробрасывается дальше, чтобы
     * не прерывать завершение набора тестов.</p>
     */
    private static void saveFailedTests() {
        Path output = Paths.get(getProperty("user.dir"), "src", "test", "resources", "FailedTests.txt");

        try {
            createDirectories(output.getParent());

            TreeSet<String> commands = new TreeSet<>(failedTests.values());
            String result = String.join(lineSeparator(), commands);

            writeString(output, result, StandardCharsets.UTF_8);

            LOGGER.info(() -> "Failed tests saved to: " + output);
            LOGGER.info(() -> "Failed tests:\n" + result);

        } catch (IOException e) {
            LOGGER.severe(() ->
                    "Не удалось сохранить список упавших тестов: " + e.getMessage()
            );
        }
    }
}