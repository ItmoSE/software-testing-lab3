package org.example.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TrafficExchangePage extends BasePage {
  private static final String DEFAULT_EXCHANGE_URL = "https://smi2.net/dashboard/exchange";

  private static final By PAGE_TITLE = By.xpath(
      "//*[contains(normalize-space(.), 'Traffic exchange') " +
          "or contains(normalize-space(.), 'Обмен трафиком')]");

  private static final By SITES_TAB = By.xpath(
      "//*[self::a or self::button or self::div or self::span or @role='tab' or @role='button']" +
          "[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'sites') "
          +
          "or contains(normalize-space(.), 'Площадки')]");

  private static final By ADD_SITE = By.xpath(
      "//*[self::a or self::button or self::div or self::span or @role='button']" +
          "[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'add site') "
          +
          "or contains(normalize-space(.), 'Добавить площадку') " +
          "or contains(normalize-space(.), 'Добавить сайт')]");

  private static final By FILTERS = By.xpath(
      "//*[contains(normalize-space(.), 'Filters') " +
          "or contains(normalize-space(.), 'Фильтры')]");

  private static final By SEVEN_DAYS = By.xpath(
      "//*[self::a or self::button or self::div or self::span or @role='button']" +
          "[contains(normalize-space(.), '7 days') " +
          "or contains(normalize-space(.), '7 дней')]");

  private static final By EXCHANGE_TYPE_RADIOS = By.xpath(
      "//*[contains(normalize-space(.), 'all exchange') " +
          "or contains(normalize-space(.), 'exchange through СМИ2') " +
          "or contains(normalize-space(.), 'direct exchange') " +
          "or contains(normalize-space(.), 'весь обмен') " +
          "or contains(normalize-space(.), 'обмен через СМИ2') " +
          "or contains(normalize-space(.), 'прямой обмен')]");

  private static final By TRAFFIC_STATS = By.xpath(
      "//*[contains(normalize-space(.), 'Incoming traffic') " +
          "or contains(normalize-space(.), 'Outgoing traffic') " +
          "or contains(normalize-space(.), 'Clicks') " +
          "or contains(normalize-space(.), 'Impressions') " +
          "or contains(normalize-space(.), 'CTR') " +
          "or contains(normalize-space(.), 'Входящий трафик') " +
          "or contains(normalize-space(.), 'Исходящий трафик') " +
          "or contains(normalize-space(.), 'Переходы') " +
          "or contains(normalize-space(.), 'Показы')]");

  private static final By CHART_OR_EMPTY_STATE = By.xpath(
      "//*[contains(normalize-space(.), 'No data') " +
          "or contains(normalize-space(.), 'by hour') " +
          "or contains(normalize-space(.), 'Clicks') " +
          "or contains(normalize-space(.), 'Impress.') " +
          "or contains(normalize-space(.), 'Нет данных') " +
          "or contains(normalize-space(.), 'по часам') " +
          "or contains(normalize-space(.), 'Переходы') " +
          "or contains(normalize-space(.), 'Показы')]");

  private static final By ADD_SITE_CONTEXT = By.xpath(
      "//*[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'add site') "
          +
          "or contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'site') "
          +
          "or contains(normalize-space(.), 'Добавить площадку') " +
          "or contains(normalize-space(.), 'Добавление площадки') " +
          "or contains(normalize-space(.), 'Площадка') " +
          "or contains(normalize-space(.), 'Сайт') " +
          "or contains(normalize-space(.), 'URL') " +
          "or contains(normalize-space(.), 'Domain') " +
          "or contains(normalize-space(.), 'Домен')]");

  private static final By FORM_FIELDS = By.xpath("//input | //textarea | //select");

  public TrafficExchangePage(WebDriver driver) {
    super(driver);
  }

  public TrafficExchangePage open() {
    driver.get(System.getProperty("exchangeUrl", DEFAULT_EXCHANGE_URL));
    waitForReadyState();

    try {
      wait.until(driver -> isOpened() || driver.getCurrentUrl().contains("/login"));
    } catch (TimeoutException exception) {
      printDebugInfo("TRAFFIC EXCHANGE OPEN FAILED");
      printVisibleTexts();
      throw exception;
    }

    if (driver.getCurrentUrl().contains("/login")) {
      printDebugInfo("TRAFFIC EXCHANGE REDIRECTED TO LOGIN");
      throw new AssertionError("Пользователь не авторизован: /dashboard/exchange перекинул на login");
    }

    return this;
  }

  public boolean isOpened() {
    return driver.getCurrentUrl().contains("/dashboard/exchange")
        && (exists(PAGE_TITLE)
            || exists(TRAFFIC_STATS)
            || exists(FILTERS)
            || exists(SITES_TAB)
            || exists(ADD_SITE));
  }

  public void selectSevenDaysPeriodIfAvailable() {
    if (!exists(SEVEN_DAYS)) {
      System.err.println("Период '7 дней / 7 days' не найден. Продолжаем тест без переключения периода.");
      return;
    }

    click(SEVEN_DAYS);
    waitForReadyState();
    wait.until(driver -> isOpened());
  }

  public void openSitesTabIfAvailable() {
    if (!exists(SITES_TAB)) {
      System.err.println("Вкладка 'Площадки / Sites' не найдена. Пробуем продолжить без переключения вкладки.");
      printVisibleTexts();
      return;
    }

    click(SITES_TAB);
    waitForReadyState();
  }

  public void clickAddSite() {
    if (!exists(ADD_SITE)) {
      printDebugInfo("ADD SITE BUTTON NOT FOUND");
      printVisibleTexts();
      throw new AssertionError("Не найдена кнопка 'Добавить площадку / Add site'");
    }

    click(ADD_SITE);
    waitForReadyState();

    try {
      wait.until(driver -> hasAddSiteContext());
    } catch (TimeoutException exception) {
      printDebugInfo("ADD SITE CONTEXT NOT FOUND AFTER CLICK");
      printVisibleTexts();
      throw exception;
    }
  }

  public boolean hasFilters() {
    return exists(FILTERS);
  }

  public boolean hasExchangeTypeSelector() {
    return exists(EXCHANGE_TYPE_RADIOS);
  }

  public boolean hasTrafficStats() {
    return exists(TRAFFIC_STATS);
  }

  public boolean hasChartOrEmptyState() {
    return exists(CHART_OR_EMPTY_STATE);
  }

  public boolean hasAddSiteAction() {
    return exists(ADD_SITE);
  }

  public boolean hasAddSiteContext() {
    return driver.getCurrentUrl().toLowerCase().contains("site")
        || driver.getCurrentUrl().toLowerCase().contains("platform")
        || exists(ADD_SITE_CONTEXT)
        || !formFields().isEmpty();
  }

  public List<WebElement> formFields() {
    return driver.findElements(FORM_FIELDS);
  }

  public String currentUrl() {
    return driver.getCurrentUrl();
  }

  public void printDebugInfo(String title) {
    System.err.println();
    System.err.println("====================================================");
    System.err.println(title);
    System.err.println("URL: " + driver.getCurrentUrl());
    System.err.println("TITLE: " + driver.getTitle());
    System.err.println("Opened: " + isOpened());
    System.err.println("Has page title: " + exists(PAGE_TITLE));
    System.err.println("Has filters: " + hasFilters());
    System.err.println("Has exchange type selector: " + hasExchangeTypeSelector());
    System.err.println("Has traffic stats: " + hasTrafficStats());
    System.err.println("Has chart or empty state: " + hasChartOrEmptyState());
    System.err.println("Has Sites tab: " + exists(SITES_TAB));
    System.err.println("Has Add site action: " + hasAddSiteAction());
    System.err.println("Has Add site context: " + hasAddSiteContext());
    System.err.println("Form fields: " + formFields().size());
    System.err.println("====================================================");
    System.err.println();
  }

  public void printVisibleTexts() {
    System.err.println();
    System.err.println("====================================================");
    System.err.println("VISIBLE TEXTS DEBUG");
    System.err.println("URL: " + driver.getCurrentUrl());
    System.err.println("TITLE: " + driver.getTitle());

    driver.findElements(By.xpath("//a | //button | //*[@role='button'] | //*[@role='tab'] | //div | //span"))
        .stream()
        .filter(WebElement::isDisplayed)
        .map(element -> element.getText() == null ? "" : element.getText().trim())
        .filter(text -> !text.isBlank())
        .distinct()
        .limit(120)
        .forEach(text -> System.err.println("- [" + text + "]"));

    System.err.println("====================================================");
    System.err.println();
  }
}
