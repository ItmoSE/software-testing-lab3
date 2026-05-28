package org.example.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.example.pages.SearchPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SearchTest extends BaseSmi2Test {
  @Test
  @DisplayName("Пользователь открывает страницу поиска")
  void userCanOpenSearchPage() {
    SearchPage searchPage = new SearchPage(driver).open("спорт");

    assertTrue(driver.getCurrentUrl().contains("search"));
    assertTrue(searchPage.hasSearchInterface());
  }
}
