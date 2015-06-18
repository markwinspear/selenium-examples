package tests;

import org.junit.Rule;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.saucelabs.saucerest.SauceREST;
import java.net.URL;

public class Base implements Config{

    protected WebDriver driver;
    private String testName;
    private String sessionId;
    private SauceREST sauceClient;

    @Rule
    public ExternalResource resource = new ExternalResource() {

        @Override
        protected void before() throws Throwable {
            if (host.equals("saucelabs")) {
                DesiredCapabilities capabilities = new DesiredCapabilities();
                capabilities.setCapability("browserName", browser);
                capabilities.setCapability("version", browserVersion);
                capabilities.setCapability("platform", platform);
                capabilities.setCapability("name", testName);           //sets test name in saucelabs to the test name
                capabilities.setCapability("recordVideo", recordVideo);
                capabilities.setCapability("recordScreenshots", recordScreenshots);
                //can add tags and build as capabilitiies also I believe - see sauce labs api documentation
                String sauceUrl = String.format("http://%s:%s@ondemand.saucelabs.com:80/wd/hub", sauceUser, sauceKey);
                driver = new RemoteWebDriver(new URL(sauceUrl),capabilities);
                sessionId = ((RemoteWebDriver) driver).getSessionId().toString();
                sauceClient = new SauceREST(sauceUser, sauceKey);
            }
            else if (host.equals("localhost")) {
                if (browser.equals("firefox")) {
                    driver = new FirefoxDriver();
                } else if (browser.equals("chrome")) {
                    System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/vendor/chromedriver.exe");
                    driver = new ChromeDriver();
                }
            }

            else if (host.equals("saucelabs-mobile")) {
                DesiredCapabilities capabilities = new DesiredCapabilities();
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
                String sauceUrl = String.format("http://%s:%s@ondemand.saucelabs.com:80/wd/hub", sauceUser, sauceKey);
                driver = new RemoteWebDriver(new URL(sauceUrl),capabilities);
                sessionId = ((RemoteWebDriver) driver).getSessionId().toString();
                sauceClient = new SauceREST(sauceUser, sauceKey);
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
