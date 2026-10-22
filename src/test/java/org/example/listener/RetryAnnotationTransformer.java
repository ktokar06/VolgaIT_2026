package org.example.listener;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Класс аннотаций TestNG, который автоматически назначает
 */
public class RetryAnnotationTransformer implements IAnnotationTransformer {

    /**
     * Устанавливает {@link RetryAnalyzer} для каждой тестовой аннотации.
     *
     * @param annotation       аннотация тестового метода
     * @param testClass        класс, содержащий тестовый метод
     * @param testConstructor  конструктор тестового класса (может быть {@code null})
     * @param testMethod       тестовый метод (может быть {@code null})
     */
    @Override
    public void transform(
            ITestAnnotation annotation,
            Class testClass,
            Constructor testConstructor,
            Method testMethod) {

        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
}