package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AddDashboard;
import pages.DashboardPage;

public class AddDashboardTest extends BaseTest{
    private static final String VALID_USERNAME = "hany";
    private static final String VALID_PASSWORD = "P@ssw0rd";
    public String dashboardName = "TestDashboard_AH" + System.currentTimeMillis();
    public String dashboardDescription = "Created by automated test ";
    String landingDateRange = "Last 7 Days";
    String startDay = "11";
    String startPanel = "left";
    String endDay = "15";
    String endPanel = "left";
    String expectedDateRangeText = "11/09/26 00:00  -  15/09/26 23:59";
    @Test
    public void addDashboardCreatesItSuccessfully() {
        loginPage.setUsername(VALID_USERNAME);
        loginPage.setPassword(VALID_PASSWORD);
        DashboardPage dashboardPage = loginPage.login();
        AddDashboard addDashboardPage = dashboardPage.clickAddDashboard();
        addDashboardPage.fillDashboardForm(dashboardName, dashboardDescription);
        addDashboardPage.setLandingDateRange(landingDateRange);
        addDashboardPage.clickSubmit();

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

    @Test
    public void addDashboardWithCustomDateRange() {
        loginPage.setUsername(VALID_USERNAME);
        loginPage.setPassword(VALID_PASSWORD);
        DashboardPage dashboardPage = loginPage.login();
        AddDashboard addDashboardPage = dashboardPage.clickAddDashboard();

        addDashboardPage.fillDashboardForm(dashboardName, dashboardDescription);
        addDashboardPage.selectCustomDateRange(startDay, startPanel, endDay, endPanel);
        addDashboardPage.clickSubmit();
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
}
