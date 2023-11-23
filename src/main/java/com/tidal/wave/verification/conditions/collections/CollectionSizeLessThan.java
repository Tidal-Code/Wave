package com.tidal.wave.verification.conditions.collections;

import com.tidal.wave.command.Executor;
import com.tidal.wave.commands.GetSize;
import com.tidal.wave.wait.FluentWait;

import java.time.Duration;

/*
 * Wait duration is hard coded as not to double up the wait time because find.elements will wait for the explicit wait time
 */
public class CollectionSizeLessThan extends CollectionsCondition {
    private final int value;

    public CollectionSizeLessThan(int value) {
        this.value = value;
    }

    @Override
    public void verify(Executor executor) {
        Duration waitDuration = Duration.ofSeconds(1);

        new FluentWait<>(executor)
                .pollingEvery(Duration.ofMillis(500))
                .forDuration(waitDuration)
                .withMessage(String.format("Expected number of elements is less than %d but found %d element[s]", value, (int) executor.invokeCommand(GetSize.class, "getSize")))
                .until(e -> (int) e.invokeCommand(GetSize.class, "getSize") < value);
    }
}
