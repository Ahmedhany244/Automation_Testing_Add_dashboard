package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import pages.ManageDashboardPage;

public class DeleteTest extends  BaseTest {
    private static final String VALID_USERNAME = "hany";
    private static final String VALID_PASSWORD = "P@ssw0rd";
    private String dashboardName="ABC";
    @Test
    public void deleteDashboardFromDashBoard() {
        loginPage.setPassword(VALID_PASSWORD);
        loginPage.setUsername(VALID_USERNAME);
        DashboardPage dashboardpage=loginPage.login();
        String dashboardIdBeforeDelete = dashboardpage.getCurrentDashboardId();
        dashboardpage.openDashboardActions();
        dashboardpage.clickDeleteDashboard();
        dashboardpage.confirmDelete();
        Assert.assertTrue(dashboardpage.isDeleteSuccessMessageDisplayed(), "Delete success message not shown");
        Assert.assertNotEquals(dashboardpage.getCurrentDashboardId(), dashboardIdBeforeDelete, "URL still shows the deleted dashboard's ID");

    }
    @Test
    public void deleteDashboardFromManageDashboard() {
        loginPage.setPassword(VALID_PASSWORD);
        loginPage.setUsername(VALID_USERNAME);
        DashboardPage dashboardpage=loginPage.login();
        ManageDashboardPage managedashboard=dashboardpage.openManageDashboards();
        managedashboard.deleteDashboardByName(dashboardName);
        managedashboard.confirmDelete();
        Assert.assertTrue(managedashboard.isDeleteSuccessMessageDisplayedFor(dashboardName),
                "Success message did not confirm deletion of '" + dashboardName + "'");

    }

    @Test
    public void cancelDeleteKeepsDashboard() {
        loginPage.setUsername(VALID_USERNAME);
        loginPage.setPassword(VALID_PASSWORD);
        DashboardPage dashboardPage = loginPage.login();
        ManageDashboardPage manageDashboardPage = dashboardPage.openManageDashboards();
        manageDashboardPage.deleteDashboardByName(dashboardName);
        manageDashboardPage.cancelDelete();

        Assert.assertTrue(
                manageDashboardPage.isDashboardPresent(dashboardName),
                "Dashboard '" + dashboardName + "' was removed even though delete was cancelled"
        );
    }
}
