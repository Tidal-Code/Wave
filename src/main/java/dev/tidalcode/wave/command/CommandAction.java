package dev.tidalcode.wave.command;

import com.tidal.utils.counter.TimeCounter;
import dev.tidalcode.wave.data.WaitTime;
import dev.tidalcode.wave.exceptions.MethodInvokerException;
import dev.tidalcode.wave.exceptions.RuntimeTestException;
import dev.tidalcode.wave.wait.ThreadSleep;
import dev.tidalcode.wave.data.WaitTimeData;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public abstract class CommandAction {

    protected CommandContext context;

    protected abstract Map<Class<? extends Throwable>, Supplier<String>> ignoredEx();

    private String getLocator() {
        return context.getLocators().get(0);
    }

    @SuppressWarnings("unchecked")
    protected final <T> T execute(String action, Supplier<Map<Class<? extends Throwable>, Supplier<String>>> ignoredExceptions, TimeCounter timeCounter) {
        final int duration = Integer.parseInt(WaitTimeData.getWaitTime(WaitTime.EXPLICIT_WAIT_TIME) == null ? WaitTimeData.getWaitTime(WaitTime.DEFAULT_WAIT_TIME) : WaitTimeData.getWaitTime(WaitTime.EXPLICIT_WAIT_TIME));

        Object value = null;
        Class<?> klass = this.getClass();

        try {
            Method method = klass.getDeclaredMethod(action);
            value = method.invoke(this);
        } catch (NoSuchMethodException e) {
            throw new MethodInvokerException(String.format("No such method with name '%s', in class '%s'", action, klass.getName()), e);
        } catch (IllegalAccessException e) {
            throw new MethodInvokerException(String.format("Method '%s', in class '%s' has got private/protected access", action, klass.getName()), e);
        } catch (InvocationTargetException e) {
            ThreadSleep.forMilliS(500);
            Set<Class<? extends Throwable>> exList = ignoredExceptions.get().keySet();
            String targetedException = e.getTargetException().getClass().toString();

            if (timeCounter.timeElapsed(Duration.ofSeconds(duration))) {
                if (exList.stream().anyMatch(s -> targetedException.contains(s.getSimpleName()))) {
                    throw new RuntimeTestException(String.format(ignoredExceptions.get().get(e.getTargetException().getClass()).get(), getLocator()));
                } else {
                    throw new RuntimeTestException(String.format("Exception caused by %s", e.getCause()));
                }
            }

            if (exList.stream().anyMatch(s -> targetedException.contains(s.getSimpleName()))) {
                execute(action, ignoredExceptions, timeCounter);
            } else {
                throw new RuntimeTestException(String.format("Exception caused by %s", e.getCause()));
            }
        }
        return (T) value;
    }


}
