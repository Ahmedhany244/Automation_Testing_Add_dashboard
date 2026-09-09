package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ManageDashboardPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private By confirmDeleteYesButton = By.cssSelector("button[data-bb-handler='confirm']");
    private By cancelDeleteNoButton = By.cssSelector("button[data-bb-handler='cancel']");

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


}
