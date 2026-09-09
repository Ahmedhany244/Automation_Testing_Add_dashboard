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

    protected WebDriver driver;
    protected LoginPage loginPage;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);       // disables "Save password?" prompt
        prefs.put("profile.password_manager_enabled", false);  // disables password manager entirely
        prefs.put("profile.password_manager_leak_detection", false); // disables the breach-warning popup you're seeing
        options.setExperimentalOption("prefs", prefs);
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get("http://192.168.125.30:8080/#/reporter/");
        loginPage = new LoginPage(driver);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
