package com.onarinskyi.driver;

import com.onarinskyi.environment.Browser;
import com.onarinskyi.environment.OperatingSystem;
import com.onarinskyi.environment.Timeout;
import com.onarinskyi.environment.UrlResolver;
import com.onarinskyi.exceptions.UnknownBrowserException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Component
@PropertySource("classpath:driver.properties")
public class WebDriverFactory {

    @Value("${use.grid}")
    private Boolean useGrid;

    @Value("${grid.hub}")
    private URL hubHostUrl;

    @Value("${exception.fail}")
    private Boolean failOnException;

    @Value("${device.name}")
    private String deviceName;

    @Value("${version}")
    private String version;

    @Value("${browser.name}")
    private String browserName;

    @Value("${platform}")
    private String platform;

    @Value("${device.width}")
    private String deviceWidth;

    @Value("${device.height}")
    private String deviceHeight;

    @Value("${device.pixel.ratio}")
    private String devicePixelRatio;

    @Value("${device.user.agent}")
    private String deviceUserAgent;

    @Autowired
    private Timeout timeout;

    @Autowired
    private UrlResolver urlResolver;

    @Autowired
    private OperatingSystem operatingSystem;

    @Autowired
    private Browser browser;

    public WebDriverDecorator getInitialDriver() {
        initDriversFor(operatingSystem);

        WebDriver driver = useGrid ? getRemoteDriver(browser, hubHostUrl) :
                getLocalDriver(browser);

        return new WebDriverDecorator(driver, timeout, urlResolver, failOnException);
    }

    private void initDriversFor(OperatingSystem operatingSystem) {
        switch (operatingSystem) {
            case MACOS:
                System.setProperty("webdriver.chrome.driver", "src/main/java/com/onarinskyi/drivers/macos/chromedriver");
                System.setProperty("webdriver.gecko.driver", "src/main/java/com/onarinskyi/drivers/macos/geckodriver");
                break;
            case WINDOWS:
                System.setProperty("webdriver.chrome.driver", "src/main/java/com/onarinskyi/drivers/windows/chromedriver.exe");
                System.setProperty("webdriver.ie.driver", "src/main/java/com/onarinskyi/drivers/windows/IEDriverServer.exe");
                System.setProperty("webdriver.edge.driver", "src/main/java/com/onarinskyi/drivers/windows/MicrosoftWebDriver.exe");
                System.setProperty("webdriver.gecko.driver", "src/main/java/com/onarinskyi/drivers/windows/geckodriver.exe");
                System.setProperty("phantomjs.binary.path", "src/main/java/com/onarinskyi/drivers/windows/phantomjs.exe");
                break;
        }
    }

    private WebDriver getLocalDriver(Browser browser) {
        switch (browser) {
            case CHROME:
                return new ChromeDriver(chromeOptions(false));
            case IE:
                return new InternetExplorerDriver(ieOptions());
            case EDGE:
                return new EdgeDriver(edgeOptions());
            case FIREFOX:
                return new FirefoxDriver(firefoxOptions());
            case HEADLESS:
                return new ChromeDriver(chromeOptions(true));
            case MOBILE_EMULATOR_CHROME:
                return new ChromeDriver(chromeMobileOptions(deviceName, deviceWidth, deviceHeight, devicePixelRatio, deviceUserAgent));
            default:
                throw new UnknownBrowserException(browser + "is not configured in the framework");
        }
    }

    private WebDriver getRemoteDriver(Browser browser, URL hubHost) {
        switch (browser) {
            case CHROME:
                return new RemoteWebDriver(hubHost, chromeOptions(false));
            case IE:
                return new RemoteWebDriver(hubHost, ieOptions());
            case EDGE:
                return new RemoteWebDriver(hubHost, edgeOptions());
            case FIREFOX:
                return new RemoteWebDriver(hubHost, firefoxOptions());
            case HEADLESS:
                return new RemoteWebDriver(hubHost, chromeOptions(true));
            case MOBILE_EMULATOR_CHROME:
                return new RemoteWebDriver(hubHost, chromeMobileOptions(deviceName, deviceWidth, deviceHeight, devicePixelRatio, deviceUserAgent));
            default:
                throw new UnknownBrowserException(browser + "is not configured in the framework");
        }
    }

    private ChromeOptions chromeOptions(boolean isHeadless) {
        ChromeOptions options = new ChromeOptions();

        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

        desiredCapabilities.setVersion(version);
        desiredCapabilities.setBrowserName(browserName);
        desiredCapabilities.setPlatform(Platform.fromString(platform));

        options.merge(desiredCapabilities);

        HashMap<String, Object> preferences = new HashMap<>();

        String downloadDirectory = System.getProperty("user.dir") + File.separator + "src";
        preferences.put("profile.default_content_settings.popups", 0);
        preferences.put("download.default_directory", downloadDirectory);

        options.setExperimentalOption("prefs", preferences);

        if (isHeadless) {
            options.addArguments("headless");
        }

        return options;
    }

    private InternetExplorerOptions ieOptions() {
        InternetExplorerOptions options = new InternetExplorerOptions();

        DesiredCapabilities desiredCapabilities = DesiredCapabilities.internetExplorer();

        desiredCapabilities.setVersion(version);
        desiredCapabilities.setBrowserName(browserName);
        desiredCapabilities.setPlatform(Platform.fromString(platform));

        desiredCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        desiredCapabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
        desiredCapabilities.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false);
        desiredCapabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);

        options.merge(desiredCapabilities);

        return options;
    }

    private EdgeOptions edgeOptions() {
        DesiredCapabilities desiredCapabilities = DesiredCapabilities.edge();

        EdgeOptions edgeOptions = new EdgeOptions();

        edgeOptions.merge(desiredCapabilities);

        return edgeOptions;
    }

    private FirefoxOptions firefoxOptions() {
        DesiredCapabilities desiredCapabilities = DesiredCapabilities.firefox();
        FirefoxProfile profile = new FirefoxProfile();
        desiredCapabilities.setCapability(FirefoxDriver.PROFILE, profile);

        FirefoxOptions options = new FirefoxOptions();

        options.merge(desiredCapabilities);

        return options;
    }

    private ChromeOptions chromeMobileOptions(String deviceName, String deviceWidth, String deviceHeight, String devicePixelRatio, String deviceUserAgent) {
        ChromeOptions options = new ChromeOptions();

        Map<String, Object> mobileEmulation = new HashMap<>();

        if (!deviceName.isEmpty()) {
            mobileEmulation.put("deviceName", deviceName);
        } else {
            Map<String, Object> deviceMetrics = new HashMap<>();

            if (!deviceWidth.isEmpty()) {
                deviceMetrics.put("width", Integer.valueOf(deviceWidth));
            }

            if (!deviceHeight.isEmpty()) {
                deviceMetrics.put("height", Integer.valueOf(deviceHeight));
            }

            if (!devicePixelRatio.isEmpty()) {
                deviceMetrics.put("pixelRatio",Double.valueOf(devicePixelRatio));
            }

            mobileEmulation.put("deviceMetrics", deviceMetrics);

            if (!deviceUserAgent.isEmpty()) {
                mobileEmulation.put("userAgent", deviceUserAgent);
            }
        }

        options.setExperimentalOption("mobileEmulation", mobileEmulation);

        return options;
    }
}
