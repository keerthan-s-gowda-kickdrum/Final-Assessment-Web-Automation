package testClasses;

import base.AdminUserManagementPage;
import base.BaseTest;
import base.DashboardPage;
import base.LoginPage;
import com.aventstack.extentreports.Status;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class AdminTest extends BaseTest {
    WebDriver driver;
    String baseUrl = "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login";
    private static final Logger logger = LogManager.getLogger(AdminTest.class);

    LoginPage loginPage;
    DashboardPage dashboardPage;
    AdminUserManagementPage adminUserManagementPage;

    @BeforeClass
    @Parameters("browser")
    public void setUp(String browser) {
        super.initExtentReports("AdminTestExtentReport");
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

    @Test(priority = 1)
    public void searchUser() {
        String searchUserName = "Keerthana";
        //test is intentionally designed to fail to verify Extent Report captures failure screenshot.
        test = extent.createTest("Search User", "Verify that a user can search for an existing username");
        logger.info("Entering username in the search field: " + searchUserName);
        test.log(Status.INFO, "Entering username in search field: " + searchUserName);

        adminUserManagementPage.enterUserName(searchUserName);
        test.log(Status.INFO, "selecting the user role");
        adminUserManagementPage.selectUserRole();
        test.log(Status.INFO, "clicking the search button");
        adminUserManagementPage.clickSearchButton();

        logger.info("Verifying search results for user: " + searchUserName);
        test.log(Status.INFO, "Verifying search results");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement tableBody = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@class='oxd-table-body']")));

        List<WebElement> rows = tableBody.findElements(By.xpath(".//div[@role='row']"));

        boolean userFound = false;
        for (WebElement row : rows) {
            WebElement usernameCell = row.findElement(By.xpath("//div[@role='cell'][2]"));
            String displayedUsername = usernameCell.getText();
            if (displayedUsername.equalsIgnoreCase(searchUserName)) {
                userFound = true;
                logger.info("User '" + searchUserName + "' found in search results.");
                test.log(Status.PASS, "User '" + searchUserName + "' found in search results.");
                break;
            }
        }

        if (!userFound) {
            //Intentional failure: The searched user is not displayed in the results to simulate a test failure.
            logger.error("User '" + searchUserName + "' NOT found in search results.");
            test.log(Status.FAIL, "User '" + searchUserName + "' was NOT found.");
            Assert.fail("User '" + searchUserName + "' was not found in search results.");
        }
    }


    @AfterMethod
    public void result(ITestResult testResult) {
        if (testResult.getStatus() == ITestResult.FAILURE) {
            String filename = "screenshot_" + generateRandomString(10) + ".png";
            System.out.println("Screenshot file name: " + filename);
            String directory = System.getProperty("user.dir") + "//FailedScreenshots//";

            File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                FileUtils.copyFile(sourceFile, new File(directory + filename));
                test.addScreenCaptureFromPath("..//FailedScreenshots//" + filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (testResult.getStatus() == ITestResult.SUCCESS) {
            System.out.println("PASSED : " + testResult.getName());
        }
    }

    private String generateRandomString(int size) {
        boolean useLetters = true;
        boolean useNumbers = true;
        return RandomStringUtils.random(size, useLetters, useNumbers);
    }
   
   @AfterClass
   public void tearDown() {
       driver.quit();
       test.log(Status.INFO, "Closed the browser.");
       logger.info("Browser closed.");
       super.flushExtentReports();
   }
}
