package org.example.config;

public enum Browser {
  CHROME,
  CHROMIUM,
  FIREFOX;

  public static Browser fromProperty() {
    String value = System.getProperty("browser", "chrome").trim().toUpperCase();
    if ("GOOGLE_CHROME".equals(value)) {
      return CHROME;
    }
    return Browser.valueOf(value);
  }
}
