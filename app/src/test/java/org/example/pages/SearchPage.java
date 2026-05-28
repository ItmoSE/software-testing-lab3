package org.example.pages;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SearchPage extends BasePage {
  private static final By SEARCH_FIELD = By.xpath(
      "//input[@type='search' or @name='q' or @name='query' or contains(@placeholder, 'поиск') or contains(@placeholder, 'Поиск')]");
  private static final By SEARCH_TEXT = By
      .xpath("//*[contains(normalize-space(.), 'поиск') or contains(normalize-space(.), 'Поиск')]");
  private static final By RESULT_LINKS = By
      .xpath("//a[normalize-space(string()) and @href and not(@href='/') and not(contains(@href, 'dashboard/login'))]");

  public SearchPage(WebDriver driver) {
    super(driver);
  }

  public SearchPage open(String query) {
    driver.get(System.getProperty("baseUrl", "https://smi2.ru/") + "search?q="
        + URLEncoder.encode(query, StandardCharsets.UTF_8));
    waitForReadyState();
    return this;
  }

  public boolean hasSearchInterface() {
    return exists(SEARCH_FIELD) || exists(SEARCH_TEXT);
  }

  public List<WebElement> resultLinks() {
    return driver.findElements(RESULT_LINKS);
  }

  public void openFirstResult() {
    click(RESULT_LINKS);
    waitForReadyState();
  }
}
