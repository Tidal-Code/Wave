package com.tidal.wave.verification.criteria;

import com.tidal.wave.command.Executor;
import com.tidal.wave.commands.GetSize;
import com.tidal.wave.data.WaitTime;
import com.tidal.wave.wait.FluentWait;

import java.time.Duration;
import java.util.List;

import static com.tidal.wave.data.WaitTimeData.getWaitTime;

public class NotPresentCriteria extends Criteria {

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
                .withMessage(String.format("Element %s is still present in the DOM", locators.get(0)))
                .until(e -> (int) e
                        .usingLocator(locators)
                        .withMultipleElements(isMultiple)
                        .isVisible(isVisible)
                        .invokeCommand(GetSize.class, "getSize") == 0);
    }

}
