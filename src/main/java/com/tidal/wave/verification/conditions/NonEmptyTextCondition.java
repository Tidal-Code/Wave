package com.tidal.wave.verification.conditions;

import com.tidal.wave.command.Executor;
import com.tidal.wave.commands.FindTextData;
import com.tidal.wave.data.WaitTime;
import com.tidal.wave.exceptions.TestAssertionError;
import com.tidal.wave.wait.FluentWait;

import java.time.Duration;
import java.util.List;

import static com.tidal.wave.data.WaitTimeData.getWaitTime;

public class NonEmptyTextCondition extends Condition {

    private final Executor executor = new Executor();


    @Override
    public void verify(boolean isVisible, boolean isMultiple, List<String> locators) {
        String duration = getWaitTime(WaitTime.EXPLICIT_WAIT_TIME) == null
                ? getWaitTime(WaitTime.DEFAULT_WAIT_TIME)
                : getWaitTime(WaitTime.EXPLICIT_WAIT_TIME);

        Duration waitDuration = Duration.ofSeconds(Integer.parseInt(duration));

        new FluentWait<>(executor)
                .pollingEvery(Duration.ofMillis(500))
                .forDuration(waitDuration)
                .throwing(TestAssertionError.class)
                .withMessage("Some text value is expected but got null")
                .until(e -> !e
                        .withMultipleElements(isMultiple)
                        .usingLocator(locators)
                        .isVisible(isVisible)
                        .usingLocator(locators)
                        .invokeCommand(FindTextData.class, "findTextData").toString().equals(""));
    }
}
