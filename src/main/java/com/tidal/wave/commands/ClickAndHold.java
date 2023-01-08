package com.tidal.wave.commands;

import com.tidal.wave.command.Command;
import com.tidal.wave.command.CommandAction;
import com.tidal.wave.command.CommandContext;
import com.tidal.wave.command.Commands;
import com.tidal.wave.counter.TimeCounter;
import com.tidal.wave.exceptions.CommandExceptions;
import com.tidal.wave.supplier.ObjectSupplier;
import com.tidal.wave.webelement.Element;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebElement;

import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class ClickAndHold extends CommandAction implements Command {

    private final Supplier<Map<Class<? extends Throwable>, Supplier<String>>> ignoredExceptions = this::ignoredEx;
    private final Element webElement = (Element) ObjectSupplier.instanceOf(Element.class);
    private final TimeCounter timeCounter = new TimeCounter();

    private boolean isMultiple;

    @Override
    public void contextSetter(CommandContext context) {
        this.locators = context.getLocators();
        this.isMultiple = context.isMultiple();
    }

    @Override
    public Map<Class<? extends Throwable>, Supplier<String>> ignoredEx() {
        return CommandExceptions.Of.click();
    }

    public void clickAndHoldAction() {
        WebElement element = webElement.getElement(locators, false, isMultiple);
        new Actions(((RemoteWebElement) element).getWrappedDriver()).clickAndHold(element).release().perform();
    }

    public void clickAndHold() {
        timeCounter.restart();
        super.execute(Commands.ClickCommands.CLICK_AND_HOLD.toString(), ignoredExceptions, timeCounter);
    }
}
    