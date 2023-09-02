package com.tidal.wave.verification.expectations;

import com.tidal.wave.command.Executor;

import java.util.List;

public class SoftAssertion {

    private SoftAssertion() {
    }

    public static Expectation softAssert(Executor executor, Expectation expectation) {
        expectation.assertion(executor);
        return expectation;
    }
}
