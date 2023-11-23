package dev.tidalcode.wave.commands;

import com.tidal.utils.filehandlers.Finder;
import dev.tidalcode.wave.exceptions.ExpectationFailure;
import dev.tidalcode.wave.verification.expectations.Expectation;
import dev.tidalcode.wave.browser.Browser;
import dev.tidalcode.wave.webelement.ElementFinder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeOptions;

import static dev.tidalcode.wave.webelement.ElementFinder.find;

public class IsVisibleTest {

    @Before
    public void initialize() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--remote-allow-origins=*");

        Browser.withOptions(options).open("file://" + Finder.findFilePath("components/elements/elements.html"));
    }

    @After
    public void terminate() {
        Browser.close();
    }

    @Test
    public void isPresentTest() {
        Assert.assertTrue(ElementFinder.findAll("title:hidden").isPresent());
    }

    @Test(expected = ExpectationFailure.class)
    public void interactionExpectationShouldFail() {
        ElementFinder.find("title:hidden").invisibleElement().expecting(Expectation.toBeInteractable).orElseFail();
    }

    @Test(expected = AssertionError.class)
    public void displayTestShouldFail() {
        Assert.assertTrue(ElementFinder.find("name:display_test").isDisplayed());
    }


}
