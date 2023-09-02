package com.tidal.wave.verification.expectations;

import com.tidal.wave.command.Executor;
import com.tidal.wave.commands.IsVisible;
import com.tidal.wave.data.WaitTime;
import com.tidal.wave.exceptions.TimeoutException;
import com.tidal.wave.wait.FluentWait;
import org.openqa.selenium.StaleElementReferenceException;

import java.time.Duration;
import java.util.List;

import static com.tidal.wave.data.WaitTimeData.getWaitTime;

public class InvisibleExpectation extends Expectation {

    private String byLocator;

    @Override
    public void assertion(Executor executor) {
        byLocator = executor.getContext().getLocators().get(executor.getContext().getElementIndex());

        String duration = getWaitTime(WaitTime.EXPLICIT_WAIT_TIME) == null
                ? getWaitTime(WaitTime.DEFAULT_WAIT_TIME)
                : getWaitTime(WaitTime.EXPLICIT_WAIT_TIME);

        Duration waitDuration = Duration.ofSeconds(Integer.parseInt(duration));

        result = new FluentWait<>(executor)
                .pollingEvery(Duration.ofMillis(500))
                .forDuration(waitDuration)
                .ignoring(TimeoutException.class)
                .ignoring(StaleElementReferenceException.class)
                .withMessage(String.format("Expected condition failed : Element %s expected to be invisible but was not", byLocator))
                .until(e -> !(boolean) (e.invokeCommand(IsVisible.class, "isVisible")));

    }

    @Override
    public void orElseFail() {
        super.orElseFail(String.format("Expected condition failed : Element %s expected to be invisible but was not", byLocator));
    }
}
