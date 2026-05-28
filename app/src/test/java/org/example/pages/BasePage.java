package org.example.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BasePage {
  protected final WebDriver driver;
  protected final WebDriverWait wait;

  protected BasePage(WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(12));
  }

  protected WebElement visible(By locator) {
    return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
  }

  protected WebElement clickable(By locator) {
    return wait.until(ExpectedConditions.elementToBeClickable(locator));
  }

  protected boolean exists(By locator) {
    return !driver.findElements(locator).isEmpty();
  }

  protected String textOrTitle(By locator) {
    return exists(locator) ? visible(locator).getText() : driver.getTitle();
  }

  protected void click(By locator) {
    StaleElementReferenceException staleElement = null;
    for (int attempt = 0; attempt < 2; attempt++) {
      WebElement element;
      try {
        element = clickable(locator);
      } catch (TimeoutException exception) {
        element = driver.findElement(locator);
      }
      try {
        scrollIntoView(element);
        element.click();
        return;
      } catch (StaleElementReferenceException exception) {
        staleElement = exception;
      } catch (RuntimeException exception) {
        try {
          ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
          return;
        } catch (StaleElementReferenceException retryException) {
          staleElement = retryException;
        }
      }
    }
    throw staleElement;
  }

  protected void waitForUrlContains(String value) {
    wait.until(ExpectedConditions.urlContains(value));
  }

  protected void waitForReadyState() {
    ExpectedCondition<Boolean> ready = currentDriver -> "complete"
        .equals(((JavascriptExecutor) currentDriver).executeScript("return document.readyState"));
    wait.until(ready);
  }

  private void scrollIntoView(WebElement element) {
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
  }
}
