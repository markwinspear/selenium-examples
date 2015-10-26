package tests;

import org.junit.Test;
import org.junit.Before;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import pageobjects.Login;
import tests.groups.Deep;
import tests.groups.Smoke;
import utility.SpreadsheetData;

import static org.junit.Assert.assertTrue;

@RunWith(value = Parameterized.class)
public class TestLoginWithData extends Base {
    private Login login;
    private String title;
    private static StringBuffer verificationErrors = new StringBuffer();

    private String username;
    private String password;
    private String expectedResult;

    public TestLoginWithData(String username, String password, String expectedResult) {
        this.username = username;
        this.password = password;
        this.expectedResult = expectedResult;
    }

    @Before
    public void setUp() {
        login = new Login(driver);
    }

    @Parameters
    public static Collection testData() throws Exception {
        InputStream spreadsheet = new FileInputStream("C:\\users\\winspearm\\desktop\\testData.xlsx");
        return new SpreadsheetData(spreadsheet).getData();
    }

    @Test
    @Category(Deep.class)
    public void testValidAndInvalidDetails() throws Exception {
        try {
            login.with(this.username, this.password);

            switch (expectedResult) {
                case "Success":
                    assertTrue("success message not present", login.success());
                    break;
                case "Failure":
                    assertTrue("failure message wasn't present after providing an incorrect password", login.failure());
                    break;
                default:
                    throw new IllegalArgumentException("Expected either Success or Failure but found " + expectedResult);
            }

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
    }
}

