package base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DashboardPage {
    WebDriver driver;
    private static final Logger logger = LogManager.getLogger(DashboardPage.class);

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//span[@class='oxd-userdropdown-tab']")
    WebElement userDropdown;
    @FindBy(xpath = "//a[normalize-space()='Logout']")
    WebElement logoutLink;
    @FindBy(xpath = "//a[@href='/web/index.php/admin/viewAdminModule']")
    WebElement adminTabLink;

    public void clickUserDropdown() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(userDropdown)).click();
        logger.info("Clicked on userDropdown");
    }

    public void clickLogoutLink() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
        logger.info("Clicked on logoutLink");
    }

    public void clickAdminTabLink() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(adminTabLink)).click();
        logger.info("Clicked on adminTabLink");
    }

}
