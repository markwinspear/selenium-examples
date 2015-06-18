package pageobjects;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Login extends Base {

    String pageTitle = "The Internet";
    By usernameLocator = By.id("username");
    By passwordLocator = By.id("password");
    By loginFormLocator = By.id("login");
    By successMessageLocator = By.cssSelector(".flash.success");
    By failureMessageLocator = By.cssSelector(".flash.error");

    public Login(WebDriver driver) {
        super(driver);
        visit("/login");
        verifyPage(pageTitle);             // alternatively, could look for an element on the page, i.e. AssertEquals("The login form is not present",isDisplayed(loginFormLocator)
    }

    public void with(String username, String password) {
        type(username, usernameLocator);
        type(password, passwordLocator);
        submit(loginFormLocator);
    }

    public Boolean successMessagePresent() {
        return isDisplayed(successMessageLocator);
    }

    public Boolean failureMessagePresent() {
        return isDisplayed(failureMessageLocator);
    }
}