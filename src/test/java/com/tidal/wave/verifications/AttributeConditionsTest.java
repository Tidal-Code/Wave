package com.tidal.wave.verifications;

import com.tidal.utils.filehandlers.Finder;
import com.tidal.wave.browser.Browser;
import com.tidal.wave.exceptions.TestAssertionError;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeOptions;

import static com.tidal.wave.verification.conditions.Condition.attribute;
import static com.tidal.wave.verification.conditions.Condition.attributeAndValue;
import static com.tidal.wave.webelement.ElementFinder.find;


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
        find("id:h2_title").shouldHave(attributeAndValue("name", "title"));
    }

    @Test(expected = TestAssertionError.class)
    public void testAttributeNameFail() {
        find("id:h2_title").shouldHave(attributeAndValue("name", "x_y_z"));
    }

    @Test
    public void testAttributeWithoutValue() {
        find("id:checked_checkbox").shouldHave(attribute("checked"));
    }

    @Test
    public void testAttributeWithoutValueForDataModelTarget() {
        find("id:checked_checkbox").shouldHave(attribute("data-modal-target"));
    }

    @Test(expected = TestAssertionError.class)
    public void testAttributeWithoutValueFail() {
        find("id:test_checkbox_id").shouldHave(attribute("checked"));
    }

    @Test(expected = TestAssertionError.class)
    public void testAttributeWithoutValueForDataModelTargetFail() {
        find("id:test_checkbox_id").shouldHave(attribute("data-modal-target"));
    }
}
