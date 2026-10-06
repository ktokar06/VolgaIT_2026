package org.example.listener;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;
import static org.example.utils.TestIdentifierUtils.buildKey;
import static org.example.utils.TestIdentifierUtils.buildTestCommand;

/**
 * Слушатель повторных запусков тестов TestNG.
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    private static final Logger LOGGER = getLogger(RetryAnalyzer.class.getName());
    private static final int MAX_RETRIES = 2;
    private static final Map<String, Integer> retryCounts = new ConcurrentHashMap<>();

    /**
     * Определяет, нужно ли перезапустить упавший тест.
     *
     * <p>Тест перезапускается, пока количество выполненных
     * повторных запусков меньше {@link #MAX_RETRIES}.</p>
     *
     * @param result результат выполнения теста
     * @return {@code true}, если тест нужно перезапустить,
     *         иначе {@code false}
     */
    @Override
    public boolean retry(ITestResult result) {
        String key = buildKey(result);

        int retryCount = retryCounts.getOrDefault(key, 0);

        if (retryCount < MAX_RETRIES) {
            int nextRetry = retryCount + 1;
            retryCounts.put(key, nextRetry);

            LOGGER.warning(() -> "RETRY [" + nextRetry + "/" + MAX_RETRIES + "]: " + buildTestCommand(result));

            return true;
        }

        LOGGER.severe(() -> "RETRY исчерпан [" + MAX_RETRIES + "/" + MAX_RETRIES + "]: " + buildTestCommand(result));

        return false;
    }
}