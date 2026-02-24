package com.myrent.tests.login;

import com.myrent.framework.base.BaseDriver;
import com.myrent.framework.config.ConfigReader;
import com.myrent.framework.pages.LoginPage;
import com.myrent.tests.base.BaseTest;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(description = "Valid user can login successfully")
    public void testValidLogin() {

        // 1. Create Page Object
        LoginPage loginPage = new LoginPage(BaseDriver.getDriver());

        // 2. Perform Login
        loginPage.login(
            ConfigReader.get("j_username"),
            ConfigReader.get("j_password"),
            ConfigReader.get("company")
        );

        // 3. Verify redirected to Home page
        String expectedUrl = ConfigReader.get("homeUrl");
        String actualUrl = BaseDriver.getDriver().getCurrentUrl();
        Assert.assertTrue(
            actualUrl.contains(ConfigReader.get("homeUrl")),
            "Login failed! Expected URL to contain: " + ConfigReader.get("homeUrl") 
            + " but got: " + actualUrl
        );
        Assert.assertEquals(actualUrl, expectedUrl, "Login failed! URL mismatch.");
    }

    @Test(description = "UI elements are visible on Login Page")
    public void testLoginPageUI() {

        LoginPage loginPage = new LoginPage(BaseDriver.getDriver());

        // Verify all fields are displayed
        Assert.assertTrue(
            BaseDriver.getDriver().findElement(By.id("j_username")).isDisplayed(),
            "Username field not visible"
        );
        Assert.assertTrue(
            BaseDriver.getDriver().findElement(By.id("j_password")).isDisplayed(),
            "Password field not visible"
        );
        Assert.assertTrue(
            BaseDriver.getDriver().findElement(By.id("company")).isDisplayed(),
            "Company Code field not visible"
        );
        Assert.assertTrue(
            BaseDriver.getDriver().findElement(By.cssSelector("button[type='submit']")).isDisplayed(),
            "Sign In button not visible"
        );
    }
}