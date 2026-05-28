package org.example.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomePage extends BasePage {
  private static final By LOGO = By.xpath("//img[contains(@alt,'logo') or contains(@src,'logo')]");
  private static final By MAIN_TITLE = By
      .xpath("//*[contains(normalize-space(.), 'ГЛАВНЫЕ НОВОСТИ') or contains(normalize-space(.), 'Главные новости')]");
  private static final By NEWS_LINKS = By
      .xpath("//a[normalize-space(string()) and @href and not(@href='/') and not(contains(@href, 'dashboard/login'))]");
  private static final By SOURCES = By.xpath("//*[normalize-space()='Источники']");
  private static final By DISCUSS_LINK = By
      .xpath("//a[contains(normalize-space(.), 'Обсудить') or contains(normalize-space(.), 'Комментарии')]");
  private static final By MORE_MENU = By
      .xpath("//*[normalize-space()='Eщё' or normalize-space()='Ещё' or normalize-space()='Еще']");
  private static final By THEME_SWITCH = By
      .xpath("//*[contains(normalize-space(.), 'Светлая тема') or contains(normalize-space(.), 'Темная тема')]");
  private static final By STOP_NEGATIVE = By.xpath("//*[contains(normalize-space(.), 'Стоп негатив')]");

  public HomePage(WebDriver driver) {
    super(driver);
  }

  public HomePage open() {
    try {
      driver.get(System.getProperty("baseUrl", "https://smi2.ru/"));
    } catch (TimeoutException exception) {
      ((JavascriptExecutor) driver).executeScript("window.stop();");
    }
    waitForReadyState();
    return this;
  }

  public boolean hasLogo() {
    return exists(LOGO);
  }

  public String mainTitle() {
    return textOrTitle(MAIN_TITLE);
  }

  public List<WebElement> newsLinks() {
    return driver.findElements(NEWS_LINKS);
  }

  public boolean hasSourcesBlock() {
    return exists(SOURCES);
  }

  public boolean hasDiscussEntryPoint() {
    return exists(DISCUSS_LINK);
  }

  public void openRubric(String rubricName) {
    By rubric = By.xpath("//a[normalize-space()='" + rubricName + "' or .//*[normalize-space()='" + rubricName + "']]");
    String href = driver.findElement(rubric).getAttribute("href");
    if (href == null || href.isBlank()) {
      click(rubric);
    } else {
      driver.get(href);
    }
    waitForReadyState();
  }

  public void openFirstNews() {
    click(NEWS_LINKS);
    waitForReadyState();
  }

  public void openMoreMenu() {
    click(MORE_MENU);
  }

  public boolean hasMoreMenu() {
    return exists(MORE_MENU);
  }

  public boolean hasExpandedRubric(String rubricName) {
    return exists(By.xpath("//a[normalize-space()='" + rubricName + "']"));
  }

  public void toggleTheme() {
    click(THEME_SWITCH);
  }

  public boolean hasThemeSwitch() {
    return exists(THEME_SWITCH);
  }

  public boolean hasStopNegativeSwitch() {
    return exists(STOP_NEGATIVE);
  }
}
