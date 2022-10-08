package com.tidal.wave.verification.criteria;

import com.tidal.wave.verification.conditions.Verification;
import org.openqa.selenium.By;

import java.util.List;
import java.util.function.Supplier;


public class EnsureElementState {

    //Class not to be instantiated.
    private EnsureElementState() {
    }

    public static void affirmation(boolean isVisible, boolean isMultiple, List<By> locatorSet, Supplier<Criteria>[] verifications) {
        for (Supplier<Criteria> verification : verifications) {
            verification.get().verify(isVisible, isMultiple, locatorSet);
        }
    }

    public static void affirmation(boolean isVisible, boolean isMultiple, List<By> locatorSet, Criteria... verifications) {
        for (Verification verification : verifications) {
            verification.verify(isVisible, isMultiple, locatorSet);
        }
    }
}
