package dev.tidalcode.wave.retry;

import com.tidal.utils.filehandlers.Finder;
import dev.tidalcode.wave.browser.Browser;
import dev.tidalcode.wave.verification.criteria.Criteria;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeOptions;

import static dev.tidalcode.wave.webelement.ElementFinder.find;


public class RetryTests {

    @Before
    public void initialize() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--remote-allow-origins=*");
        Browser.withOptions(options).open("file://" + Finder.findFilePath("components/timeout/timeoutElements.html"));
    }

    @After
    public void terminate() {
        Browser.close();
    }

    @Test
    public void retryTestIfVisible() {
        find("#textInput").clear().sendKeys("Retry test").clear().sendKeys("QA").retryIf(RetryCondition.stillVisible, 3);
        find("#textInput").shouldBe(Criteria.notVisible);
    }

    @Test
    public void retryTestIfNotVisible() {
        find("#textInput2").clear().click().sendKeys("Retry test").retryIf(RetryCondition.notVisible("#buttonId"), 8);
        find("#buttonId").shouldBe(Criteria.visible);
    }

}
