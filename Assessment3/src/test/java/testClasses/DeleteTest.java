package testClasses;

import base.AdminUserManagementPage;
import base.BaseTest;
import base.DashboardPage;
import base.LoginPage;
import com.aventstack.extentreports.Status;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class DeleteTest extends BaseTest {
    WebDriver driver;
    String baseUrl = "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login";
    private static final Logger logger = LogManager.getLogger(DeleteTest.class);

    LoginPage loginPage;
    DashboardPage dashboardPage;
    AdminUserManagementPage adminUserManagementPage;

    @BeforeClass
    @Parameters("browser")
    public void setUp(String browser) {
        super.initExtentReports("DeleteTestExtentReport");
        driver = launch_browser(browser);
        driver.manage().window().maximize();
        driver.get(baseUrl);
        loginPage = new LoginPage(driver);
        dashboardPage = new DashboardPage(driver);
        adminUserManagementPage = new AdminUserManagementPage(driver);
        logger.info("Logging in as Admin...");
        loginPage.login("Admin", "admin123");
        logger.info("Navigating to Admin -> User Management");
        dashboardPage.clickAdminTabLink();
    }

    @Test
    public void deleteUserTest() {
        test = extent.createTest("Delete User Test", "Verify that an Admin can delete a user successfully");

        logger.info("Attempting to delete a user...");
        test.log(Status.INFO, "Attempting to delete a user...");

        adminUserManagementPage.deleteUser();
        test.log(Status.INFO, "Clicked delete button");

        adminUserManagementPage.confirmDeleteUser();
        test.log(Status.INFO, "Confirmed deletion");

        // Verification: Check if user is deleted
        boolean isUserDeleted = adminUserManagementPage.isDeleteConfirmationPopupDisplayed();
        Assert.assertTrue(isUserDeleted, "User deletion failed!");

        logger.info("User deleted successfully");
        test.log(Status.PASS, "User deleted successfully");
    }

    @AfterMethod
    public void result(ITestResult testResult) {
        if (testResult.getStatus() == ITestResult.FAILURE) {
            String filename = "screenshot_" + System.currentTimeMillis() + ".png";
            String directory = System.getProperty("user.dir") + ".//DeleteFailedScreenshots//";

            File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                File directoryPath = new File(directory);
                if (!directoryPath.exists()) {
                    directoryPath.mkdirs(); // Create directory if it doesn't exist
                }
                FileUtils.copyFile(sourceFile, new File(directory + filename));
                test.addScreenCaptureFromPath('.'+directory + filename);
                logger.error("Test failed. Screenshot captured: " + filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (testResult.getStatus() == ITestResult.SUCCESS) {
            logger.info("PASSED: " + testResult.getName());
        }
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
        test.log(Status.INFO, "Closed the browser.");
        logger.info("Browser closed.");
        super.flushExtentReports();
    }
}
