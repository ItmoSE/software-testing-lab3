package org.example.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.example.pages.AuthenticationPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthenticationTest extends BaseSmi2Test {
  // dont worry, these creds are long gone (at least the mail, so i dont care
  // about the fact that they're in source code)
  private static final String VALID_LOGIN = System.getProperty("auth.login", "Tibisay.Mifflin@AllWebEmails.com");
  private static final String VALID_PASSWORD = System.getProperty("auth.password", "asHngu2bcubs");

  @Test
  @DisplayName("Пользователь открывает форму аутентификации")
  void userCanOpenAuthenticationForm() {
    AuthenticationPage authenticationPage = new AuthenticationPage(driver).open();

    assertTrue(authenticationPage.hasLoginForm());
  }

  @Test
  @DisplayName("Пользователь не входит с неверными учетными данными")
  void userCannotLoginWithInvalidCredentials() {
    AuthenticationPage authenticationPage = new AuthenticationPage(driver).open();

    authenticationPage.submitCredentials("invalid-user@example.com", "invalid-password");

    assertTrue(authenticationPage.showsAuthenticationContext());
  }

  @Test
  @DisplayName("Пользователь входит с действующими учетными данными")
  void userCanLoginWithValidCredentials() {
    AuthenticationPage authenticationPage = new AuthenticationPage(driver).open();

    authenticationPage.loginWithOptionalManualCaptcha(VALID_LOGIN, VALID_PASSWORD);

    assertTrue(authenticationPage.isAuthenticated()
        || !driver.getCurrentUrl().contains("/login"));
  }

  @Test
  @DisplayName("Пользователь открывает восстановление пароля из формы входа")
  void userCanOpenPasswordRecoveryFromAuthenticationForm() {
    AuthenticationPage authenticationPage = new AuthenticationPage(driver).open();

    authenticationPage.openRecovery();

    assertTrue(authenticationPage.showsRecoveryPage());
  }

  @Test
  @DisplayName("Пользователь открывает регистрацию из формы входа")
  void userCanOpenSignupFromAuthenticationForm() {
    AuthenticationPage authenticationPage = new AuthenticationPage(driver).open();

    authenticationPage.openSignup();

    assertTrue(authenticationPage.showsSignupPage());
  }
}
