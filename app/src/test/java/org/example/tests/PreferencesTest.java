package org.example.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.example.pages.HomePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PreferencesTest extends BaseSmi2Test {
  @Test
  @DisplayName("Пользователь видит настройки фильтрации и темы")
  void userCanSeePreferenceControls() {
    HomePage homePage = new HomePage(driver).open();

    assertTrue(homePage.hasThemeSwitch());
    assertTrue(homePage.hasStopNegativeSwitch());
    homePage.toggleTheme();
    assertTrue(homePage.hasThemeSwitch());
  }
}
