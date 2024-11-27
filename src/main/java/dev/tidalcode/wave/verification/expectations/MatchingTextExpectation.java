package dev.tidalcode.wave.verification.expectations;

import dev.tidalcode.wave.command.Executor;
import dev.tidalcode.wave.commands.FindTextData;
import dev.tidalcode.wave.data.WaitTime;
import dev.tidalcode.wave.exceptions.TimeoutException;
import dev.tidalcode.wave.wait.FluentWait;
import org.openqa.selenium.StaleElementReferenceException;

import java.time.Duration;

import static dev.tidalcode.wave.data.WaitTimeData.getWaitTime;

public class MatchingTextExpectation extends Expectation {

    private static final String FIND_TEXT_DATA = "findTextData";
    private final String value;
    private final Executor executor = new Executor();

    public MatchingTextExpectation(String value) {
        this.value = value;
    }

    @Override
    public void assertion(Executor executor) {

        String duration = getWaitTime(WaitTime.EXPLICIT_WAIT_TIME) == null
                ? getWaitTime(WaitTime.DEFAULT_WAIT_TIME)
                : getWaitTime(WaitTime.EXPLICIT_WAIT_TIME);

        Duration waitDuration = Duration.ofSeconds(Integer.parseInt(duration));

        result = new FluentWait<>(executor)
                .pollingEvery(Duration.ofMillis(500))
                .forDuration(waitDuration)
                .ignoring(TimeoutException.class)
                .ignoring(StaleElementReferenceException.class)
                .withMessage(String.format("Expected value '%s' is not contained in the actual value %s", value, executor.invokeCommand(FindTextData.class, FIND_TEXT_DATA)))
                .until(e -> e.invokeCommand(FindTextData.class, FIND_TEXT_DATA).toString().contains(value));
    }

    @Override
    public void orElseFail() {
        super.orElseFail(String.format("Expected value '%s' is not contained in the actual value %s", value, executor.invokeCommand(FindTextData.class, FIND_TEXT_DATA)));
    }
}
