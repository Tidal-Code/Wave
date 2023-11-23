package com.tidal.wave.retry;

import com.tidal.utils.filehandlers.Finder;
import com.tidal.wave.verification.criteria.Criteria;
import com.tidal.wave.browser.Browser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeOptions;

import static com.tidal.wave.webelement.ElementFinder.find;


public class StillPresentRetryTests {

    @Before
    public void initialize() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--remote-allow-origins=*");
        Browser.withOptions(options).open("file://" + Finder.findFilePath("components/timeout/stillPresent.html"));
    }

    @After
    public void terminate() {
        Browser.close();
    }

    @Test
    public void retryTestIfVisible() {
        find("#textInput").clear().sendKeys("Retry test").clear().sendKeys("QA").retryIf(RetryCondition.stillPresent, 3);
        find("#textInput").shouldBe(Criteria.notPresent);
    }

}
