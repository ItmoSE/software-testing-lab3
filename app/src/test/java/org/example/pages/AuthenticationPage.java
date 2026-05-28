package org.example.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AuthenticationPage extends BasePage {
  private static final String DEFAULT_AUTH_URL = "https://smi2.net/dashboard/login";

  private static final By LOGIN_ENTRY = By.xpath(
      "//a[contains(@href, 'dashboard/login') and (contains(normalize-space(.), 'Войти') or contains(@href, 'smi2.net'))]");

  private static final By LOGIN_FIELD = By.xpath(
      "//input[" +
          "not(@type='hidden') and (" +
          "@type='email' " +
          "or @name='email' " +
          "or @name='login' " +
          "or @name='username' " +
          "or contains(translate(@placeholder, 'ABCDEFGHIJKLMNOPQRSTUVWXYZАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ', 'abcdefghijklmnopqrstuvwxyzабвгдеёжзийклмнопрстуфхцчшщъыьэюя'), 'mail') "
          +
          "or contains(translate(@placeholder, 'ABCDEFGHIJKLMNOPQRSTUVWXYZАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ', 'abcdefghijklmnopqrstuvwxyzабвгдеёжзийклмнопрстуфхцчшщъыьэюя'), 'логин') "
          +
          "or contains(translate(@placeholder, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'email') " +
          ")" +
          "]");

  private static final By PASSWORD_FIELD = By.xpath(
      "//input[not(@type='hidden') and (@type='password' or @name='password')]");

  private static final By SUBMIT_BUTTON = By.xpath(
      "//form//button[not(@disabled)] " +
          "| //button[@type='submit' " +
          "or contains(normalize-space(.), 'Войти') " +
          "or contains(normalize-space(.), 'Вход') " +
          "or contains(normalize-space(.), 'Sign') " +
          "or contains(normalize-space(.), 'Login')]");

  private static final By AUTH_TEXT = By.xpath(
      "//*[contains(normalize-space(.), 'Войти') " +
          "or contains(normalize-space(.), 'Authorization') " +
          "or contains(normalize-space(.), 'Login') " +
          "or contains(normalize-space(.), 'E-mail') " +
          "or contains(normalize-space(.), 'Email')]");

  private static final By AUTH_ERROR = By.xpath(
      "//*[contains(normalize-space(.), 'ошиб') " +
          "or contains(normalize-space(.), 'невер') " +
          "or contains(normalize-space(.), 'Невер') " +
          "or contains(normalize-space(.), 'invalid') " +
          "or contains(normalize-space(.), 'Invalid')]");

  private static final By VERIFICATION_FIELD = By.xpath("//input[@name='verificationCode']");

  private static final By CAPTCHA = By.xpath(
      "//*[contains(@class, 'captcha') or contains(@class, 'recaptcha') or @vc-recaptcha] " +
          "| //iframe[contains(@src, 'recaptcha')]");

  private static final By RECOVERY_LINK = By.xpath(
      "//a[contains(normalize-space(.), 'Забыли пароль') or contains(@href, 'recovery')]");

  private static final By SIGNUP_LINK = By.xpath(
      "//a[contains(normalize-space(.), 'Зарегистрироваться') or contains(@href, 'signup')]");

  private static final By RECOVERY_TEXT = By.xpath(
      "//*[contains(normalize-space(.), 'Восстановление пароля') or contains(normalize-space(.), 'восстановления пароля')]");

  private static final By SIGNUP_TEXT = By.xpath(
      "//*[contains(normalize-space(.), 'Регистрация') or contains(normalize-space(.), 'Зарегистрироваться')]");

  public AuthenticationPage(WebDriver driver) {
    super(driver);
  }

  public AuthenticationPage open() {
    driver.get(System.getProperty("authUrl", DEFAULT_AUTH_URL));
    waitForReadyState();

    try {
      wait.until(
          driver -> hasLoginForm() || isAuthenticated() || showsAntiBotChallenge() || showsAuthenticationContext());
    } catch (TimeoutException exception) {
      printDebugInfo("AUTH PAGE OPEN FAILED");
      throw exception;
    }

    return this;
  }

  public AuthenticationPage openFromHomePage() {
    String href = visible(LOGIN_ENTRY).getAttribute("href");
    if (href == null || href.isBlank()) {
      click(LOGIN_ENTRY);
    } else {
      driver.get(href);
    }

    waitForReadyState();

    wait.until(
        driver -> hasLoginForm() || isAuthenticated() || showsAntiBotChallenge() || showsAuthenticationContext());

    return this;
  }

  public boolean hasLoginForm() {
    return exists(LOGIN_FIELD) && exists(PASSWORD_FIELD) && exists(SUBMIT_BUTTON);
  }

  public void submitCredentials(String login, String password) {
    if (isAuthenticated()) {
      return;
    }

    if (login == null || login.isBlank() || password == null || password.isBlank()) {
      throw new IllegalArgumentException(
          "Передай логин и пароль через -Dauth.login=\"...\" -Dauth.password=\"...\"");
    }

    type(LOGIN_FIELD, login);
    type(PASSWORD_FIELD, password);

    try {
      click(SUBMIT_BUTTON);
    } catch (StaleElementReferenceException exception) {
      click(SUBMIT_BUTTON);
    }

    waitForReadyState();
  }

  public AuthenticationPage login(String login, String password) {
    submitCredentials(login, password);

    wait.until(driver -> isAuthenticated()
        || showsAuthenticationError()
        || exists(VERIFICATION_FIELD));

    return this;
  }

  public AuthenticationPage loginWithOptionalManualCaptcha(String login, String password) {
    submitCredentials(login, password);

    long captchaTimeoutSeconds = Long.getLong("auth.captchaTimeoutSeconds", 180);
    long loginTimeoutSeconds = Long.getLong("auth.loginTimeoutSeconds", 60);

    try {
      new WebDriverWait(driver, Duration.ofSeconds(5))
          .until(driver -> isAuthenticated() || showsAntiBotChallenge() || showsAuthenticationError());
    } catch (TimeoutException ignored) {
      // Ничего страшного: дальше будет основное ожидание.
    }

    if (showsAntiBotChallenge()) {
      System.out.println();
      System.out.println("====================================================");
      System.out.println("Обнаружена CAPTCHA.");
      System.out.println("Пройди её вручную в открытом окне браузера.");
      System.out.println("Если после CAPTCHA нужно нажать кнопку входа — нажми её руками.");
      System.out.println("Тест будет ждать до " + captchaTimeoutSeconds + " секунд.");
      System.out.println("====================================================");
      System.out.println();

      new WebDriverWait(driver, Duration.ofSeconds(captchaTimeoutSeconds))
          .until(driver -> isAuthenticated());
    }

    try {
      new WebDriverWait(driver, Duration.ofSeconds(loginTimeoutSeconds))
          .until(driver -> isAuthenticated());
    } catch (TimeoutException exception) {
      printDebugInfo("LOGIN FAILED");
      throw exception;
    }

    return this;
  }

  public void openRecovery() {
    click(RECOVERY_LINK);
    waitForReadyState();
    wait.until(driver -> driver.getCurrentUrl().contains("recovery") || exists(RECOVERY_TEXT));
  }

  public void openSignup() {
    click(SIGNUP_LINK);
    waitForReadyState();
    wait.until(driver -> driver.getCurrentUrl().contains("signup") || exists(SIGNUP_TEXT));
  }

  public boolean isAuthenticated() {
    return driver.getCurrentUrl().contains("/dashboard")
        && !driver.getCurrentUrl().contains("/dashboard/login")
        && !driver.getCurrentUrl().contains("/login");
  }

  public boolean showsAuthenticationError() {
    return exists(AUTH_ERROR) || exists(VERIFICATION_FIELD) || exists(CAPTCHA);
  }

  public boolean showsAntiBotChallenge() {
    return exists(CAPTCHA);
  }

  public boolean showsRecoveryPage() {
    return driver.getCurrentUrl().contains("recovery") || exists(RECOVERY_TEXT);
  }

  public boolean showsSignupPage() {
    return driver.getCurrentUrl().contains("signup") || exists(SIGNUP_TEXT);
  }

  public boolean showsAuthenticationContext() {
    return driver.getCurrentUrl().contains("login")
        || exists(AUTH_TEXT)
        || exists(AUTH_ERROR)
        || exists(LOGIN_FIELD)
        || exists(PASSWORD_FIELD);
  }

  private void type(By locator, String value) {
    try {
      WebElement input = visible(locator);
      input.clear();
      input.sendKeys(value);
    } catch (StaleElementReferenceException exception) {
      WebElement input = visible(locator);
      input.clear();
      input.sendKeys(value);
    }
  }

  private void printDebugInfo(String title) {
    System.out.println();
    System.out.println("====================================================");
    System.out.println(title);
    System.out.println("URL: " + driver.getCurrentUrl());
    System.out.println("TITLE: " + driver.getTitle());
    System.out.println("Has login field: " + exists(LOGIN_FIELD));
    System.out.println("Has password field: " + exists(PASSWORD_FIELD));
    System.out.println("Has submit button: " + exists(SUBMIT_BUTTON));
    System.out.println("Has login form: " + hasLoginForm());
    System.out.println("Is authenticated: " + isAuthenticated());
    System.out.println("Shows auth context: " + showsAuthenticationContext());
    System.out.println("Shows auth error: " + showsAuthenticationError());
    System.out.println("Shows captcha: " + showsAntiBotChallenge());
    System.out.println("====================================================");
    System.out.println();
  }
}
