package tests;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AddDashboard;
import pages.ManageDashboardPage;

public class DeleteTest extends AuthenticatedBaseTest {

    private static final String DASHBOARD_DESCRIPTION = "Created by automated test ";
    private static final String LANDING_DATE_RANGE = "Last 7 Days";

    private String newDashboardName() {
        return "TestDashboard_AH" + System.currentTimeMillis();
    }

    @Test(groups = {"smoke"})
    public void deleteDashboardFromDashBoard() {
        String dashboardName = newDashboardName();
        AddDashboard addDashboardPage = dashboardPage.clickAddDashboard();
        addDashboardPage.fillDashboardForm(dashboardName, DASHBOARD_DESCRIPTION);
        addDashboardPage.setLandingDateRange(LANDING_DATE_RANGE);
        addDashboardPage.clickSubmit();
        Assert.assertTrue(addDashboardPage.isAddSuccessMessageDisplayed(),
                "Setup failed: dashboard '" + dashboardName + "' was not created before running the delete flow");

        String dashboardIdBeforeDelete = dashboardPage.getCurrentDashboardId();
        dashboardPage.openDashboardActions();
        dashboardPage.clickDeleteDashboard();
        dashboardPage.confirmDelete();
        Assert.assertTrue(dashboardPage.isDeleteSuccessMessageDisplayed(), "Delete success message not shown");
        Assert.assertNotEquals(dashboardPage.getCurrentDashboardId(), dashboardIdBeforeDelete, "URL still shows the deleted dashboard's ID");

    }
    @Test(groups = {"smoke"})
    public void deleteDashboardFromManageDashboard() {
        String dashboardName = newDashboardName();
        AddDashboard addDashboardPage = dashboardPage.clickAddDashboard();
        addDashboardPage.fillDashboardForm(dashboardName, DASHBOARD_DESCRIPTION);
        addDashboardPage.setLandingDateRange(LANDING_DATE_RANGE);
        addDashboardPage.clickSubmit();
        Assert.assertTrue(addDashboardPage.isAddSuccessMessageDisplayed(),
                "Setup failed: dashboard '" + dashboardName + "' was not created before running the delete flow");

        ManageDashboardPage managedashboard=dashboardPage.openManageDashboards();
        managedashboard.deleteDashboardByName(dashboardName);
        managedashboard.confirmDelete();
        Assert.assertTrue(managedashboard.isDeleteSuccessMessageDisplayedFor(dashboardName),
                "Success message did not confirm deletion of '" + dashboardName + "'");
        managedashboard.searchDashboardByName(dashboardName);
        // DEBUG - print everything visible in the table area right now
        System.out.println("=== TABLE CONTENT AFTER SEARCH ===");
        System.out.println(driver.findElement(By.tagName("table")).getText());
        System.out.println("=== FULL PAGE SOURCE SNIPPET ===");
        System.out.println(driver.getPageSource().substring(0, Math.min(3000, driver.getPageSource().length())));
        Assert.assertTrue(managedashboard.isDashboardAbsentAfterSearch(dashboardName),
                "Dashboard '" + dashboardName + "' still appears in Manage Dashboards search results after deletion");
    }
}
