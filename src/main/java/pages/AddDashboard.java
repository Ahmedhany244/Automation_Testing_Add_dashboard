package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;


public class AddDashboard {
    private WebDriver driver;
    private WebDriverWait wait;
    private By dashboardNameInput = By.xpath("//label[text()='Dashboard Name']/following::input[1]");
    private By dashboardDescriptionInput = By.name("description");
    private By cancelButton = By.cssSelector(".cancelForm-btn");
    private By submitButton = By.cssSelector(".submitForm-btn");
    private By addSuccessMessage = By.xpath("//div[@class='message' and contains(text(),'added successfully')]");
    private By landingDateRangeTrigger = By.className("ev-auto-timeLabelWrapper");
    private By dateRangeApplyButton = By.cssSelector(".applyBtn");
    private By dateRangeCancelButton = By.cssSelector(".cancelBtn");

    public AddDashboard(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    public void enterDashboardName(String name) {
        WebElement nameField = wait.until(ExpectedConditions.presenceOfElementLocated(dashboardNameInput));
        nameField.clear();
        nameField.sendKeys(name);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", nameField
        );
    }


    public void enterDashboardDescription(String description) {
        WebElement descField = wait.until(ExpectedConditions.presenceOfElementLocated(dashboardDescriptionInput));
        descField.clear();
        descField.sendKeys(description);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", descField);
        js.executeScript("arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", descField);
        js.executeScript("arguments[0].dispatchEvent(new Event('blur', { bubbles: true }));", descField);
    }

    public void fillDashboardForm(String name, String description) {
        enterDashboardName(name);
        enterDashboardDescription(description);
    }

    public void clickCancel() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(cancelButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    }

    public void clickSubmit() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    }
    public boolean isAddSuccessMessageDisplayed() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(addSuccessMessage)).isDisplayed();
    }
    private By dateRangeOption(String rangeLabel) {
        return By.cssSelector("li[data-range-key='" + rangeLabel + "']");
    }
    public void openLandingDateRangePicker() {
        WebElement trigger = wait.until(ExpectedConditions.elementToBeClickable(landingDateRangeTrigger));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", trigger);
    }

    public void selectDateRangeOption(String rangeLabel) {
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(dateRangeOption(rangeLabel)));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
    }

    public void clickApplyDateRange() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(dateRangeApplyButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    }

    public void clickCancelDateRange() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(dateRangeCancelButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    }

    public void setLandingDateRange(String rangeLabel) {
        openLandingDateRangePicker();
        selectDateRangeOption(rangeLabel);

    }
    private By dateCellOnCalendar(String panel, String day) {
        // panel should be "left" or "right"
        return By.xpath("//div[contains(@class,'calendar') and contains(@class,'" + panel + "')]//td[contains(@class,'available') and not(contains(@class,'off')) and text()='" + day + "']");
    }
    public void selectCustomDateRange(String startDay, String startPanel, String endDay, String endPanel) {
        openLandingDateRangePicker();
        selectDateRangeOption("Custom Range");

        wait.until(ExpectedConditions.elementToBeClickable(dateCellOnCalendar(startPanel, startDay))).click();
        wait.until(ExpectedConditions.elementToBeClickable(dateCellOnCalendar(endPanel, endDay))).click();
        clickApplyDateRange();
    }

}
