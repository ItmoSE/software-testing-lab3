package org.example.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public final class DriverFactory {
  private DriverFactory() {
  }

  public static WebDriver createDriver() {
    Browser browser = Browser.fromProperty();
    String remoteUrl = System.getProperty("selenium.remoteUrl", "").trim();
    WebDriver driver = remoteUrl.isBlank()
        ? createLocalDriver(browser)
        : createRemoteDriver(remoteUrl, capabilities(browser));
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
    driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(40));
    driver.manage().window().maximize();
    return driver;
  }

  private static WebDriver createLocalDriver(Browser browser) {
    return switch (browser) {
      case CHROME, CHROMIUM -> {
        configureLocalChromium();
        yield new ChromeDriver(chromeOptions());
      }
      case FIREFOX -> {
        WebDriverManager.firefoxdriver().setup();
        yield new FirefoxDriver(firefoxOptions());
      }
    };
  }

  private static WebDriver createRemoteDriver(String remoteUrl, Capabilities capabilities) {
    try {
      return new RemoteWebDriver(URI.create(remoteUrl).toURL(), capabilities);
    } catch (MalformedURLException exception) {
      throw new IllegalArgumentException("Invalid Selenium remote URL: " + remoteUrl, exception);
    }
  }

  private static Capabilities capabilities(Browser browser) {
    return switch (browser) {
      case CHROME, CHROMIUM -> chromeOptions();
      case FIREFOX -> firefoxOptions();
    };
  }

  private static ChromeOptions chromeOptions() {
    ChromeOptions options = new ChromeOptions();
    options.setPageLoadStrategy(PageLoadStrategy.EAGER);
    findExecutable("/usr/bin/chromium", "/usr/sbin/chromium", "/usr/bin/google-chrome")
        .ifPresent(path -> options.setBinary(path.toString()));
    options.addArguments("--disable-notifications", "--disable-popup-blocking");
    return options;
  }

  private static FirefoxOptions firefoxOptions() {
    FirefoxOptions options = new FirefoxOptions();
    options.setPageLoadStrategy(PageLoadStrategy.EAGER);
    options.addPreference("dom.webnotifications.enabled", false);
    return options;
  }

  private static void configureLocalChromium() {
    findExecutable("/usr/bin/chromedriver", "/usr/local/bin/chromedriver")
        .ifPresentOrElse(
            path -> System.setProperty("webdriver.chrome.driver", path.toString()),
            () -> WebDriverManager.chromedriver().setup());
  }

  private static java.util.Optional<Path> findExecutable(String... candidates) {
    for (String candidate : candidates) {
      Path path = Path.of(candidate);
      if (Files.isExecutable(path)) {
        return java.util.Optional.of(path);
      }
    }
    return java.util.Optional.empty();
  }
}
