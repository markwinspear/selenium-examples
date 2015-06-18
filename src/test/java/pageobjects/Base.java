package pageobjects;

import com.google.common.annotations.Beta;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.security.Credentials;
import org.openqa.selenium.security.UserAndPassword;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.Alert;
import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import static java.lang.Boolean.*;
import tests.Config;

public class Base implements Config{
    private WebDriver driver;
    Boolean mobile;

    public Base(WebDriver driver){
        this.driver = driver;
        if (host.equals("saucelabs-mobile")) {
            mobile = TRUE;
        }
        else {
            mobile = FALSE;
        }
    }

    public void visit(String url) {
        if (url.contains("http")) {
            driver.get(url);
        } else {
            driver.get(baseUrl + url);
        }
    }
public WebElement find(By locator) {
        return driver.findElement(locator);
    }

    /**
     * if constant 'host' is not set to mobile then click, else perform a tap.
     * Allows click and tap to be used interchangeably
     * @param locator Webdriver locator
     */
    public void click(By locator) {
        if (!mobile) {
            find(locator).click();
        } else {
            tap(locator);
        }
    }

    /**
     * Compares the expected page title with the contents of the html title tag to confirm a match
     * @param pageTitle The expected page title
     * @throws IllegalStateException If the page title does not match
     */
    public void verifyPage(String pageTitle) {
        if (!pageTitle.equals(driver.getTitle())) {
            throw new IllegalStateException("This page is not " + pageTitle + ". The title is: " + driver.getTitle() );
        }
    }

    /**
     * if constant 'host' is not set to mobile then double click, else perform a double tap.
     * Allows doubleClick and doubleTap to be used interchangeably
     * @param locator Webdriver locator
     */
    public void doubleclick(By locator) {
            if (!mobile) {
                Actions builder = new Actions(driver);
                builder.doubleClick(find(locator));
                builder.build().perform();
            }
            else {
                new TouchActions(driver).doubleTap(find(locator));
            }
    }

    /**
     * Right click an element
     * @param locator Webdriver locator
     */
    public void rightClick(By locator) {
        Actions builder = new Actions(driver);
        builder.contextClick(find(locator));
        builder.build().perform();
    }

    /**
     * Mobile: Long press on an element
     * @param locator Weddriver locator
     */

    public void longPress (By locator) {
            new TouchActions(driver).longPress(find(locator));
    }

    public void type(String inputText, By locator) {
        find(locator).sendKeys(inputText);
    }

    public void submit(By locator) {
        find(locator).submit();
    }

    public Boolean isDisplayed(By locator) {
        try {
            return find(locator).isDisplayed();
        } catch (NoSuchElementException exception) {
            return false;
        }
    }

    /**
     * @param locator the WebDriver locator
     * @return the contents of the href attribute (for hyperlinks and images)
     */
    public String getLinkDestination (By locator) {
        return find(locator).getAttribute("href");
    }

    /**
     * Checks a checkbox or radio button. Performs no action if the element is already checked or selected
     * @param locator the WebDriver locator
     */
    public void check(By locator) {
        WebElement element = find(locator);
        if (!element.isSelected()) {
            element.click();
        }
    }

    /**
     * Use this method when authenticating to a windows security dialog instead of with(NOTE: THIS IS UNTESTED)
     * @param username the username to send to the alert box
     * @param password the password to send to the alert box
     */
    public void authenticateUsing(String username, String password) {
        Alert alert = driver.switchTo().alert();
        alert.authenticateUsing(new UserAndPassword("",""));

    }

    /**
     * Unchecks a checkbox or radio button. Performs no action if the element is already unchecked or un-selected
     * @param locator the WebDriver locator
     */
    public void uncheck(By locator) {
        WebElement element = find(locator);
        if (element.isSelected()) {
            element.click();
        }
    }

    /**
     * Verifies if a checkbox is checked
     * @param locator Webdriver locator
     * @return true if checkbox is selected
     */
    public Boolean isSelected(By locator) {
        return find(locator).isSelected();
    }

    /**
     * Taps on an element if constant host is set to mobile else performs a click.
     * Allows click and tap to be used interchangeably
     * @param locator WebDriver locator
     */
    public void tap(By locator) {
        if (mobile) {
            new TouchActions(driver).singleTap(find(locator));
        }
        else {
            click(locator);
        }
    }
    /**
     * Double taps on an element if constant host is set to mobile else performs a double click.
     * Allows doubleClick and doubleTap to be used interchangeably
     * @param locator WebDriver locator
     */
    public void doubleTap(By locator) {
        if (mobile) {
            new TouchActions(driver).doubleTap(find(locator));
        }
        else {
            doubleclick(locator);
        }
    }

    /**
     * Looks for the supplied option in the select list
     * @param locator the WebDriver locator
     * @param optionToFind a String containing the option to find in the select list
     * @return true if optionToFind matches any of the options in the select list
     */
    public Boolean optionExistsInSelectList(By locator, String optionToFind) {
        boolean match;
        try {
            match = false;
            Select select = new Select(find(locator));
            List<WebElement> options = select.getOptions();

            for (WebElement item : options) {
                if (item.getText().equals(optionToFind)) {
                    match = true;
                }
            }
            Assert.assertTrue(match);

        } catch (NoSuchElementException exception) {
            return false;
        }

        return match;
    }

    /**
     * Looks for the supplied optionS in the select list
     * @param locator The WebDriver locator
     * @param optionsToFind An array of Strings to match to the select list contents
     * @return true if all options are found
     */
    public Boolean optionsExistInSelectList(By locator, String[] optionsToFind) {

        boolean match = false;

        try {
            Select select = new Select(find(locator));
            List<WebElement> options = select.getOptions();

            for (WebElement item:options) {
                for (int i=0; i < optionsToFind.length; i++) {
                    if (item.getText().equals(optionsToFind[i])) {
                    match = true;
                    }
                }
            }
            Assert.assertTrue(match);

        } catch (NoSuchElementException exception) {
            return false;
        }

        return match;
    }

    public Boolean waitForIsDisplayed(By locator, Integer... timeout) {
        try {
            waitFor(ExpectedConditions.visibilityOfElementLocated(locator),(timeout.length > 0 ? timeout[0] : null));
        }
        catch (org.openqa.selenium.TimeoutException exception) {
            return false;
        }
        return true;
    }

    /**
     * @param date  A String representing a date
     * @param daysToAdd The number of days to add to the supplied date
     * @param monthsToAdd The number of months to add to the supplied date
     * @param yearsToAdd The number of years to add to the supplied date
     * @param format The format of date to return
     * @return the date with the number of days, months and years added in the specified format
     */
    public LocalDate getFutureDate(String date, int daysToAdd, int monthsToAdd, int yearsToAdd) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
       // String text = date.toString(formatter);
        LocalDate newDate = LocalDate.parse(date, formatter);
             // from http://stackoverflow.com/questions/22463062/how-to-parse-format-dates-with-localdatetime-java-8

        LocalDate dateToReturn = newDate.plusDays(daysToAdd).plusMonths(monthsToAdd).plusYears(yearsToAdd);

        return dateToReturn;         // change to return in a format specified in the argument
    }
    private void waitFor(ExpectedCondition<WebElement> condition, Integer timeout) {
        timeout = timeout != null ? timeout : 5;
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(condition);
    }
}