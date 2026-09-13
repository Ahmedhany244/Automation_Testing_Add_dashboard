package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pages.LoginPage;

import java.util.HashMap;
import java.util.Map;

public class BaseTest {

    protected static final String APP_URL = "http://192.168.125.30:8080/#/reporter/";

    protected WebDriver driver;
    protected LoginPage loginPage;

    @BeforeMethod
    public void setUp() {
        driver = createChromeDriver();
        driver.manage().window().maximize();
        driver.get(APP_URL);
        loginPage = new LoginPage(driver);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Shared with AuthenticatedBaseTest, which needs its own @BeforeClass/@BeforeMethod lifecycle
    // rather than this class's per-method one - factored out so that class doesn't have to
    // duplicate the ChromeOptions setup, without extending this class (extending it would pull in
    // this class's own @BeforeMethod too, since TestNG inherits annotated lifecycle methods unless
    // the same signature is overridden).
    protected static WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);       // disables "Save password?" prompt
        prefs.put("profile.password_manager_enabled", false);  // disables password manager entirely
        prefs.put("profile.password_manager_leak_detection", false); // disables the breach-warning popup you're seeing
        options.setExperimentalOption("prefs", prefs);
        return new ChromeDriver(options);
    }
}
