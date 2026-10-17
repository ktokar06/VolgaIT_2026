package org.example.dataprovider;

/**
 * Данные тестируемых страниц: ключ URL в конфигурации, ожидаемая часть
 * адреса и ожидаемый заголовок.
 *
 * <p>Используется в smoke-тестах для проверки открытия страниц без
 * «магических» строк в самих тестах.</p>
 */
public enum PageInfo {
    CALENDARS("calendars.url", "/calendars/", "Calendars"),
    MODALS("modals.url", "/modals/", "Modals"),
    ADS("ads.url", "/ads/", "Ads");

    private final String urlKey;
    private final String urlPart;
    private final String title;

    PageInfo(String urlKey, String urlPart, String title) {
        this.urlKey = urlKey;
        this.urlPart = urlPart;
        this.title = title;
    }

    public String urlKey() {
        return urlKey;
    }

    public String urlPart() {
        return urlPart;
    }

    public String title() {
        return title;
    }
}
