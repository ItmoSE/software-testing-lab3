package org.example.tests;

import org.example.config.DriverFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;

abstract class BaseSmi2Test {
  protected WebDriver driver;

  @BeforeEach
  void setUp() {
    driver = DriverFactory.createDriver();
  }

  @AfterEach
  void tearDown() {
    if (driver != null) {
      driver.quit();
    }
  }
}
