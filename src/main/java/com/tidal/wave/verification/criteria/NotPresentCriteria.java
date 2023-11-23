package com.tidal.wave.verification.criteria;

import com.tidal.wave.command.Executor;
import com.tidal.wave.commands.GetSize;
import com.tidal.wave.data.WaitTime;
import com.tidal.wave.wait.FluentWait;

import java.time.Duration;

import static com.tidal.wave.data.WaitTimeData.getWaitTime;

public class NotPresentCriteria extends Criteria {

    @Override
    public void verify(Executor executor) {
        String duration = getWaitTime(WaitTime.EXPLICIT_WAIT_TIME) == null
                ? getWaitTime(WaitTime.DEFAULT_WAIT_TIME)
                : getWaitTime(WaitTime.EXPLICIT_WAIT_TIME);

        Duration waitDuration = Duration.ofSeconds(Integer.parseInt(duration));

        new FluentWait<>(executor)
                .pollingEvery(Duration.ofMillis(500))
                .forDuration(waitDuration)
                .withMessage(String.format("Element %s is still present in the DOM", executor.getContext().getLocators().get(executor.getContext().getLocators().size() - 1)))
                .until(e -> (int) e.invokeCommand(GetSize.class, "getSize") == 0);
    }

}
