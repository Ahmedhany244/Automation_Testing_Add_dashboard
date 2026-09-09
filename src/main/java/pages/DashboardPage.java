package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DashboardPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private By welcomeText = By.xpath("//span[contains(text(),'Welcome')]");
    private By userDropdownTrigger = By.id("logout");           // the dropdown toggle
    private By logoutLink = By.xpath("//a[@href='logout']");    // the actual Logout menu item
    private By dashboardActionsTrigger = By.id("designSpaceActionMenu");
    private By cloneDashboardOption = By.xpath("//ul//a[contains(text(),'Clone Dashboard')]");
    private By editDashboardOption = By.xpath("//ul//a[contains(text(),'Edit Dashboard')]");
    private By deleteDashboardOption = By.xpath("//ul//a[contains(text(),'Delete Dashboard')]");
    private By confirmDeleteYesButton = By.cssSelector("button[data-bb-handler='confirm']");
    private By cancelDeleteNoButton = By.cssSelector("button[data-bb-handler='cancel']"); // useful for a "cancel delete" test later
    private By deleteSuccessMessage = By.xpath("//div[@class='message' and contains(text(),'deleted successfully')]");
    private By manageDashboardsButton = By.id("manageDashboard");
    private By addDashboardButton = By.id("addDashboard");
    private By addDashboardOption = By.cssSelector("a[data-test-id='dropdownButtonWrapper-menuItem ']");
    private By dashboardTitleLabel = By.className("navBtnText");
    private By dateRangeText = By.className("ev-auto-dateText");
    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isWelcomeTextDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(welcomeText)).isDisplayed();
    }
    public boolean isOnDashboardUrl() {
        return driver.getCurrentUrl().contains("dashboard");
    }

    public void logout() {

        WebElement trigger = wait.until(ExpectedConditions.elementToBeClickable(userDropdownTrigger));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", trigger);

        WebElement logout = wait.until(ExpectedConditions.presenceOfElementLocated(logoutLink));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", logout);
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("dashboard")));


    }

    public void openDashboardActions() {
        WebElement trigger = wait.until(ExpectedConditions.elementToBeClickable(dashboardActionsTrigger));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", trigger);
    }
    public void clickDeleteDashboard() {

        wait.until(ExpectedConditions.elementToBeClickable(deleteDashboardOption)).click();
    }
    public void confirmDelete() {
        WebElement yesBtn = wait.until(ExpectedConditions.presenceOfElementLocated(confirmDeleteYesButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", yesBtn);
    }
    public boolean isDeleteSuccessMessageDisplayed() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(deleteSuccessMessage)).isDisplayed();
    }
    public String getCurrentDashboardId() {
        String url = driver.getCurrentUrl();
        return url.replaceAll(".*id=(\\d+).*", "$1"); // extracts the id value from the URL
    }
    public ManageDashboardPage openManageDashboards() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(manageDashboardsButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table"))); // wait for table to render
        return new ManageDashboardPage(driver);

    }
    public AddDashboard clickAddDashboard() {
        WebElement trigger = wait.until(ExpectedConditions.elementToBeClickable(addDashboardButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", trigger);

        WebElement dashboardOption = wait.until(ExpectedConditions.presenceOfElementLocated(addDashboardOption));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dashboardOption);
        return new AddDashboard(driver);
    }
    public String getDisplayedDashboardName() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(dashboardTitleLabel)).getText();
    }


    public String getDisplayedDateRangeText() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(dateRangeText)).getText();
    }


}
