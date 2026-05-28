package org.example.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class DashboardPage extends BasePage {
  private static final By GLOBAL_SEARCH = By.xpath(
      "//*[@id='input-global-search' " +
          "or //input[contains(@placeholder, 'поиск') or contains(@placeholder, 'Поиск')]]");

  private static final By USER_MENU = By.xpath(
      "//*[contains(@class, 'header-menu__toolbar-user') " +
          "or contains(@class, 'user') " +
          "or contains(@class, 'profile') " +
          "or contains(normalize-space(.), 'Профиль') " +
          "or contains(normalize-space(.), 'Кабинет')]");

  private static final By LOGOUT = By.xpath(
      "//*[contains(normalize-space(.), 'Выйти') " +
          "or contains(normalize-space(.), 'Выход') " +
          "or contains(@href, 'logout') " +
          "or contains(@href, 'signout')]");

  private static final By NAVIGATION_LINKS = By.xpath(
      "//a[@href and normalize-space(string())]");

  private static final By BUTTONS = By.xpath(
      "//button[normalize-space(string())]");

  private static final By FORMS = By.xpath("//form");

  private static final By DASHBOARD_TEXT = By.xpath(
      "//*[contains(normalize-space(.), 'Кабинет') " +
          "or contains(normalize-space(.), 'Профиль') " +
          "or contains(normalize-space(.), 'Настройки') " +
          "or contains(normalize-space(.), 'Статистика') " +
          "or contains(normalize-space(.), 'Dashboard')]");

  public DashboardPage(WebDriver driver) {
    super(driver);
  }

  public boolean isOpened() {
    return !driver.getCurrentUrl().contains("/login")
        && (exists(USER_MENU)
            || exists(GLOBAL_SEARCH)
            || exists(DASHBOARD_TEXT)
            || !navigationLinks().isEmpty());
  }

  public boolean hasUserMenu() {
    return exists(USER_MENU);
  }

  public boolean hasGlobalSearch() {
    return exists(GLOBAL_SEARCH);
  }

  public boolean hasLogoutAction() {
    return exists(LOGOUT);
  }

  public boolean hasDashboardContext() {
    return exists(DASHBOARD_TEXT)
        || driver.getCurrentUrl().contains("dashboard")
        || driver.getCurrentUrl().contains("profile")
        || driver.getCurrentUrl().contains("cabinet");
  }

  public List<WebElement> navigationLinks() {
    return driver.findElements(NAVIGATION_LINKS);
  }

  public List<WebElement> buttons() {
    return driver.findElements(BUTTONS);
  }

  public List<WebElement> forms() {
    return driver.findElements(FORMS);
  }

  public void printPageSnapshot() {
    System.out.println();
    System.out.println("====================================================");
    System.out.println("AUTHORIZED PAGE SNAPSHOT");
    System.out.println("URL: " + driver.getCurrentUrl());
    System.out.println("TITLE: " + driver.getTitle());
    System.out.println("Has user menu: " + hasUserMenu());
    System.out.println("Has global search: " + hasGlobalSearch());
    System.out.println("Has logout action: " + hasLogoutAction());
    System.out.println("Has dashboard context: " + hasDashboardContext());
    System.out.println("Navigation links: " + navigationLinks().size());
    System.out.println("Buttons: " + buttons().size());
    System.out.println("Forms: " + forms().size());
    System.out.println("====================================================");

    System.out.println();
    System.out.println("Links:");
    navigationLinks().stream()
        .limit(30)
        .forEach(link -> {
          String text = link.getText();
          String href = link.getAttribute("href");
          System.out.println("- " + text + " -> " + href);
        });

    System.out.println();
    System.out.println("Buttons:");
    buttons().stream()
        .limit(30)
        .forEach(button -> System.out.println("- " + button.getText()));

    System.out.println("====================================================");
    System.out.println();
  }
}
