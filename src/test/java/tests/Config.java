package tests;

public interface Config {

    final String baseUrl        = System.getProperty("baseUrl", "http://the-internet.herokuapp.com"); //2nd param is the default to use
    final String browser        = System.getProperty("browser", "firefox");

    //to support executing using SauceLabs
    final String host           = System.getProperty("host", "localhost"); //or "saucelabs"
    final String browserVersion = System.getProperty("browserVersion", "33");
    final String platform       = System.getProperty("platform", "Windows 8.1");

    //These details can be stored as environment variables then replace with System.getenv("SAUCE_USERNAME") and "SAUCE_ACCESS_KEY"
    final String sauceUser = "markwinspear";
    final String sauceKey = "0b03dcb1-949b-4261-ad9f-1a465421e779";

}