package org.example.tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.example.pages.HomePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RubricNavigationTest extends BaseSmi2Test {
  @Test
  @DisplayName("Пользователь переходит в рубрику Спорт")
  void userCanOpenSportRubric() {
    HomePage homePage = new HomePage(driver).open();

    homePage.openRubric("Спорт");

    assertTrue(driver.getCurrentUrl().contains("/tematiks/16"));
    assertFalse(homePage.newsLinks().isEmpty());
  }

  @Test
  @DisplayName("Пользователь открывает дополнительные рубрики")
  void userCanOpenAdditionalRubrics() {
    HomePage homePage = new HomePage(driver).open();

    homePage.openMoreMenu();

    assertTrue(
        homePage.hasMoreMenu() || homePage.hasExpandedRubric("Культура") || homePage.hasExpandedRubric("Экономика"));
  }
}
