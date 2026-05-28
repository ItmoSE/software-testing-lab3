package org.example.tests;

import org.example.pages.AuthenticationPage;
import org.example.pages.TrafficExchangePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TrafficExchangeUseCaseTest extends BaseSmi2Test {
  private static final String VALID_LOGIN = System.getProperty("auth.login");
  private static final String VALID_PASSWORD = System.getProperty("auth.password");

  @Test
  @DisplayName("Авторизованный пользователь смотрит статистику и начинает добавление сайта в Traffic exchange")
  void authorizedUserCanViewTrafficStatsAndStartAddingSite() {
    AuthenticationPage authenticationPage = new AuthenticationPage(driver).open();

    authenticationPage.loginWithOptionalManualCaptcha(VALID_LOGIN, VALID_PASSWORD);

    TrafficExchangePage exchangePage = new TrafficExchangePage(driver).open();

    assertTrue(exchangePage.isOpened());

    exchangePage.selectSevenDaysPeriodIfAvailable();

    assertTrue(exchangePage.hasTrafficStats() || exchangePage.hasChartOrEmptyState());

    exchangePage.openSitesTabIfAvailable();

    assertTrue(
        exchangePage.hasAddSiteAction(),
        "После открытия Traffic exchange должна быть доступна возможность Добавить площадку / Add site");

    exchangePage.clickAddSite();

    exchangePage.printDebugInfo("TRAFFIC EXCHANGE USE CASE RESULT");

    assertTrue(exchangePage.hasAddSiteContext());
  }
}
