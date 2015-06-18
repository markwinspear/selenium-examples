package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckboxesPage extends Base {

    By checkboxOne = By.xpath("//*[@id=\"checkboxes\"]/input[1]");
    By checkboxTwo = By.xpath("//*[@id=\"checkboxes\"]/input[2]");

    public CheckboxesPage (WebDriver driver){
        super(driver);
        visit("/checkboxes");
        verifyPage("The Internet");
    }

    public void checkAllCheckBoxes() {
        check(checkboxOne);
        check(checkboxTwo);
    }

    public Boolean verifyAllCheckBoxesSelected() {
        Boolean result;
        if (isSelected(checkboxOne) && isSelected(checkboxTwo)) {
            result = true;
        }
        else {
            result = false;
        }
        return result;
    }
}
