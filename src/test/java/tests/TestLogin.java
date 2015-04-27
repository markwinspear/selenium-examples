package tests;

import org.junit.Test;
import org.junit.Before;
import org.junit.experimental.categories.Category;
import pageobjects.Login;
import tests.groups.Smoke;


import static org.junit.Assert.*;

public class TestLogin extends Base  {

    private Login login;

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
        assertTrue("failure message wasn't present after providing an incorrect password", login.failureMessagePreeent());
    }
}