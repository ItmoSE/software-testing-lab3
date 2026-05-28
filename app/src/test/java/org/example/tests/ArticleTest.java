package org.example.tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.example.pages.ArticlePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ArticleTest extends BaseSmi2Test {
  @Test
  @DisplayName("Пользователь открывает новость и видит действия статьи")
  void userCanOpenNewsArticle() {
    driver.get(System.getProperty("baseUrl", "https://smi2.ru/") + "article/172000441");
    ArticlePage articlePage = new ArticlePage(driver);

    assertTrue(driver.getCurrentUrl().contains("/article/"));
    assertFalse(articlePage.title().isBlank());
    assertTrue(articlePage.hasReadMoreAction() || articlePage.hasCommentsAction());
  }
}
