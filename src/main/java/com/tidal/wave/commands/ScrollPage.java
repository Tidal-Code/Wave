package com.tidal.wave.commands;

import com.tidal.wave.command.Command;
import com.tidal.wave.command.CommandAction;
import com.tidal.wave.command.CommandContext;
import com.tidal.wave.command.Commands;
import com.tidal.wave.counter.TimeCounter;
import com.tidal.wave.exceptions.CommandExceptions;
import com.tidal.wave.supplier.ObjectSupplier;
import com.tidal.wave.webelement.Element;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class ScrollPage extends CommandAction implements Command {

    private final Supplier<Map<Class<? extends Throwable>, Supplier<String>>> ignoredExceptions = this::ignoredEx;
    private final Element webElement = (Element) ObjectSupplier.instanceOf(Element.class);
    private final TimeCounter timeCounter = new TimeCounter();
    private boolean isMultiple;
    private int[] xyCords;

    @Override
    public void contextSetter(CommandContext context) {
        this.locators = context.getLocators();
        this.isMultiple = context.isMultiple();
        this.xyCords = context.getXYCords();
    }

    @Override
    protected Map<Class<? extends Throwable>, Supplier<String>> ignoredEx() {
        return CommandExceptions.TypeOf.stale();
    }

    public void scrollPageAction() {
        WebElement element = webElement.getElement(locators, false, isMultiple);
        ((JavascriptExecutor) ((RemoteWebElement) element).getWrappedDriver()).executeScript(String.format("window.scrollBy(%d ,%d)", xyCords[0], xyCords[1]), element);
    }

    public void scrollPage() {
        timeCounter.restart();
        super.execute(Commands.MoveCommands.SCROLL_PAGE.toString(), ignoredExceptions, timeCounter);
    }

}
