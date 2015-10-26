package tests;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.junit.Rule;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.MarionetteDriver;    // Will replace FirefoxDriver in future
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.edge.EdgeDriver;         // For running tests on MS Edge. Executable added to vendor folder

import com.saucelabs.saucerest.SauceREST;
import java.net.URL;


// Add browser drivers to the vendor folder, link to it and add it's location to the PATH environment variable

public class Base implements Config{

    protected WebDriver driver;
    protected AndroidDriver<AndroidElement> mobileDriver;
    private String testName;
    private String sessionId;
    private SauceREST sauceClient;

    @Rule
    public ExternalResource resource = new ExternalResource() {

        @Override
        protected void before() throws Throwable {
            DesiredCapabilities capabilities;
            String sauceUrl;

            switch (host) {
                case "saucelabs":
                    capabilities = new DesiredCapabilities();
                    capabilities.setCapability("browserName", browser);
                    capabilities.setCapability("version", browserVersion);
                    capabilities.setCapability("platform", platform);
                    capabilities.setCapability("name", testName);           //sets test name in saucelabs to the test name
                    capabilities.setCapability("recordVideo", recordVideo);
                    capabilities.setCapability("recordScreenshots", recordScreenshots);
                    //can add tags and build as capabilitiies also I believe - see sauce labs api documentation
                    sauceUrl = String.format("http://%s:%s@ondemand.saucelabs.com:80/wd/hub", sauceUser, sauceKey);
                    driver = new RemoteWebDriver(new URL(sauceUrl), capabilities);
                    sessionId = ((RemoteWebDriver) driver).getSessionId().toString();
                    sauceClient = new SauceREST(sauceUser, sauceKey);
                    break;

                case "localhost":
                    switch (browser) {
                        case "firefox":
                            driver = new FirefoxDriver();
                            //driver = new MarionetteDriver();
                            break;
                        case "chrome":
                            System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/vendor/chromedriver.exe");
                            driver = new ChromeDriver();
                            break;
                        case "edge":
                            System.setProperty("webdriver.edge.driver", System.getProperty("user.dir") + "/vendor/MicrosoftWebDriver.msi");
                            driver = new EdgeDriver();
                            break;
                        case "ie":
                            capabilities = DesiredCapabilities.internetExplorer();
                            capabilities.setCapability("EnableNativeEvents", false);
                            capabilities.setCapability("ignoreZoomSetting", true);
                            System.setProperty("webdriver.ie.driver", System.getProperty("user.dir") + "/vendor/IEDriverServer.exe");
                            driver = new InternetExplorerDriver(capabilities);
                            driver.findElement(By.tagName("html")).sendKeys(Keys.chord(Keys.CONTROL, "0")); //set zoom level to 100% or IE Driver throws an error
                            break;

                    }
                    break;

                case "local-android":
                //     mobileDriver = new AndroidDriver<AndroidElement>();

                    break;


                case "saucelabs-mobile":
                    capabilities = new DesiredCapabilities();
                    capabilities.setCapability("browserName", browser);
                    capabilities.setCapability("deviceName", device);
                    capabilities.setCapability("platform", platform);
                    capabilities.setCapability("platformVersion", platformVersion);
                    capabilities.setCapability("appiumVersion", appiumVersion);
                    capabilities.setCapability("device-orientation", deviceOrientation);
                    // capabilities.setCapability("browserVersion",browserVersion);
                    capabilities.setCapability("name", testName);           //sets test name in saucelabs to the test name
                    capabilities.setCapability("recordVideo", recordVideo);
                    capabilities.setCapability("recordScreenshots", recordScreenshots);
                    //can add tags and build as capabilitiies
                    // capabilities.setCapability("tags",tags);
                    capabilities.setCapability("build", build);
                    sauceUrl = String.format("http://%s:%s@ondemand.saucelabs.com:80/wd/hub", sauceUser, sauceKey);
                    driver = new RemoteWebDriver(new URL(sauceUrl), capabilities);
                    sessionId = ((RemoteWebDriver) driver).getSessionId().toString();
                    sauceClient = new SauceREST(sauceUser, sauceKey);
                    break;
                }
            }

        @Override
        protected void after() {
            driver.quit();
        }
    };

        @Rule
        public TestRule watcher = new TestWatcher() {
            @Override
            protected void starting(Description description) {
                testName = description.getDisplayName();
            }

            @Override
            protected void failed(Throwable throwable, Description description) {
                if (host.contains("saucelabs")) {
                    sauceClient.jobFailed(sessionId);
                    System.out.println(String.format("https://saucelabs.com/tests/%s", sessionId));
                }
            }

            @Override
            protected void succeeded(Description description) {
                if (host.contains("saucelabs")) {
                    sauceClient.jobPassed(sessionId);
                }
            }
        };
}
