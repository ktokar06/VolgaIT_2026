package org.example.utils;

import org.testng.ITestResult;

import static java.util.Arrays.deepToString;

/**
 * Класс для формирования идентификатора теста
 * и команды его запуска через Gradle.
 */
public final class TestIdentifierUtils {
    private TestIdentifierUtils() {
    }

    /**
     * Формирует уникальный ключ теста на основе браузера и команды запуска.
     *
     * @param result результат выполнения теста
     * @return уникальный ключ теста
     */
    public static String buildKey(ITestResult result) {
        String browser = result.getTestContext()
                .getCurrentXmlTest()
                .getParameter("browser");

        if (browser == null || browser.isBlank()) {
            browser = "default";
        }

        return browser + "|" + buildTestCommand(result) + "|"
                + deepToString(result.getParameters());
    }

    /**
     * Формирует команду для запуска теста через Gradle.
     *
     * @param result результат выполнения теста
     * @return команда вида {@code --tests ClassName.methodName}; Gradle повторяет все наборы данных метода
     */
    public static String buildTestCommand(ITestResult result) {
        String testClass = result.getTestClass().getName();
        String testName = result.getName();

        return "--tests " + testClass + "." + testName;
    }
}