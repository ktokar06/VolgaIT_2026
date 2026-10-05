package org.example.utils;

import org.testng.ITestResult;

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

        return browser + "|" + buildTestCommand(result);
    }

    /**
     * Формирует команду для запуска теста через Gradle.
     *
     * @param result результат выполнения теста
     * @return команда вида {@code --tests ClassName.methodName [params]}
     */
    public static String buildTestCommand(ITestResult result) {
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
}