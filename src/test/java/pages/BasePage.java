package pages;

import actions.UIActions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.PropertiesUtil;

import java.time.Duration;

public abstract class BasePage {

    protected final WebDriver driver; //TODO dif btw public/protected/default
    protected final UIActions browserActions;
    protected final WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.browserActions = new UIActions(driver);
        PageFactory.initElements(driver, this);
        long timeoutSeconds = getTimeoutFromConfig();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    private long getTimeoutFromConfig() {
        try {
            String timeoutString = PropertiesUtil.getProperty("timeout.seconds");
            return Long.parseLong(timeoutString);
        } catch (NumberFormatException ex) {
            // Handle exceptions gracefully, maybe fallback to a default value
            ex.printStackTrace();
            return 30; // Default to 30 seconds if reading from config fails
        }
    }

}
