package com.tidal.wave.verification.expectations.collections;

import com.tidal.wave.command.Executor;
import com.tidal.wave.commands.GetSize;
import com.tidal.wave.data.WaitTime;
import com.tidal.wave.exceptions.ExpectationFailure;
import com.tidal.wave.exceptions.TimeoutException;
import com.tidal.wave.supplier.ObjectSupplier;
import com.tidal.wave.wait.FluentWait;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;

import java.time.Duration;
import java.util.List;

import static com.tidal.wave.data.WaitTimeData.getWaitTime;

public class SizeEqualsExpectation implements Expectations {

    private static final String GET_SIZE = "getSize";
    private final Executor executor = (Executor) ObjectSupplier.instanceOf(Executor.class);
    private final int size;
    private boolean result;

    public SizeEqualsExpectation(int size) {
        this.size = size;
    }


    @Override
    public void assertion(boolean isMultiple, List<By> locatorSet) {

        String duration = getWaitTime(WaitTime.EXPLICIT_WAIT_TIME) == null
                ? getWaitTime(WaitTime.DEFAULT_WAIT_TIME)
                : getWaitTime(WaitTime.EXPLICIT_WAIT_TIME);

        Duration waitDuration = Duration.ofSeconds(Integer.parseInt(duration));

        //noinspection RedundantCast
        result = new FluentWait<>(executor)
                .pollingEvery(Duration.ofMillis(500))
                .forDuration(waitDuration)
                .ignoring(TimeoutException.class)
                .ignoring(StaleElementReferenceException.class)
                .withMessage(String.format("Expected number of elements is %d but could find [only] %d element[s]", size, (int) executor.isVisible(false).withMultipleElements(isMultiple).usingLocator(locatorSet).invokeCommand(GetSize.class, GET_SIZE)))
                .until(e -> (int) e
                        .usingLocator(locatorSet)
                        .withMultipleElements(isMultiple)
                        .invokeCommand(GetSize.class, GET_SIZE) == size);
    }

    @Override
    public void orElseFail() {
        if (!result) {
            //noinspection RedundantCast
            throw new ExpectationFailure(String.format("Expected number of elements is %d but could find [only] %d element[s]", size, (int) executor.invokeCommand(GetSize.class, GET_SIZE)));
        }
    }
}
