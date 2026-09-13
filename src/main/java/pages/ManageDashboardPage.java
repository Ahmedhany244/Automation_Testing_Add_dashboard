package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ManageDashboardPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private By confirmDeleteYesButton = By.cssSelector("button[data-bb-handler='confirm']");
    private By cancelDeleteNoButton = By.cssSelector("button[data-bb-handler='cancel']");
    private By dashboardSearchInput = By.cssSelector(".Dashboards-search-input");
    private By tableRows = By.cssSelector("table tbody tr");

    public ManageDashboardPage(WebDriver driver) {

        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    private By getDeleteButtonForDashboard(String dashboardName) {
        return By.xpath("//tr[td[contains(., '" + dashboardName + "')]]//button[@tooltip='Delete Dashboard']");
    }
    public void deleteDashboardByName(String dashboardName) {
        WebElement deleteBtn = wait.until(ExpectedConditions.presenceOfElementLocated(getDeleteButtonForDashboard(dashboardName)));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deleteBtn);
    }
    public void confirmDelete() {
        WebElement yesBtn = wait.until(ExpectedConditions.presenceOfElementLocated(confirmDeleteYesButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", yesBtn);
    }
    public void cancelDelete() {
        WebElement noBtn = wait.until(ExpectedConditions.presenceOfElementLocated(cancelDeleteNoButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", noBtn);
    }
    private By deleteSuccessMessage(String dashboardName) {
        return By.xpath("//div[@class='message' and contains(text(),'" + dashboardName + "') and contains(text(),'deleted successfully')]");
    }

    public boolean isDeleteSuccessMessageDisplayedFor(String dashboardName) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(deleteSuccessMessage(dashboardName))).isDisplayed();
    }
    public boolean isDashboardPresent(String dashboardName) {
        return !driver.findElements(getDeleteButtonForDashboard(dashboardName)).isEmpty();
    }

    public void searchDashboardByName(String dashboardName) {
        WebElement searchInput = wait.until(ExpectedConditions.presenceOfElementLocated(dashboardSearchInput));
        searchInput.clear();
        searchInput.sendKeys(dashboardName);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", searchInput
        );
        searchInput.sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.presenceOfElementLocated(dashboardSearchInput));


    }

    // Waits for the filtered table to have zero rows matching dashboardName, rather than checking
    // instantly like isDashboardPresent() does - the table re-render after typing into the search
    // box is not guaranteed to be synchronous, so an unwaited check right after searchDashboardByName()
    // could read stale DOM and report a false positive.


    public boolean isDashboardAbsentAfterSearch(String dashboardName) {
        try {
            wait.until(ExpectedConditions.numberOfElementsToBe(tableRows, 0));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

}
