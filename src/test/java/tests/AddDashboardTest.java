package tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import pages.AddDashboard;
import pages.ManageDashboardPage;

public class AddDashboardTest extends AuthenticatedBaseTest {

    private static final Logger LOGGER = LogManager.getLogger(AddDashboardTest.class);

    public String dashboardName = "TestDashboard_AH" + System.currentTimeMillis();
    public String dashboardDescription = "Created by automated test ";
    String landingDateRange = "Last 7 Days";
    String startDay = "11";
    String startPanel = "left";
    String endDay = "15";
    String endPanel = "left";
    String expectedDateRangeText = "11/09/26 00:00  -  15/09/26 23:59";

    // Set right after clickSubmit() in each test - null means nothing was actually created
    // this run (the test failed before reaching submission), non-null is the name to clean up.
    private String createdDashboardName;

    @Test(groups = {"smoke"})
    public void addDashboardCreatesItSuccessfully() {
        AddDashboard addDashboardPage = dashboardPage.clickAddDashboard();
        addDashboardPage.fillDashboardForm(dashboardName, dashboardDescription);
        addDashboardPage.setLandingDateRange(landingDateRange);
        addDashboardPage.clickSubmit();
        createdDashboardName = dashboardName;

        Assert.assertTrue(
                addDashboardPage.isAddSuccessMessageDisplayed(),
                "Success message not shown after adding dashboard"
        );

        Assert.assertEquals(
                dashboardPage.getDisplayedDashboardName(),
                dashboardName,
                "Displayed dashboard name does not match the one that was created"
        );
        Assert.assertEquals(
                dashboardPage.getDisplayedDateRangeText(),
                landingDateRange,
                "Displayed date range does not match the selected preset"
        );

    }

    @Test(groups = {"regression"})
    public void addDashboardWithCustomDateRange() {
        AddDashboard addDashboardPage = dashboardPage.clickAddDashboard();

        addDashboardPage.fillDashboardForm(dashboardName, dashboardDescription);
        addDashboardPage.selectCustomDateRange(startDay, startPanel, endDay, endPanel);
        addDashboardPage.clickSubmit();
        createdDashboardName = dashboardName;

        Assert.assertEquals(
                dashboardPage.getDisplayedDateRangeText().replaceAll("\\s+", " ").trim(),
                expectedDateRangeText.replaceAll("\\s+", " ").trim(),
                "Displayed date range does not match the selected custom range"
        );

        Assert.assertTrue(
                addDashboardPage.isAddSuccessMessageDisplayed(),
                "Success message not shown after adding dashboard with custom date range"
        );
    }

    @AfterMethod(alwaysRun = true)
    public void cleanupCreatedDashboard() {
        if (createdDashboardName == null) {
            // clickSubmit() was never reached (e.g. the form-fill step failed) - nothing to clean up.
            return;
        }
        try {
            ManageDashboardPage manageDashboardPage = dashboardPage.openManageDashboards();
            manageDashboardPage.deleteDashboardByName(createdDashboardName);
            manageDashboardPage.confirmDelete();
        } catch (Exception e) {
            // Best-effort cleanup: don't let a cleanup failure mask the test's actual result.
            LOGGER.warn("Cleanup failed to delete dashboard '{}': {}", createdDashboardName, e.getMessage());
        } finally {
            createdDashboardName = null;
        }
    }
}
