package testClasses;

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

public class LoginTest extends BaseTest {
    WebDriver driver;
    String baseUrl = "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login";
    private static final Logger logger = LogManager.getLogger(LoginTest.class);
    LoginPage loginPage;
    DashboardPage dashboardPage;

    @BeforeClass
    @Parameters("browser")
    public void setUp(String browser) {
        super.initExtentReports("LoginPageExtentReport"); // Initialize Extent Reports
        driver = launch_browser(browser);
        driver.manage().window().maximize();
        driver.get(baseUrl);
        loginPage = new LoginPage(driver);
        dashboardPage = new DashboardPage(driver);
    }


    @Test(priority =1)
    public void testValidLogin() {
        test = extent.createTest("Test Valid Login", "Verify that a user can successfully log in with valid credentials");
        logger.info("Starting test: Valid Login");
    
        // Perform login with valid credentials
        if (loginPage != null) {
            test.log(Status.INFO, "Login page is initialized.");
            test.log(Status.INFO, "valid credentials is being entered.");
            loginPage.login("Admin", "admin123");
            test.log(Status.INFO, "Valid credentials entered.");

        } else {
            test.log(Status.FAIL, "Login page is not initialized.");
            logger.error("Login page is not initialized.");
            Assert.fail("Login page is not initialized.");
            return;
        }
    
        // Verify if the user is redirected to the dashboard page
        String expectedUrl = "https://opensource-demo.orangehrmlive.com/web/index.php/dashboard/index";
        String currentUrl = driver.getCurrentUrl();
    
        if (currentUrl != null && currentUrl.equals(expectedUrl)) {
            test.log(Status.PASS, "Valid login test passed, user redirected to dashboard.");
            logger.info("Login successful, user redirected to dashboard.");
        } else {
            test.log(Status.FAIL, "Login failed! User is not redirected to the dashboard. Current URL: " + currentUrl);
            logger.error("Login failed, expected URL: " + expectedUrl + " but got: " + currentUrl);
            Assert.fail("Login failed! User was not redirected to the dashboard.");
        }
    }

    @Test(priority =2)
    public void testInvalidLogin() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='oxd-userdropdown-tab']")));
            dashboardPage.clickUserDropdown();
            dashboardPage.clickLogoutLink();
        } catch (TimeoutException e) {
            logger.warn("User dropdown not clickable, proceeding with invalid login test");
        }

        test = extent.createTest("Test Invalid Login", "Verify that a user cannot log in with invalid credentials and error message is displayed.");
        logger.info("Starting test: Invalid Login");
        // Perform login with invalid credentials
        if (loginPage != null) {
            test.log(Status.INFO, "Login page is initialized.");
            test.log(Status.INFO, "invalid credentials is being entered.");
            loginPage.login("Admin", "invalid_password");
            test.log(Status.INFO, "Invalid credentials entered.");
        } else {
            test.log(Status.FAIL, "Login page is not initialized.");
            logger.error("Login page is not initialized.");
            Assert.fail("Login page is not initialized.");
            return;
        }
        // Verify if the login failed with an error message
        if (loginPage.isInvalidCredentialsMessageDisplayed()) {
            String expectedErrorText = "Invalid credentials";
            Assert.assertEquals(loginPage.textOfCredentialsMessage(),expectedErrorText,"invalid error message is displayed");
            test.log(Status.PASS, "Invalid login test passed, error message displayed with valid error message.");
            logger.info("Invalid login test passed, error message displayed.");
        } else {
            test.log(Status.FAIL, "Invalid login failed! Error message not displayed.");
            logger.error("Invalid login failed, error message not displayed.");
            Assert.fail("Invalid login failed! Error message not displayed.");
        }
    }
    @AfterMethod
    public void result(ITestResult testResult) {
        if (testResult.getStatus() == ITestResult.FAILURE) {
            String filename = "screenshot_" + generateRandomString(10) + ".png";
            System.out.println("Screenshot file name: " + filename);
            String directory = System.getProperty("user.dir") + ".//LoginFailedScreenshots//";

            File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                FileUtils.copyFile(sourceFile, new File(directory + filename));
                test.addScreenCaptureFromPath('.'+directory + filename);
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
