package org.example.factory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.Locale;

/**
 * Класс для создания экземпляров WebDriver.
 */
public final class WebDriverFactory {
    private WebDriverFactory() {
    }

    /**
     * Создаёт WebDriver для указанного браузера.
     *
     * @param browser имя браузера
     * @param headless запуск браузера без графического интерфейса
     * @return экземпляр WebDriver
     * @throws IllegalArgumentException если браузер не указан
     * @throws IllegalArgumentException если браузер не поддерживается
     */
    public static WebDriver create(String browser, boolean headless) {
        if (browser == null || browser.isBlank()) {
            throw new IllegalArgumentException("Браузер не указан");
        }

        switch (browser.toLowerCase(Locale.ROOT)) {
            case "chrome":
                return createChromeDriver(headless);
            case "firefox":
                return createFirefoxDriver(headless);
            default:
                throw new IllegalArgumentException("Неподдерживаемый браузер: " + browser);
        }
    }

    /**
     * Создаёт экземпляр ChromeDriver.
     *
     * @param headless запуск в headless-режиме
     * @return экземпляр ChromeDriver
     */
    private static WebDriver createChromeDriver(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        }

        WebDriver driver = new ChromeDriver(options);
        if (!headless) {
            driver.manage().window().maximize();
        }

        return driver;
    }

    /**
     * Создаёт экземпляр FirefoxDriver.
     *
     * @param headless запуск в headless-режиме
     * @return экземпляр FirefoxDriver
     */
    private static WebDriver createFirefoxDriver(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("-headless");
            options.addArguments("--width=1920");
            options.addArguments("--height=1080");
        }

        WebDriver driver = new FirefoxDriver(options);
        if (!headless) {
            driver.manage().window().maximize();
        }

        return driver;
    }
}