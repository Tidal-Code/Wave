package com.tidal.wave.browser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;


class Firefox {
    private static final Logger logger = LogManager.getLogger(Firefox.class);
    public WebDriver getDriver(FirefoxOptions options) {
        logger.info("Test Starting with Firefox Browser");
        if(options == null){
            return new FirefoxDriver();
        }
        return new FirefoxDriver(options);
    }
}
