package tests;

import org.junit.Test;
import org.junit.Before;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import pageobjects.Login;
import tests.groups.Smoke;
import utility.ExcelUtils;
import utility.Constant;
import org.easetech.easytest.loader.LoaderType;
import org.easetech.easytest.runner.DataDrivenTestRunner;
import org.easetech.easytest.annotation.Param;
import org.easetech.easytest.annotation.DataLoader;
import static org.junit.Assert.*;

@RunWith(DataDrivenTestRunner.class)        //added for Data driven testing using Easytest
@DataLoader(filePaths = {"C:\\Users\\winspearm\\Desktop\\testData.xls"}, loaderType = LoaderType.EXCEL)
public class TestLogin extends Base  {

    private Login login;
    private String title;

    @Before
    public void setUp() {
        login = new Login(driver);
     }

    @Test
    @Category(Smoke.class)
    public void succeeded() {
        login.with("tomsmith","SuperSecretPassword!");
        assertTrue("success message not present", login.successMessagePresent());
    }

    @Test
    @Category(Smoke.class)
    public void failed() {
        login.with("tomsmith","bad password");
        assertTrue("failure message wasn't present after providing an incorrect password", login.failureMessagePresent());
   }

    @Test
    @Category(Smoke.class)
    public void dataProvisioning(@Param(name="username")String username, @Param(name = "password") String password, @Param(name="expectedResult")String expectedResult) throws Exception {

     //   ExcelUtils.setExcelFile(Constant.path_TestData + Constant.file_testData,"login");
     //   String username = ExcelUtils.getCellData(1,1);          //zero-based indexing
     //   String password = ExcelUtils.getCellData(1,2);
     //   String expectedResult = ExcelUtils.getCellData(1,3);
        login.with(username, password);

        switch (expectedResult) {
            case "Success":
                assertTrue("success message not present", login.successMessagePresent());
                break;
            case "Failure":
                assertTrue("failure message wasn't present after providing an incorrect password", login.failureMessagePresent());
                break;
            default:
             //   ExcelUtils.SetCellData("Exception",1,4);
                throw new IllegalArgumentException("Expected either Success or Failure but found " + expectedResult );
        }
    }

}