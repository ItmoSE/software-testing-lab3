package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ArticlePage extends BasePage {
  private static final By ARTICLE_TITLE = By.xpath("//h1[normalize-space()]");
  private static final By READ_MORE = By
      .xpath("//a[contains(normalize-space(.), 'Читать') or contains(normalize-space(.), 'К источнику')]");
  private static final By COMMENTS = By
      .xpath("//*[contains(normalize-space(.), 'Комментарии') or contains(normalize-space(.), 'Обсудить')]");

  public ArticlePage(WebDriver driver) {
    super(driver);
  }

  public String title() {
    return textOrTitle(ARTICLE_TITLE);
  }

  public boolean hasReadMoreAction() {
    return exists(READ_MORE);
  }

  public boolean hasCommentsAction() {
    return exists(COMMENTS);
  }
}
