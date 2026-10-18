# README.md

# Волга ИТ 2026 — Автоматизация тестирования на Java

Проект автоматизации UI-тестов для сайта [Practice Automation](https://practice-automation.com/).

## Клонирование репозитория

```bash
git clone https://github.com/ktokar06/VolgaIT_2026.git

cd VolgaIT_2026
```

## Используемые технологии

* Java 17 (toolchain)
* Selenium WebDriver 4.27
* TestNG 7.10
* Gradle 9.2.1 (wrapper) — совместим с JDK 17–25
* Allure (Gradle plugin 4.3.0)
* Page Object Model (POM)
* Fluent Interface
* WebDriver Factory (ThreadLocal)

## Требования для запуска

* Установленный JDK 17 для компиляции и тестов (Java toolchain); Gradle 9.2.1 можно запускать на JDK 17–25. Автоматическая загрузка JDK не настроена.
* Chrome и/или Firefox (драйверы подбирает Selenium Manager автоматически)
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

### Конфигурация запуска

Параметры читаются из `src/test/resources/config.properties` и могут быть
переопределены через системные свойства (`-Dkey=value`). Приоритет:
`-Dkey` → `config.properties` → значение по умолчанию.

| Свойство | По умолчанию | Назначение |
|----------|--------------|------------|
| `browser` | `chrome` | При запуске полного набора браузеры задаются параметрами `testng.xml`; `-Dbrowser` сейчас не фильтрует этот набор. |
| `headless` | `false` | Headless-режим. |
| `retry.max` | `2` | Максимальное число повторных запусков упавшего теста. |
| `screenshot.on.failure` | `true` | Скриншот при падении теста. |
| `screenshot.on.success` | `false` | Скриншот при успешном тесте. |
| `screenshot.on.skip` | `false` | Скриншот при пропуске теста. |
| `screenshot.on.retry` | `true` | Скриншот при повторной попытке (retry). |

Пример запуска в headless-режиме с включёнными скриншотами успеха:

```bash
./gradlew test -Dheadless=true -Dscreenshot.on.success=true
```

## Поддерживаемые браузеры

Проект поддерживает:

* Chrome
* Firefox

Архитектура проекта позволяет расширять список поддерживаемых браузеров через `WebDriverFactory`.

`WebDriverFactory` хранит драйверы в `ThreadLocal<WebDriver>`, поэтому каждый
поток работает с собственным экземпляром драйвера. Это обеспечивает
корректный параллельный запуск (в `testng.xml` — `parallel="tests"`).
Жизненный цикл драйвера: `createDriver()` в `@BeforeMethod`, `quitDriver()`
в `@AfterMethod` (с гарантированным `ThreadLocal.remove()`).

## Устойчивые локаторы

Тест открывает страницу через `page.open(urlKey)` — общий метод `BasePage`
с шагом Allure, который получает URL из конфигурации.
`BaseTest` управляет жизненным циклом WebDriver.

Повторяющиеся операции страниц вынесены в `BasePage`: ожидания, клик,
получение текста и значения поля, заполнение поля, выделение текста,
открытие и закрытие попапов. Методы используют драйвер экземпляра страницы.
Публичные методы конкретных страниц вызывают эти операции и сохраняют
Fluent Interface и предметные шаги `@Step` для Allure.

Сайт построен на WordPress, где часть элементов имеет динамические id,
меняющиеся при пересоздании контента в CMS (например `#popmake-1272`,
`#g1051-name`). Чтобы тесты не были хрупкими, локаторы опираются на
устойчивые признаки:

* **Попапы** адресуются по авторскому `slug` в атрибуте `data-popmake`
  (`simple-modal`, `sandbox-form-modal`) и по классам Popup Maker
  (`.pum-container`, `.pum-title`, `.pum-content`, `.pum-close`).
* **Рекламное окно** — по признаку `.pum-overlay.auto_open` (единственное
  открывается автоматически).
* **Поля форм Jetpack** — по суффиксу `name` (`name$='-name'`,
  `name$='-email'`, `name$='-message'`, `name$='selectorenteradate'`),
  что исключает зависимость от динамического префикса экземпляра формы.
* **Заголовки/кнопки** — по тексту и типу (`h1`, `button[type='submit']`).

## Allure

В проекте настроена генерация отчётов Allure.

**Генерация отчёта:**
```bash
./gradlew allureReport
```
Эта команда создаёт HTML-отчёт из уже полученных результатов в директории
`build/reports/allure-report/allureReport`. Сначала выполните `./gradlew test`.
Для локального просмотра используйте `allureServe` или HTTP-сервер.

**Просмотр отчёта (Allure Serve):**
```bash
./gradlew allureServe
```
Эта команда генерирует отчет во временную директорию и автоматически открывает его в браузере по умолчанию. Удобно использовать при локальной разработке для быстрой проверки результатов.


### Что входит в отчёт

Для каждого теста в отчёт добавляются:

* шаги Page Object, размеченные `@Step`;
* вложение `Логи браузера` — console logs браузера;
* параметры фактического окружения: браузер, версия, ОС, headless;
* структура Suites: браузер → страница → Smoke/Quality/Regression/Negative;
* названия наборов DataProvider в заголовках тестов;
* `Контекст выполнения`: метод, тестовые данные, попытка, событие, поток,
  текущий URL и команда повторного запуска метода;
* ссылка на страницу в конце выполнения;
* при ошибке: DOM страницы, capabilities WebDriver и stack trace;
* скриншот — только по конфигурации (см. ниже).

В Environment автоматически записываются реальные Java/ОС/часовой пояс и
настройки retry/скриншотов. Категории ошибок отделяют несовпадения ожиданий,
проблемы ожиданий/локаторов, WebDriver и прочие ошибки выполнения. Категория
сама по себе не доказывает, что причина ошибки находится в продукте.

Перед новым Gradle-прогоном удаляются старые результаты и вложения, а история
копируется из `build/allure-history/`. После успешной генерации `allureReport` эта
директория обновляется. Она исключена из Git вместе с `build/`; `clean` удаляет
её и сбрасывает историю.
Это предотвращает смешивание запусков и ложные retries. Для накопления трендов
выполняйте `test`, затем `allureReport` после каждого прогона.

**Categories** показывает только категории, которым соответствуют результаты
текущего прогона. Если все итоговые тесты прошли, список пуст. Неуспешные
попытки перед успешным retry доступны во вкладке Retries.

**Trend** строится по истории разных прогонов: для сравнения нужны минимум
два прогона с генерацией отчёта после каждого. Повторная генерация отчёта из
тех же результатов не добавляет новый прогон. Вложения «Контекст выполнения»
не являются источником данных для Categories или Trend.

Даже при падении тестов отчёт можно собрать отдельной командой:

```bash
./gradlew test -Dheadless=true
./gradlew allureReport
./gradlew allureServe
```

Перед сборкой отчёта дождитесь завершения тестового процесса. Для сравнения
результатов разных коммитов в CI сохраняйте и восстанавливайте `build/allure-history/`
между сборками. `allureServe` используется для просмотра; историю для следующего
прогона сохраняет `allureReport`. Для сброса истории выполните `clean` перед
следующим прогоном. Для накопления Trend запускайте тесты без `clean`.

### Скриншоты

Снятие скриншотов управляет конфигурируемый слушатель `AllureTestListener`.
Он реагирует на события PASSED / FAILED / SKIPPED / RETRY, а решение о
снятии скриншота принимается **по конфигу**, а не после каждого теста:

```properties
screenshot.on.failure=true
screenshot.on.success=false
screenshot.on.skip=false
screenshot.on.retry=true
```

Каждый скриншот получает уникальное имя, чтобы вложения разных попыток
не перезатирали друг друга:

```text
testName_attemptN_timestamp.png
```

Например: `testFormSubmission_attempt2_20261016_103245_118.png`.
Тип события указан во вложении `Контекст выполнения`. Allure сохраняет сами
файлы с UUID, поэтому даже параллельные попытки не перезаписывают вложения.

**Allure: Главная страница отчета:**

<img width="1918" height="932" alt="image" src="https://github.com/user-attachments/assets/4a326640-c375-46f0-b5f4-c456c0955ca0" />

**Как выглядит отчёт, если тест прошёл:**

<img width="1918" height="932" alt="image" src="https://github.com/user-attachments/assets/c2029555-7a38-4f71-bd3e-72a7bcf6bb34" />

**Если тест не прошёл, отчёт будет выглядеть так:**

<img width="1918" height="932" alt="image" src="https://github.com/user-attachments/assets/55582c88-5f9c-4476-aa91-f7ad37cff475" />

**Также отображается перезапуск тестов:**

<img width="885" height="268" alt="image" src="https://github.com/user-attachments/assets/0aab64de-d2b2-4bec-b987-3e7cecd1a315" />

### Запуск тестов из IntelliJ IDEA

Шаги `@Step` в Allure работают через AspectJ Weaver. При запуске через Gradle он подключается автоматически (плагином `io.qameta.allure`), а при запуске TestNG напрямую из IntelliJ IDEA его нужно указать вручную в VM options.

**Как настроить:**

1. Открыть `Run` → `Edit Configurations…`.
2. Выбрать нужную конфигурацию TestNG.
3. В поле **VM options** добавить строку:

```text
-javaagent:/путь/до/aspectjweaver-1.9.22.jar
```

**Пример настройки:**

<img width="866" height="734" alt="Screenshot From 2026-10-08 00-42-04" src="https://github.com/user-attachments/assets/8a63a51d-8ef7-4d0e-9436-2a823347a8dc" />

## Перезапуск тестов

В проекте реализован автоматический retry упавших тестов.

За механизм перезапуска отвечает `RetryAnalyzer`, который назначается всем
тестам через `RetryAnnotationTransformer` (зарегистрирован в `testng.xml`).

Максимальное число повторных запусков берётся из свойства `retry.max`
(`config.properties`, по умолчанию `2`) и может быть переопределено через
`-Dretry.max=N`.

При `retry.max=2` тест может быть выполнен максимум три раза:

```text
1 основной запуск
+ 2 повторных запуска
```

После исчерпания retry тест остаётся неуспешным. Повторные попытки
корректно отображаются во вкладке **Retries** отчёта Allure; при событии
RETRY (если включено `screenshot.on.retry`) снимается отдельный скриншот
с уникальным именем, поэтому аттачи попыток не перезатираются.
Номер попытки фиксируется в результате TestNG; счётчики ограничены контекстом
запуска. Номер попытки и время не входят в идентификатор истории теста.

## Повторный запуск отдельного теста

Команду запуска можно скопировать из вложения **«Контекст выполнения»** в Allure.
Команда для параметризованного метода повторяет все его наборы данных:
Gradle `--tests` не выбирает отдельную строку DataProvider по значению enum.

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

## Запуск в Jenkins

### Подготовка агента и Jenkins

Пример рассчитан на Linux-агент с меткой `ui-tests`. На агенте должны быть
установлены JDK 17, Git, Google Chrome и Firefox. Пользователь агента должен
иметь доступ к интернету для загрузки Gradle, зависимостей, драйверов через
Selenium Manager и обращения к тестируемому сайту.

В **Manage Jenkins → Plugins** установите плагины **Pipeline**,
**Pipeline: Declarative**, **Git**, **JUnit** и **Allure Jenkins Plugin**.
Для автоматического запуска по GitHub webhook также нужен **GitHub plugin**.

В **Manage Jenkins → Tools** настройте:

| Инструмент | Имя | Настройка |
|------------|-----|-----------|
| JDK | `jdk17` | Путь к установленному JDK 17 на агенте |
| Allure Commandline | `allure230` | Версия 2.30.0, можно включить автоматическую установку |

Gradle используется из wrapper проекта. Имена инструментов и метка агента
в Pipeline должны совпадать с настройками вашей установки Jenkins.

### Pipeline script

Создайте **New Item → Pipeline**, в разделе **Pipeline** выберите
**Definition → Pipeline script** и вставьте пример ниже. Он хранится в
конфигурации задачи Jenkins; файл `Jenkinsfile` для этого варианта не требуется.

```groovy
pipeline {
    agent { label 'ui-tests' }

    tools { jdk 'jdk17' }

    options {
        timestamps()
        disableConcurrentBuilds()
        timeout(time: 30, unit: 'MINUTES')
        buildDiscarder(logRotator(
            numToKeepStr: '20',
            artifactNumToKeepStr: '10'
        ))
        skipDefaultCheckout(true)
    }

    parameters {
        choice(
            name: 'RETRY_MAX',
            choices: ['0', '1', '2'],
            description: 'Количество повторов упавшего теста'
        )
    }

    stages {
        stage('Checkout') {
            steps {
                deleteDir()
                git(
                    branch: 'semi-final',
                    url: 'https://github.com/ktokar06/VolgaIT_2026.git'
                )
            }
        }

        stage('Проверка окружения') {
            steps {
                sh '''
                    java -version
                    google-chrome --version
                    firefox --version
                    chmod +x gradlew
                '''
            }
        }

        stage('UI-тесты: Chrome и Firefox') {
            steps {
                sh '''
                    ./gradlew clean test \
                        --no-daemon \
                        --console=plain \
                        -Dheadless=true \
                        -Dretry.max="$RETRY_MAX"
                '''
            }
            post {
                always {
                    junit(
                        testResults: 'build/test-results/test/TEST-*.xml',
                        allowEmptyResults: true
                    )
                    archiveArtifacts(
                        artifacts: 'build/allure-results/**,build/reports/tests/test/**',
                        allowEmptyArchive: true
                    )
                    script {
                        if (fileExists('build/allure-results')) {
                            allure(
                                commandline: 'allure230',
                                includeProperties: false,
                                jdk: 'jdk17',
                                reportBuildPolicy: 'ALWAYS',
                                results: [[path: 'build/allure-results']]
                            )
                        }
                    }
                }
            }
        }
    }
}
```

Для приватного репозитория добавьте в шаг `git` параметр
`credentialsId: 'идентификатор-учётных-данных-Jenkins'`.

### Пример ручного запуска

1. Отправьте нужную версию проекта в удалённую ветку `semi-final`.
2. Сохраните Pipeline и нажмите **Build Now** для первого запуска.
3. После регистрации параметров используйте **Build with Parameters**.
   Например, выберите `RETRY_MAX=1` и нажмите **Build**.
4. В **Console Output** проверьте выполнение Gradle. Оба браузера запускаются
   в headless-режиме по `testng.xml`; параметр `-Dbrowser` для выбора одного
   браузера в этом примере не используется.
5. На странице сборки откройте **Test Result**, **Allure Report** или
   **Artifacts** с исходными результатами и диагностикой.

При падении тестов Gradle возвращает ненулевой код, и сборка становится
неуспешной. Блок `post → always` этапа тестов публикует доступные результаты
и при таком завершении. Если ошибка произошла до создания результатов
(например, при компиляции), отчёта о выполненных тестах может не быть:
причину смотрите в Console Output.

В этом Pipeline HTML-отчёт создаёт **Allure Jenkins Plugin**. Вызывать
`./gradlew allureReport` или `allureServe` дополнительно не нужно. Историю
для Trend плагин переносит из предыдущих отчётов этой же задачи Jenkins;
для её накопления сохраняйте предыдущие сборки с Allure-отчётами. Локальная
директория `build/allure-history/` в этом варианте не используется.

### Автоматический запуск

Для ночного запуска добавьте в `pipeline`, рядом с `options`, блок:

```groovy
triggers {
    cron('H 2 * * *')
}
```

Он запускает задачу ежедневно между 02:00 и 02:59 по часовому поясу Jenkins.

Для запуска после push вместо расписания можно использовать:

```groovy
triggers {
    githubPush()
}
```

В GitHub откройте **Settings → Webhooks → Add webhook** и укажите
`https://адрес-jenkins/github-webhook/`, тип содержимого `application/json`
и событие **push**. Этот адрес должен быть доступен GitHub. После изменения
скрипта выполните ручную сборку, чтобы Jenkins зарегистрировал triggers.

Пример требует настроенного Jenkins и агента; выполнение в вашей установке
Jenkins необходимо проверить первым ручным запуском.

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
    │   │   └── ModalDataProvider.java
    │   │
    │   ├── enums
    │   │   ├── CalendarDate.java
    │   │   ├── FormField.java
    │   │   ├── InvalidFormData.java
    │   │   ├── ModalType.java
    │   │   ├── PageInfo.java
    │   │   └── TestData.java
    │   │
    │   ├── factory
    │   │   └── WebDriverFactory.java
    │   │
    │   ├── listener
    │   │   ├── AllureEnvironmentListener.java
    │   │   ├── AllureTestListener.java
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
    │       ├── DataProviderUtils.java
    │       ├── ParameterProvider.java
    │       ├── TestIdentifierUtils.java
    │       └── WaitUtils.java
    │
    └── resources
        ├── allure.properties
        ├── config.properties
        ├── environment.properties
        ├── categories.json
        ├── executor.json
        └── testng.xml
```

## Тестовые данные (enum)

Чтобы убрать «магические» строки из тестов, тестовые данные вынесены в
перечисления в пакете `enums`:

* **`PageInfo`** — ключ URL, часть адреса и заголовок для каждой тестируемой
  страницы (Calendars/Modals/Ads). Используется в smoke-тестах.
* **`TestData`** — общие значения и ожидаемые тексты (валидная дата, имя,
  email, сообщение об успехе, тексты попапов и т.д.).
* **`CalendarDate`** — даты с категорией (`VALID` / `INVALID_FORMAT` /
  `OUT_OF_RANGE`) и описанием.
* **`FormField`** — поля формы модального окна со значением, setter и getter.
* **`InvalidFormData`** — комбинации невалидных данных формы.
* **`ModalType`** — типы модальных окон.

Шаблонный код преобразования `Enum[] → Object[][]` вынесен в
**`utils.DataProviderUtils`** и вызывается из провайдеров
(`CalendarDataProvider`, `ModalDataProvider`), поэтому в самих провайдерах
нет дублирования циклов, а Stream API не используется.

## Тестовые сценарии

Количество запусков на один браузер (без retry):

| Страница | Позитивные | Негативные | Всего |
|----------|------------|------------|-------|
| Calendars | 9 | 7 | 16 |
| Modals | 10 | 5 | 15 |
| Ads | 8 | 3 | 11 |
| **Всего** | **27** | **15** | **42** |

### Calendars

#### Smoke — 2 теста

**`testCalendarsPageOpens`**

Проверяет открытие страницы Calendars, URL и заголовок страницы.

**`testDateFieldDisplayed`**

Проверяет отображение поля даты.

#### Quality — 4 теста (с учётом параметризации)

**`testValidDateCanBeEntered`**

Проверяет ввод корректной даты. Параметризован перечислением `CalendarDate`
(категория `VALID`): `2026-10-15`, `2026-12-15`, `2028-02-29`. Итого 3 запуска.

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

Известный баг: поле принимает невалидный формат. Параметризован перечислением
`CalendarDate` (категория `INVALID_FORMAT`): `15-10-2026`, `2026-02-30`,
`2026-13-10`. Итого 3 запуска.

**`testInvalidDateDoesNotProduceSuccessMessage`**

Проверяет, что невалидный формат не отправляет форму. 1 запуск.

**`testOutOfRangeDateAcceptedByInputField`**

Известный баг: поле принимает дату вне min (`1900-01-01`). 1 запуск.

**`testOutOfRangeDateBlocksSubmission`**

Проверяет, что дата вне диапазона не отправляет форму. 1 запуск.

**`testEmptyDateCannotBeSubmitted`**

Проверяет невозможность отправки формы без даты. 1 запуск.

**Всего Calendars: 16 запусков**

---

### Modals

#### Smoke — 3 теста

**`testModalsPageOpens`**

Проверяет открытие страницы Modals, URL и заголовок страницы.

**`testSimpleModalOpens`**

Проверяет открытие простого modal.

**`testFormModalOpens`**

Проверяет открытие modal с формой.

#### Quality — 5 запусков (3 метода, с учётом параметризации)

**`testSimpleModalText`**

Проверяет содержимое простого modal.

**`testSimpleModalCloses`**

Проверяет закрытие простого modal кнопкой Close.

**`testFormAcceptsFieldValue`**

Проверяет ввод значения в поле формы и фактическое значение поля.
Параметризован перечислением `FormField` (name, email, message) —
3 запуска. Каждая константа enum хранит значение, setter и getter поля.

#### Regression — 2 теста

**`testFormSubmission`** — ключевой тест формы из ТЗ.

Проверяет успешную отправку формы с корректными данными.

Поле **Message** заполняется по требованию ТЗ: средствами Selenium со страницы
[Form Fields](https://practice-automation.com/form-fields/) собирается список
элементов раздела **Automation Tools** (`FormFieldsPage.getAutomationToolsAsText()`),
преобразуется в строку через запятую и вставляется в поле Message формы модального
окна. Тест проверяет, что значение поля Message равно собранной строке, после чего
отправляет форму и проверяет сообщение об успехе.

> Примечание: на странице `/modals/` находится форма с полем Message, а список
> Automation Tools расположен на `/form-fields/`. Поэтому тест открывает обе
> страницы: собирает инструменты с Form Fields и заполняет форму модального окна.

**`testFormModalCloses`**

Проверяет закрытие modal с формой кнопкой Close.

#### Negative — 5 запусков (2 метода, с учётом параметризации)

**`testFormSubmittedWithInvalidData`**

Известный баг: форма принимает невалидные данные. Параметризован
перечислением `InvalidFormData` (имя/email/сообщение): пустое имя,
невалидный email, пустое сообщение. Итого 3 запуска.

**`testModalNotClosedByEscape`**

Проверяет, что modal не закрывается клавишей Escape. Параметризованный тест, 2 набора данных: SIMPLE, FORM. Итого 2 запуска.

**Всего Modals: 15 запусков**

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
