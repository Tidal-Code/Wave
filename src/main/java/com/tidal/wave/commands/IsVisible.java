package com.tidal.wave.commands;

import com.tidal.utils.counter.TimeCounter;
import com.tidal.wave.command.Command;
import com.tidal.wave.command.CommandAction;
import com.tidal.wave.command.CommandContext;
import com.tidal.wave.command.Commands;
import com.tidal.wave.exceptions.CommandExceptions;
import com.tidal.wave.supplier.ObjectSupplier;
import com.tidal.wave.webelement.Element;
import org.openqa.selenium.WebElement;

import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class IsVisible extends CommandAction implements Command {

    private final Supplier<Map<Class<? extends Throwable>, Supplier<String>>> ignoredExceptions = this::ignoredEx;
    private final Element webElement = (Element) ObjectSupplier.instanceOf(Element.class);
    private final TimeCounter timeCounter = new TimeCounter();
    private CommandContext context;

    private boolean isMultiple;

    @Override
    public void contextSetter(CommandContext context) {
        this.context = context;
        this.context.setVisibility(false);
    }

    @Override
    public Map<Class<? extends Throwable>, Supplier<String>> ignoredEx() {
        return CommandExceptions.TypeOf.stale();
    }

    public boolean isVisibleAction() {
        WebElement element = webElement.getElement(context);
        return element.isDisplayed();
    }

    public boolean isVisible() {
        timeCounter.restart();
        return super.execute(Commands.StateCheckCommands.IS_VISIBLE.toString(), ignoredExceptions, timeCounter);
    }
}
