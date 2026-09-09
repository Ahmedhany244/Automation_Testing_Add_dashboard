package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private By username = By.name("username");
    private By password = By.name("password");
    private By loginButton = By.cssSelector(".submitForm-btn");
    private By loginFailedAlert = By.cssSelector(".ev-auto-alertTitle");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void setUsername(String usernameText) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(username)).sendKeys(usernameText);
    }

    public void setPassword(String passwordText) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(password)).sendKeys(passwordText);
    }

    public DashboardPage login() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();

        // Don't assume success: wait for whichever shows up first —
        // the dashboard URL (success) or the "Login Failed!" alert (failure).
        // This fails fast on bad credentials instead of waiting out the full timeout.
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("dashboard"),
                ExpectedConditions.visibilityOfElementLocated(loginFailedAlert)
        ));

        return new DashboardPage(driver);
    }



    public String getLoginErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(loginFailedAlert)).getText();
    }
    public boolean isUsernameFieldDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(username)).isDisplayed();
    }
}
