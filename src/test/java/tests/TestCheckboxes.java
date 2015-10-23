package tests;

import org.junit.Test;
import org.junit.Before;
import org.junit.experimental.categories.Category;
import pageobjects.CheckboxesPage;
import tests.groups.Smoke;

import static org.junit.Assert.assertTrue;

public class TestCheckboxes extends Base {

    private CheckboxesPage checkboxesPage;

    @Before
    public void setUp() {
        checkboxesPage = new CheckboxesPage(driver);
    }

    @Test
    @Category(Smoke.class)
    public void checkAll() {
        checkboxesPage.checkAllCheckBoxes();
        assertTrue("Checkboxes not selected", checkboxesPage.verifyAllCheckBoxesSelected());
    }
}
