package base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AdminUserManagementPage {
    WebDriver driver;
    private static final Logger logger = LogManager.getLogger(AdminUserManagementPage.class);

    public AdminUserManagementPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "(//input[@class='oxd-input oxd-input--active'])[2]")
    WebElement userNameInput;

    @FindBy(xpath = "//button[@class='oxd-button oxd-button--medium oxd-button--secondary']")
    WebElement addBtn;

    @FindBy(xpath = "(//div[@class='oxd-select-text-input'][normalize-space()='-- Select --'])[1]")
    WebElement userRole;

    @FindBy(xpath = "//div[@class='oxd-select-option']//span[text()='ESS']")
    WebElement ESSRoleOption;

    @FindBy(xpath = "//input[@placeholder='Type for hints...']")
    WebElement employeeName;

    @FindBy(xpath = "(//i[@class='oxd-icon bi-caret-down-fill oxd-select-text--arrow'])[2]")
    WebElement status;

    @FindBy(xpath = "(//div[@class='oxd-select-wrapper']//span[text()='Enabled']")
    WebElement enableOption;

    @FindBy(xpath = "//button[normalize-space()='Search']")
    WebElement searchButton;

    @FindBy(xpath = "(//i[@class='oxd-icon bi-trash'])[5]")
    WebElement deleteIcon;

    @FindBy(xpath = "//button[normalize-space()='Yes, Delete']")
    WebElement deleteConfirmationsButton;

    @FindBy(xpath = "//div[@class='oxd-toast-content oxd-toast-content--success']")
    WebElement deleteConfirmationPopup;



//methods to perform actions on the elements
    public void clickAddBtn() {
        logger.info("Clicking on Add button");
        addBtn.click();
    }

    public void selectUserRole() {
        userRole.click();
        ESSRoleOption.click();
    }
    public void enterUserName(String userName) {
        logger.info("Entering username: " + userName);
        userNameInput.sendKeys(userName);
    }
    public void enterEmployeeName(String employeeName)  {
        logger.info("Entering employee name: " + employeeName);
        this.employeeName.sendKeys(employeeName, Keys.ENTER);


    }
    public void selectStatus() {
        status.click();
        enableOption.click();
    }
    public void clickSearchButton() {
        searchButton.click();
    }

    public void deleteUser(){
        logger.info("Deleting the user");
        deleteIcon.click();
    }
    public void confirmDeleteUser(){
        logger.info("Confirming deletion");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(deleteConfirmationsButton));
        deleteConfirmationsButton.click();
    }
    public boolean isDeleteConfirmationPopupDisplayed(){
        return deleteConfirmationPopup.isDisplayed();
    }















}
