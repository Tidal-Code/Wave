package dev.tidalcode.wave.tabsandwindows;

public class LastWindow implements BrowserWindows {
    @Override
    public int getIndex(int totalNumberOfTabs) {
        return totalNumberOfTabs;
    }
}
