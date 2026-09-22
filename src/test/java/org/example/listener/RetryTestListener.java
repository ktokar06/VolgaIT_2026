package org.example.listener;

import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.writeString;

public class RetryTestListener implements IRetryAnalyzer, ITestListener, IAnnotationTransformer, ISuiteListener {
    private static final int MAX_RETRIES = 2;
    private static final String RETRY_COUNT_ATTRIBUTE = RetryTestListener.class.getName() + ".retryCount";

    private static final Map<String, String> failedTests = new ConcurrentHashMap<>();

    /**
     * Определяет, нужно ли повторно запустить тест.
     *
     * @param result результат теста
     * @return true, если необходимо выполнить повторный запуск
     */
    @Override
    public boolean retry(ITestResult result) {
        Integer retryCount = (Integer) result.getAttribute(RETRY_COUNT_ATTRIBUTE);

        if (retryCount == null) {
            retryCount = 0;
        }

        if (retryCount < MAX_RETRIES) {
            result.setAttribute(RETRY_COUNT_ATTRIBUTE, retryCount + 1);
            return true;
        }

        return false;
    }

    /**
     * Устанавливает RetryTestListener для тестовых методов.
     *
     * @param annotation аннотация тестового метода
     * @param testClass класс теста
     * @param testConstructor конструктор теста
     * @param testMethod тестовый метод
     */
    @Override
    public void transform(
            ITestAnnotation annotation,
            Class testClass,
            Constructor testConstructor,
            Method testMethod) {

        if (testMethod != null) {
            annotation.setRetryAnalyzer(RetryTestListener.class);
        }
    }

    /**
     * Добавляет упавший тест в список неуспешных тестов.
     *
     * @param result результат теста
     */
    @Override
    public void onTestFailure(ITestResult result) {
        addToFailedSet(result);
    }

    /**
     * Добавляет пропущенный тест в список неуспешных тестов.
     *
     * @param result результат теста
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        addToFailedSet(result);
    }

    /**
     * Добавляет тест, завершившийся в рамках
     * разрешённого процента успешности, в список неуспешных.
     *
     * @param result результат теста
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        addToFailedSet(result);
    }

    /**
     * Удаляет тест из списка неуспешных после успешного выполнения.
     *
     * @param result результат теста
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        failedTests.remove(buildKey(result));
    }

    /**
     * Добавляет тест в список неуспешных.
     *
     * @param result результат теста
     */
    private void addToFailedSet(ITestResult result) {
        failedTests.put(buildKey(result), buildTestCommand(result));
    }

    /**
     * Формирует уникальный ключ теста с учётом браузера.
     *
     * @param result результат теста
     * @return уникальный ключ теста
     */
    private String buildKey(ITestResult result) {
        String browser = result.getTestContext()
                .getCurrentXmlTest()
                .getParameter("browser");

        if (browser == null) {
            browser = "default";
        }

        return browser + "|" + buildTestCommand(result);
    }

    /**
     * Формирует команду Gradle для запуска теста.
     *
     * @param result результат теста
     * @return команда запуска теста
     */
    private String buildTestCommand(ITestResult result) {
        String testClass = result.getTestClass().getName();
        String testName = result.getName();

        Object[] parameters = result.getParameters();

        if (parameters != null && parameters.length > 0) {
            StringBuilder parameterValues = new StringBuilder();

            for (int i = 0; i < parameters.length; i++) {
                if (i > 0) {
                    parameterValues.append(", ");
                }

                parameterValues.append(String.valueOf(parameters[i]));
            }
            testName = testName + " [" + parameterValues + "]";
        }

        return "--tests " + testClass + "." + testName;
    }

    /**
     * Очищает список неуспешных тестов перед запуском suite.
     *
     * @param suite TestNG suite
     */
    @Override
    public void onStart(ISuite suite) {
        failedTests.clear();
    }

    /**
     * Сохраняет список неуспешных тестов после завершения suite.
     *
     * @param suite TestNG suite
     */
    @Override
    public void onFinish(ISuite suite) {
        if (!failedTests.isEmpty()) {
            saveFailedTests();
        }
    }

    /**
     * Сохраняет команды упавших тестов в FailedTests.txt.
     */
    private static void saveFailedTests() {
        Path output = Paths.get(
                System.getProperty("user.dir"),
                "src",
                "test",
                "resources",
                "FailedTests.txt"
        );

        try {
            createDirectories(output.getParent());

            TreeSet<String> commands = new TreeSet<>(failedTests.values());
            String result = String.join(System.lineSeparator(), commands);

            writeString(output, result, StandardCharsets.UTF_8);

            System.out.println("Failed tests saved to: " + output);
            System.out.println("Failed tests: " + result);
        } catch (IOException e) {
            System.err.println(
                    "Не удалось сохранить список упавших тестов: " + e.getMessage()
            );
        }
    }
}