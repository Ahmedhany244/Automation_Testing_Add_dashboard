package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;

public class LogoutTest extends BaseTest{
    private static final String VALID_USERNAME = "hany";
    private static final String VALID_PASSWORD = "P@ssw0rd";
    @Test(groups = {"smoke"})
    public void logoutRedirectsToLoginPage()   {


        loginPage.setUsername(VALID_USERNAME);
        loginPage.setPassword(VALID_PASSWORD);
        DashboardPage dashboardPage = loginPage.login();


        dashboardPage.logout();

        Assert.assertFalse(dashboardPage.isOnDashboardUrl(), "Still on dashboard after logout");
        Assert.assertTrue(loginPage.isUsernameFieldDisplayed(), "Not redirected to login page after logout");

    }
}
