# UI Automation Project

Проект автоматизации UI-тестов для сайта [Practice Automation](https://practice-automation.com/).

## Используемые технологии

* Java 17
* Selenium WebDriver
* TestNG
* Gradle
* Allure
* Page Object Model (POM)
* Fluent Interface
* WebDriver Factory

## Требования для запуска

* JDK 17+
* Chrome и/или Firefox
* Интернет-соединение

## Запуск тестов

Для запуска всех тестов:

```bash
./gradlew test
```

Для очистки проекта и запуска тестов:

```bash
./gradlew clean test
```

Для запуска в headless-режиме:

```bash
./gradlew test -Dheadless=true
```

## Поддерживаемые браузеры

Проект поддерживает два браузера:

* Chrome
* Firefox

Архитектура проекта позволяет расширять список поддерживаемых браузеров. Для добавления нового браузера достаточно реализовать его создание в `WebDriverFactory`.

## FailedTests.txt

Если во время запуска есть упавшие или пропущенные тесты, автоматически создаётся или обновляется файл:

```text
src/test/resources/FailedTests.txt
```

В файле сохраняются команды для запуска тестов, которые не прошли.

Пример:

```text
--tests org.example.tests.modals.ModalsSmokeTest.testModalsPageOpens
--tests org.example.tests.ads.AdsRegressionTest.testAdPopupNotClosedByEscape
```

Каждый тест записывается с новой строки.

## Повторный запуск упавшего теста

Для повторного запуска конкретного теста используется команда:

```bash
./gradlew test --tests org.example.tests.modals.ModalsSmokeTest.testModalsPageOpens
```

Для запуска нескольких тестов:

```bash
./gradlew test \
--tests org.example.tests.modals.ModalsSmokeTest.testModalsPageOpens \
--tests org.example.tests.ads.AdsRegressionTest.testAdPopupNotClosedByEscape
```

## Структура проекта

```text
src
└── test
    ├── java/org/example
    │   ├── config
    │   │   └── TimeoutConfig.java          # Таймауты ожиданий
    │   ├── factory
    │   │   └── WebDriverFactory.java       # Фабрика WebDriver
    │   ├── listener
    │   │   ├── AllureTestListener.java     # Скриншоты и логирование
    │   │   └── RetryTestListener.java      # Ретраи и сбор упавших тестов
    │   ├── pages
    │   │   ├── BasePage.java               # Базовый Page Object
    │   │   ├── CalendarPage.java
    │   │   ├── ModalPage.java
    │   │   └── AdsPage.java
    │   ├── tests
    │   │   ├── BaseTest.java               # @BeforeMethod / @AfterMethod
    │   │   ├── ads
    │   │   │   ├── AdsSmokeTest.java
    │   │   │   ├── AdsQualityTest.java
    │   │   │   └── AdsRegressionTest.java
    │   │   ├── calendars
    │   │   │   ├── CalendarsSmokeTest.java
    │   │   │   ├── CalendarsQualityTest.java
    │   │   │   └── CalendarsRegressionTest.java
    │   │   └── modals
    │   │       ├── ModalsSmokeTest.java
    │   │       ├── ModalsQualityTest.java
    │   │       └── ModalsRegressionTest.java
    │   └── utils
    │       ├── ParameterProvider.java      # Чтение config.properties
    │       └── WaitUtils.java              # WebDriverWait
    │
    └── resources
        ├── configuration
        ├── allure.properties
        ├── testng.xml
        └── FailedTests.txt
```

## Тестовые сценарии

### Calendars

**Smoke (3)**

* `testCalendarsPageOpens` — страница открывается, URL и заголовок соответствуют ожидаемым.
* `testDateFieldDisplayed` — поле даты отображается.
* `testSubmitButtonDisplayed` — кнопка Submit отображается.

**Quality / позитивные (4)**

* `testValidDateCanBeEntered` — корректная дата вводится.
* `testDateCanBeChanged` — новая дата заменяет предыдущую.
* `testCurrentYearDateCanBeEntered` — дата текущего года принимается.
* `testLeapYearDateCanBeEntered` — високосная дата (29.02.2028) принимается.

**Regression / негативные (4)**

* `testDateFieldIsEmptyInitially` — поле пустое при открытии.
* `testDateFieldCanBeCleared` — поле очищается.
* `testDateFieldRemainsEmptyAfterClearing` — после очистки поле пустое.
* `testDateCanBeReenteredAfterClearing` — после очистки можно ввести дату снова.

### Modals

**Smoke (3)**

* `testModalsPageOpens` — страница открывается, URL и заголовок верны.
* `testSimpleModalOpens` — простой modal открывается.
* `testFormModalOpens` — modal с формой открывается.

**Quality / позитивные (5)**

* `testSimpleModalText` — текст простого modal соответствует ожидаемому.
* `testSimpleModalCloses` — простой modal закрывается кнопкой.
* `testFormAcceptsName` — форма принимает имя.
* `testFormAcceptsEmail` — форма принимает email.
* `testFormAcceptsMessage` — форма принимает сообщение.

**Regression / негативные (4)**

* `testFormSubmission` — отправка формы с валидными данными → сообщение об успехе.
* `testFormModalCloses` — форма закрывается кнопкой.
* `testSimpleModalNotClosedByEscape` — простой modal не закрывается `Esc`.
* `testFormModalNotClosedByEscape` — форма не закрывается `Esc`.

### Ads

**Smoke (3)**

* `testAdsPageOpens` — страница открывается, URL и заголовок верны.
* `testAdPopupAppearsAutomatically` — рекламное окно появляется само.
* `testAdPopupCloses` — окно закрывается кнопкой Close.

**Quality (4)**

* `testAdPopupTitle` — заголовок окна `Hi`.
* `testAdPopupText` — текст `I am an ad.`
* `testAdPopupCloseButtonAccessible` — `aria-label="Close"`.
* `testAdPopupCloseButtonDisplayed` — кнопка Close видима.

**Regression / негативные (3)**

* `testAdPopupNotDisplayedImmediately` — окно не появляется сразу.
* `testAdPopupNotClosedByEscape` — окно не закрывается `Esc`.
* `testAdPopupNotClosedByOverlayClick` — окно не закрывается кликом по overlay.

**Итого:** 33 теста × 2 браузера = **66 прогонов**.