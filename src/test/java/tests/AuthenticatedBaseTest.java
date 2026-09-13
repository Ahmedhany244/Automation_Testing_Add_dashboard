package tests;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import pages.DashboardPage;
import pages.LoginPage;

/**
 * Base class for test classes that need to start every test already authenticated and don't
 * exercise the login flow itself. Logs in ONCE per class (@BeforeClass), reusing the same
 * browser/session for every @Test method in the class - unlike BaseTest, which creates a fresh
 * driver and re-logs in before every single method.
 *
 * Each test method still gets a clean DOM to start from: @BeforeMethod re-navigates back to the
 * app's base URL before every method. Because the session is already authenticated, this reloads
 * straight to a dashboard (no re-login) but still discards any leftover open dropdown/modal/search
 * filter from the previous test - a full page reload (cheap), not a full re-login (typing
 * credentials + submit + redirect wait - the expensive part this class exists to avoid).
 *
 * Deliberately re-navigates to the base URL rather than a cached post-login URL: some tests
 * (e.g. deleting "the currently open dashboard") can invalidate whatever dashboard was current at
 * login time, so pinning a specific post-login URL/ID could reset methods onto a now-deleted
 * dashboard. Relying on the app's own "redirect to current dashboard" behavior avoids that.
 *
 * What this does NOT reset: any server-side data mutation a test performs (e.g. deleting a
 * dashboard) is still visible to every later test in the class (and even later classes) exactly
 * as it always was - BaseTest's per-method driver recreation never reset server-side state either,
 * only the browser. Tests that depend on specific data existing must not assume an earlier test
 * in the same class hasn't already mutated or removed it.
 *
 * Deliberately does NOT extend BaseTest: BaseTest's own @BeforeMethod would still be inherited and
 * run before every method, defeating the point. Reuses BaseTest.createChromeDriver() instead,
 * which is the only piece actually worth sharing.
 *
 * Classes that need to exercise the login/logout flow itself (LoginTest, LogoutTest) should keep
 * extending BaseTest directly - they need control over the unauthenticated state per method.
 */
public class AuthenticatedBaseTest {

    protected static final String USERNAME = "hany";
    protected static final String PASSWORD = "P@ssw0rd";

    protected WebDriver driver;
    protected DashboardPage dashboardPage;

    @BeforeClass
    public void loginOnce() {
        driver = BaseTest.createChromeDriver();
        driver.manage().window().maximize();
        driver.get(BaseTest.APP_URL);

        LoginPage loginPage = new LoginPage(driver);
        loginPage.setUsername(USERNAME);
        loginPage.setPassword(PASSWORD);
        dashboardPage = loginPage.login();
    }

    @BeforeMethod
    public void resetToDashboard() {
        driver.get(BaseTest.APP_URL);
        dashboardPage.isWelcomeTextDisplayed(); // block until the reloaded page has settled
    }

    @AfterClass
    public void tearDownClass() {
        if (driver != null) {
            driver.quit();
        }
    }
}
