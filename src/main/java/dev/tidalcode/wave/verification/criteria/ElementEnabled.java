package dev.tidalcode.wave.verification.criteria;

import dev.tidalcode.wave.command.Executor;
import dev.tidalcode.wave.commands.IsEnabled;
import dev.tidalcode.wave.data.WaitTime;
import dev.tidalcode.wave.wait.FluentWait;

import java.time.Duration;

import static dev.tidalcode.wave.data.WaitTimeData.getWaitTime;

public class ElementEnabled extends Criteria {
    @Override
    public void verify(Executor executor) {
        String duration = getWaitTime(WaitTime.EXPLICIT_WAIT_TIME) == null
                ? getWaitTime(WaitTime.DEFAULT_WAIT_TIME)
                : getWaitTime(WaitTime.EXPLICIT_WAIT_TIME);

        Duration waitDuration = Duration.ofSeconds(Integer.parseInt(duration));

        new FluentWait<>(executor)
                .pollingEvery(Duration.ofMillis(500))
                .forDuration(waitDuration)
                .withMessage(String.format("Element %s is not enabled", executor.getContext().getLocators().get(executor.getContext().getLocators().size() - 1)))
                .until(e -> e.invokeCommand(IsEnabled.class, "isEnabled"));
    }
}
