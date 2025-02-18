package base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {
    WebDriver driver;
    private static final Logger logger = LogManager.getLogger(LoginPage.class);

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//input[@placeholder='Username']")
    WebElement userNameTextField;

    @FindBy(xpath = "//input[@placeholder='Password']")
    WebElement passwordTextField;

    @FindBy(xpath = "//button[normalize-space()='Login']")
    WebElement loginButton;

    @FindBy(xpath = "//p[@class='oxd-text oxd-text--p oxd-alert-content-text']")
    WebElement invalidCredentialsErrorMessage;

    public void login(String username, String password) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(userNameTextField)).sendKeys(username);
        logger.info("Entered username: " + username);

        wait.until(ExpectedConditions.elementToBeClickable(passwordTextField)).sendKeys(password);
        logger.info("Entered password: " + password);

        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
        logger.info("Clicked on Login button");
    }

    // Check for invalid credentials error message
    public Boolean isInvalidCredentialsMessageDisplayed() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.visibilityOf(invalidCredentialsErrorMessage)).isDisplayed();

    }
    public String textOfCredentialsMessage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.visibilityOf(invalidCredentialsErrorMessage)).getText();

    }






}
