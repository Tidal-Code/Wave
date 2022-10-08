package com.tidal.wave.verification.expectations;

import com.tidal.wave.verification.expectations.collections.Expectations;
import org.openqa.selenium.By;

import java.util.List;

public class CollectionsSoftAssertion {

    private CollectionsSoftAssertion() {
    }

    public static Expectations softAssert(boolean isMultiple, List<By> locatorSet, Expectations expectations) {
        expectations.assertion(isMultiple, locatorSet);
        return expectations;
    }
}
