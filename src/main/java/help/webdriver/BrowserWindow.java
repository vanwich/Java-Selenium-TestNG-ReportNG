package help.webdriver;

import java.awt.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import help.config.PropertyProvider;
import help.config.TestProperties;

public class BrowserWindow {
    protected static final Logger log = LoggerFactory.getLogger(BrowserWindow.class);

    private Rectangle monitorBounds;
    private org.openqa.selenium.Point windowTopLeftPoint;
    private org.openqa.selenium.Dimension windowSize;

    BrowserWindow() {
        getMonitorBounds(PropertyProvider.getProperty(TestProperties.TEST_BROWSER_MONITOR_NUMBER, -1)).ifPresent(mb -> {
            monitorBounds = mb;
            windowTopLeftPoint = new org.openqa.selenium.Point((int) monitorBounds.getX(), (int) monitorBounds.getY());
            int width = PropertyProvider.getProperty("test.browser.windowwidth", (int) monitorBounds.getWidth());
            int height = PropertyProvider.getProperty("test.browser.windowheight", (int) monitorBounds.getHeight());
            this.windowSize = new org.openqa.selenium.Dimension(width, height);
            setWindowLayout(width, height);
        });
    }

    public Rectangle getMonitorBounds() {
        return monitorBounds;
    }

    public org.openqa.selenium.Point getTopLeftPoint() {
        return windowTopLeftPoint;
    }

    public org.openqa.selenium.Dimension getSize() {
        return windowSize;
    }

    private Optional<Rectangle> getMonitorBounds(int monitorNumber) {
        if (monitorNumber < 0 && PropertyProvider.getProperty(TestProperties.TEST_BROWSER_WINDOW_LAYOUT).isEmpty()) {
            return Optional.empty();
        } else {
            try {
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice gd;
                if (monitorNumber < 0) {
                    gd = ge.getDefaultScreenDevice();
                } else {
                    GraphicsDevice[] graphicsDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
                    if (monitorNumber > graphicsDevices.length + 1) {
                        log.error("Monitor with number {} does not exist, total number of monitors is: {}", monitorNumber, graphicsDevices.length);
                        return Optional.empty();
                    }
                    gd = graphicsDevices[monitorNumber - 1];
                }
                return Optional.ofNullable(gd.getDefaultConfiguration().getBounds());
            } catch (Throwable e) {
                log.error("Unable to get graphics configuration", e);
                return Optional.empty();
            }
        }
    }

    private void setWindowLayout(int width, int height) {
        String layout = PropertyProvider.getProperty(TestProperties.TEST_BROWSER_WINDOW_LAYOUT);
        if (!layout.isEmpty()) {
            switch (Layout.of(layout)) {
                case HALF_LEFT:
                    windowSize = new org.openqa.selenium.Dimension(width / 2, height);
                    break;
                case HALF_RIGHT:
                    windowTopLeftPoint = windowTopLeftPoint.moveBy(width / 2, 0);
                    windowSize = new org.openqa.selenium.Dimension(width / 2, height);
                    break;
                case HALF_TOP:
                    windowSize = new org.openqa.selenium.Dimension(width, height / 2);
                    break;
                case HALF_BOTTOM:
                    windowTopLeftPoint = windowTopLeftPoint.moveBy(0, height / 2);
                    windowSize = new org.openqa.selenium.Dimension(width, height / 2);
                    break;
                case QUARTER_UPPER_LEFT:
                    windowSize = new org.openqa.selenium.Dimension(width / 2, height / 2);
                    break;
                case QUARTER_UPPER_RIGHT:
                    windowTopLeftPoint = windowTopLeftPoint.moveBy(width / 2, 0);
                    windowSize = new org.openqa.selenium.Dimension(width / 2, height / 2);
                    break;
                case QUARTER_LOWER_LEFT:
                    windowTopLeftPoint = windowTopLeftPoint.moveBy(0, height / 2);
                    windowSize = new org.openqa.selenium.Dimension(width / 2, height / 2);
                    break;
                case QUARTER_LOWER_RIGHT:
                    windowTopLeftPoint = windowTopLeftPoint.moveBy(width / 2, height / 2);
                    windowSize = new org.openqa.selenium.Dimension(width / 2, height / 2);
                    break;
                case DEFAULT:
                    log.error("Invalid browser window layout [{}], supported values are: {}. Browser window layout will not be changed",
                            layout, Arrays.stream(Layout.values()).map(Layout::get).collect(Collectors.toList()));
            }
        }
    }

    private enum Layout {
        HALF_LEFT("half-left"),
        HALF_RIGHT("half-right"),
        HALF_TOP("half-top"),
        HALF_BOTTOM("half-bottom"),
        QUARTER_UPPER_LEFT("quarter-upper-left"),
        QUARTER_UPPER_RIGHT("quarter-upper-right"),
        QUARTER_LOWER_LEFT("quarter-lower-left"),
        QUARTER_LOWER_RIGHT("quarter-lower-right"),
        DEFAULT("default");

        private final String layout;

        Layout(String layout) {
            this.layout = layout;
        }

        public static Layout of(String layout) {
            return Arrays.stream(values()).filter(w -> w.get().equals(layout)).findFirst().orElse(DEFAULT);
        }

        private String get() {
            return layout;
        }
    }
}
