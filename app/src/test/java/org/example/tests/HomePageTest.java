package org.example.tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.example.pages.HomePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HomePageTest extends BaseSmi2Test {
  @Test
  @DisplayName("Главная страница содержит ленту главных новостей")
  void homePageContainsMainNewsFeed() {
    HomePage homePage = new HomePage(driver).open();

    assertTrue(homePage.hasLogo());
    assertTrue(homePage.mainTitle().contains("новости") || homePage.mainTitle().contains("НОВОСТИ"));
    assertFalse(homePage.newsLinks().isEmpty());
    assertTrue(homePage.hasSourcesBlock());
  }
}
