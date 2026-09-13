package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;

public class LoginTest extends BaseTest {

    private static final String VALID_USERNAME = "hany";
    private static final String VALID_PASSWORD = "P@ssw0rd";
    private static final String INVALID_USERNAME = "wrongUser";
    private static final String INVALID_PASSWORD = "wrongPass";
    private static final String LOGIN_FAILED_MESSAGE = "Login Failed!";

    @Test(groups = {"smoke"})
    public void loginWithValidCredentialsRedirectsToDashboard() {
        loginPage.setUsername(VALID_USERNAME);
        loginPage.setPassword(VALID_PASSWORD);
        DashboardPage dashboardPage = loginPage.login();

        Assert.assertTrue(driver.getCurrentUrl().contains("dashboard"), "URL did not redirect to dashboard");
        Assert.assertTrue(dashboardPage.isWelcomeTextDisplayed(), "Welcome text not visible");
    }

    @Test(groups = {"regression"})
    public void loginWithInvalidCredentialsShowsError() {
        loginPage.setUsername(INVALID_USERNAME);
        loginPage.setPassword(INVALID_PASSWORD);
        DashboardPage dashboardPage = loginPage.login();

        Assert.assertEquals(loginPage.getLoginErrorMessage(), LOGIN_FAILED_MESSAGE);
    }

    @Test(groups = {"regression"})
    public void logoutRedirectsToLoginPage() {

        loginPage.setUsername(VALID_USERNAME);
        loginPage.setPassword(VALID_PASSWORD);
        DashboardPage dashboardPage = loginPage.login();


        dashboardPage.logout();


        Assert.assertTrue(loginPage.isUsernameFieldDisplayed(), "Not redirected to login page after logout");
    }
}
