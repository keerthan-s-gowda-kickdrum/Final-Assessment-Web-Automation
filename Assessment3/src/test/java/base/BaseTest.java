package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.time.Duration;

public class BaseTest {
    protected WebDriver driver; // Changed to protected
    protected ExtentReports extent; // Changed to protected
    protected ExtentTest test; // Changed to protected

    public WebDriver launch_browser(String browser_type) {
        switch (browser_type.toLowerCase()) {
            case "chrome":
                driver = new ChromeDriver();
                break;
            case "firefox":
                driver = new FirefoxDriver();
                break;
            case "edge":
                driver = new EdgeDriver();
                break;
            default:
                driver = new ChromeDriver();
                break;
        }

        System.out.println("Launched the browser type: " + browser_type);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        return driver;
    }

    public void initExtentReports(String reportName) {
        extent = new ExtentReports();
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("ExtentReports/" + reportName + ".html");
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Project Name", "Assessment");
        extent.setSystemInfo("Organisation", "KickdrumTech");
    }

    // Close ExtentReports
    public void flushExtentReports() {
        if (extent != null) {
            extent.flush();
        }
    }
}
