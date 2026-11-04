package org.example.listener;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;
import static org.example.utils.ParameterProvider.getInt;
import static org.example.utils.TestIdentifierUtils.buildKey;
import static org.example.utils.TestIdentifierUtils.buildTestCommand;

/**
 * Анализатор повторных запусков тестов TestNG.
 *
 * <p>Максимальное число повторов берётся из свойства {@code retry.max}
 * ({@code config.properties}, по умолчанию 2). Счётчик попыток ведётся
 * на уникальный ключ теста и доступен другим слушателям через
 * {@link #currentAttempt(ITestResult)} — например, для формирования
 * уникальных имён скриншотов.</p>
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    private static final Logger LOGGER = getLogger(RetryAnalyzer.class.getName());
    private static final int MAX_RETRIES = Math.max(0, getInt("retry.max", 2));
    private static final String COUNTS_ATTRIBUTE = RetryAnalyzer.class.getName() + ".counts";
    private static final String ATTEMPT_ATTRIBUTE = RetryAnalyzer.class.getName() + ".attempt";

    @SuppressWarnings("unchecked")
    private static Map<String, Integer> retryCounts(ITestResult result) {
        synchronized (result.getTestContext()) {
            Object counts = result.getTestContext().getAttribute(COUNTS_ATTRIBUTE);
            if (counts == null) {
                counts = new ConcurrentHashMap<String, Integer>();
                result.getTestContext().setAttribute(COUNTS_ATTRIBUTE, counts);
            }
            return (Map<String, Integer>) counts;
        }
    }

    public static boolean willRetry(ITestResult result) {
        return result.getMethod().getRetryAnalyzer(result) instanceof RetryAnalyzer
                && currentAttempt(result) <= MAX_RETRIES;
    }

    /**
     * Определяет, нужно ли перезапустить упавший тест.
     *
     * @param result результат выполнения теста
     * @return {@code true}, если тест нужно перезапустить, иначе {@code false}
     */
    @Override
    public boolean retry(ITestResult result) {
        String key = buildKey(result);
        int retryCount = currentAttempt(result) - 1;

        if (willRetry(result)) {
            int nextRetry = retryCount + 1;
            retryCounts(result).put(key, nextRetry);

            LOGGER.warning(() -> "RETRY [" + nextRetry + "/" + MAX_RETRIES + "]: " + buildTestCommand(result));
            return true;
        }

        LOGGER.severe(() -> "RETRY исчерпан [" + MAX_RETRIES + "/" + MAX_RETRIES + "]: " + buildTestCommand(result));
        return false;
    }

    /**
     * Возвращает номер текущей попытки выполнения теста,
     * начиная с 1 (первый запуск).
     *
     * @param result результат выполнения теста
     * @return номер попытки: {@code 1} для первого запуска,
     *         {@code 2} и далее — для повторных
     */
    public static int currentAttempt(ITestResult result) {
        Object attempt = result.getAttribute(ATTEMPT_ATTRIBUTE);
        if (attempt == null) {
            attempt = retryCounts(result).getOrDefault(buildKey(result), 0) + 1;
            result.setAttribute(ATTEMPT_ATTRIBUTE, attempt);
        }
        return (Integer) attempt;
    }

    /**
     * Возвращает максимальное число повторных запусков.
     *
     * @return значение {@code retry.max}
     */
    public static int maxRetries() {
        return MAX_RETRIES;
    }
}