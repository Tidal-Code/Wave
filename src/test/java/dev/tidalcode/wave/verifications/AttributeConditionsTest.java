package dev.tidalcode.wave.verifications;

import com.tidal.utils.filehandlers.Finder;
import dev.tidalcode.wave.browser.Browser;
import dev.tidalcode.wave.exceptions.TestAssertionError;
import dev.tidalcode.wave.verification.conditions.Condition;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeOptions;

import static dev.tidalcode.wave.webelement.ElementFinder.find;


public class AttributeConditionsTest {


    @Before
    public void initialize() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--remote-allow-origins=*");
        Browser.withOptions(options).open("file://" + Finder.findFilePath("components/checkbox/checkbox.html"));
    }

    @After
    public void terminate() {
        Browser.close();
    }

    @Test
    public void testAttributeName() {
        find("id:h2_title").shouldHave(Condition.attributeAndValue("name", "title"));
    }

    @Test(expected = TestAssertionError.class)
    public void testAttributeNameFail() {
        find("id:h2_title").shouldHave(Condition.attributeAndValue("name", "x_y_z"));
    }

    @Test
    public void testAttributeWithoutValue() {
        find("id:checked_checkbox").shouldHave(Condition.attribute("checked"));
    }

    @Test
    public void testAttributeWithoutValueForDataModelTarget() {
        find("id:checked_checkbox").shouldHave(Condition.attribute("data-modal-target"));
    }

    @Test(expected = TestAssertionError.class)
    public void testAttributeWithoutValueFail() {
        find("id:test_checkbox_id").shouldHave(Condition.attribute("checked"));
    }

    @Test(expected = TestAssertionError.class)
    public void testAttributeWithoutValueForDataModelTargetFail() {
        find("id:test_checkbox_id").shouldHave(Condition.attribute("data-modal-target"));
    }
}
