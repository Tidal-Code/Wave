package dev.tidalcode.wave.verification.conditions.collections;

import dev.tidalcode.wave.command.Executor;
import dev.tidalcode.wave.commands.GetSize;
import dev.tidalcode.wave.wait.FluentWait;

import java.time.Duration;


/*
 * Wait duration is hard coded as not to double up the wait time because find.elements will wait for the explicit wait time
 */
@SuppressWarnings("all")
public class CollectionSizeGreaterThan extends CollectionsCondition {
    private final int value;

    public CollectionSizeGreaterThan(int value) {
        this.value = value;
    }

    @Override
    public void verify(Executor executor) {
        Duration waitDuration = Duration.ofSeconds(1);

        new FluentWait<>(executor)
                .pollingEvery(Duration.ofMillis(500))
                .forDuration(waitDuration)
                .withMessage(String.format("Expected number of elements is more than %d, but could only find %d element[s]", value, (int) executor.invokeCommand(GetSize.class, "getSize")))
                .until(e -> (int) e.invokeCommand(GetSize.class) > value);
    }
}
