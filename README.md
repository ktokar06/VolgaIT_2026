# README.md

# Волга ИТ 2026 — Автоматизация тестирования на Java

Проект автоматизации UI-тестов для сайта [Practice Automation](https://practice-automation.com/).

## Клонирование репозитория

```bash
git clone https://github.com/ktokar06/VolgaIT_2026.git

cd VolgaIT_2026
```

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

Проект поддерживает:

* Chrome
* Firefox

Архитектура проекта позволяет расширять список поддерживаемых браузеров через `WebDriverFactory`.

## Allure

В проекте настроена генерация отчётов Allure.

### Что входит в отчёт

Для каждого теста в отчёт добавляются:

* шаги Page Object, размеченные `@Step`;
* параметры окружения: `Browser`, `Browser version`, `OS`, `OS version`;
* вложение `Browser logs` — console logs браузера
* вложение `OS information` — сведения об ОС и Java;
* скриншот — при событиях, указанных в системном свойстве `screenshot.events` (по умолчанию `failed,skipped`).

### Запуск тестов из IntelliJ IDEA

Шаги `@Step` в Allure работают через AspectJ Weaver. При запуске через Gradle он подключается автоматически (плагином `io.qameta.allure`), а при запуске TestNG напрямую из IntelliJ IDEA его нужно указать вручную в VM options.

**Как настроить:**

1. Открыть `Run` → `Edit Configurations…`.
2. Выбрать нужную конфигурацию TestNG.
3. В поле **VM options** добавить строку:

   ```text
   -javaagent:/путь/до/aspectjweaver-1.9.22.jar
   ```

## Перезапуск тестов

В проекте реализован автоматический retry упавших тестов.

За механизм перезапуска отвечает `RetryAnalyzer`, который назначается через `RetryAnnotationTransformer`.

Максимальное количество повторных запусков:

```text
MAX_RETRIES = 2
```

Таким образом, тест может быть выполнен максимум три раза:

```text
1 основной запуск
+ 2 повторных запуска
```

После исчерпания retry тест остаётся неуспешным.

## Файл FailedTests.txt

Если тест завершился с ошибкой или был пропущен, его команда сохраняется в:

```text
src/test/resources/FailedTests.txt
```

Пример:

```text
--tests org.example.tests.modals.ModalsSmokeTest.testModalsPageOpens
--tests org.example.tests.ads.AdsNegativeTest.testAdPopupNotClosedByEscape
```

Каждый тест записывается с новой строки.

## Повторный запуск отдельного теста

Для повторного запуска одного теста:

```bash
./gradlew test --tests org.example.tests.modals.ModalsSmokeTest.testModalsPageOpens
```

Для запуска нескольких тестов:

```bash
./gradlew test \
--tests org.example.tests.modals.ModalsSmokeTest.testModalsPageOpens \
--tests org.example.tests.ads.AdsNegativeTest.testAdPopupNotClosedByEscape
```

## Структура проекта

```text
src
└── test
    ├── java/org/example
    │   ├── config
    │   │   └── TimeoutConfig.java
    │   │
    │   ├── dataprovider
    │   │   ├── CalendarDataProvider.java
    │   │   ├── ModalDataProvider.java
    │   │   └── ModalType.java
    │   │
    │   ├── factory
    │   │   └── WebDriverFactory.java
    │   │
    │   ├── listener
    │   │   ├── AllureTestListener.java
    │   │   ├── FailedTestsListener.java
    │   │   ├── RetryAnalyzer.java
    │   │   └── RetryAnnotationTransformer.java
    │   │
    │   ├── pages
    │   │   ├── BasePage.java
    │   │   ├── AdsPage.java
    │   │   ├── CalendarPage.java
    │   │   ├── FormFieldsPage.java
    │   │   └── ModalPage.java
    │   │
    │   ├── tests
    │   │   ├── BaseTest.java
    │   │   │
    │   │   ├── ads
    │   │   │   ├── AdsSmokeTest.java
    │   │   │   ├── AdsQualityTest.java
    │   │   │   ├── AdsRegressionTest.java
    │   │   │   └── AdsNegativeTest.java
    │   │   │
    │   │   ├── calendars
    │   │   │   ├── CalendarsSmokeTest.java
    │   │   │   ├── CalendarsQualityTest.java
    │   │   │   ├── CalendarsRegressionTest.java
    │   │   │   └── CalendarsNegativeTest.java
    │   │   │
    │   │   └── modals
    │   │       ├── ModalsSmokeTest.java
    │   │       ├── ModalsQualityTest.java
    │   │       ├── ModalsRegressionTest.java
    │   │       └── ModalsNegativeTest.java
    │   │
    │   └── utils
    │       ├── ParameterProvider.java
    │       └── WaitUtils.java
    │
    └── resources
        ├── allure.properties
        ├── config.properties
        ├── testng.xml
        └── FailedTests.txt
```

## Тестовые сценарии

### Calendars

#### Smoke — 2 теста

**`testCalendarsPageOpens`**

Проверяет открытие страницы Calendars, URL и заголовок страницы.

**`testDateFieldDisplayed`**

Проверяет отображение поля даты.

#### Quality — 4 теста (с учётом параметризации)

**`testValidDateCanBeEntered`**

Проверяет ввод корректной даты. Параметризованный тест, 3 набора данных: `2026-10-15`, `2026-12-15`, `2028-02-29`. Итого 3 запуска.

**`testDateCanBeChanged`**

Проверяет замену ранее введённой даты на новую. 1 запуск.

#### Regression — 3 теста

**`testDateFieldIsEmptyInitially`**

Проверяет начальное пустое состояние поля даты.

**`testDateCanBeReenteredAfterClearing`**

Проверяет повторный ввод даты после очистки поля.

**`testValidDateCanBeSubmitted`**

Проверяет успешную отправку формы с корректной датой.

#### Negative — 7 запусков (5 методов, с учётом параметризации)

**`testInvalidDateFormatAcceptedByField`**

Известный баг: поле принимает невалидный формат. Параметризованный тест, 3 набора данных: `15-10-2026`, `2026-02-30`, `2026-13-10`. Итого 3 запуска.

**`testInvalidDateDoesNotProduceSuccessMessage`**

Проверяет, что невалидный формат не отправляет форму. 1 запуск.

**`testOutOfRangeDateAcceptedByInputField`**

Известный баг: поле принимает дату вне min (`1900-01-01`). 1 запуск.

**`testOutOfRangeDateBlocksSubmission`**

Проверяет, что дата вне диапазона не отправляет форму. 1 запуск.

**`testEmptyDateCannotBeSubmitted`**

Проверяет невозможность отправки формы без даты. 1 запуск.

**Всего Calendars: 16 запусков** (2 + 4 + 3 + 7).

---

### Modals

#### Smoke — 3 теста

**`testModalsPageOpens`**

Проверяет открытие страницы Modals, URL и заголовок страницы.

**`testSimpleModalOpens`**

Проверяет открытие простого modal.

**`testFormModalOpens`**

Проверяет открытие modal с формой.

#### Quality — 5 тестов

**`testSimpleModalText`**

Проверяет содержимое простого modal.

**`testSimpleModalCloses`**

Проверяет закрытие простого modal кнопкой Close.

**`testFormAcceptsName`**

Проверяет ввод имени и фактическое значение поля.

**`testFormAcceptsEmail`**

Проверяет ввод email и фактическое значение поля.

**`testFormAcceptsMessage`**

Проверяет ввод сообщения и фактическое значение поля.

#### Regression — 2 теста

**`testFormSubmission`**

Проверяет успешную отправку формы с корректными данными.

Поле Message заполняется списком элементов из раздела Automation Tools, полученным средствами Selenium и преобразованным в строку через запятую.

**`testFormModalCloses`**

Проверяет закрытие modal с формой кнопкой Close.

#### Negative — 5 запусков (4 метода, с учётом параметризации)

**`testEmptyNameSubmissionIsKnownBug`**

Известный баг: форма отправляется без обязательного имени. 1 запуск.

**`testInvalidEmailSubmissionIsKnownBug`**

Известный баг: форма отправляется с невалидным email. 1 запуск.

**`testEmptyMessageSubmissionIsKnownBug`**

Известный баг: форма отправляется без message. 1 запуск.

**`testModalNotClosedByEscape`**

Проверяет, что modal не закрывается клавишей Escape. Параметризованный тест, 2 набора данных: SIMPLE, FORM. Итого 2 запуска.

**Всего Modals: 15 запусков** (3 + 5 + 2 + 5).

---

### Ads

#### Smoke — 3 теста

**`testAdsPageOpens`**

Проверяет открытие страницы Ads, URL и заголовок страницы.

**`testAdPopupAppearsAutomatically`**

Проверяет автоматическое появление рекламного окна.

**`testAdPopupCloses`**

Проверяет закрытие рекламного окна кнопкой Close.

#### Quality — 4 теста

**`testAdPopupTitle`**

Проверяет заголовок рекламного окна.

Ожидаемое значение:

```text
Hi
```

**`testAdPopupText`**

Проверяет текст рекламного окна.

Ожидаемое значение:

```text
I am an ad.
```

**`testAdPopupCloseButtonAccessible`**

Проверяет наличие `aria-label="Close"` у кнопки закрытия.

**`testAdPopupCloseButtonDisplayed`**

Проверяет отображение кнопки закрытия.

#### Regression — 1 тест

**`testAdPopupNotDisplayedImmediately`**

Проверяет, что рекламное окно не появляется непосредственно после открытия страницы.

#### Negative — 3 теста

**`testAdPopupNotClosedByEscape`**

Проверяет, что рекламное окно не закрывается клавишей Escape.

**`testAdPopupNotClosedByOverlayClick`**

Проверяет, что рекламное окно не закрывается кликом по overlay.

**`testAdPopupNotClosedByContentClick`**

Проверяет, что рекламное окно не закрывается кликом по его содержимому.

**Всего Ads: 11 запусков.**

## Общее количество тестов

```text
Modals    — 15
Calendars — 16
Ads       — 11
----------------
Всего     — 42 запуска
```

При запуске в двух браузерах:

```text
42 × 2 = 84 тестовых запуска
```