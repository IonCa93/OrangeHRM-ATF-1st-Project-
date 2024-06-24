package pages;

import actions.UIActions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.PropertiesUtil;

import java.time.Duration;

public abstract class BasePage {

    protected final WebDriver driver;

    protected final UIActions UIActions;

    protected final WebDriverWait wait;

    private static final int TIMEOUT_SECONDS = Integer.parseInt(PropertiesUtil.getProperty("timeout.seconds"));

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.UIActions = new UIActions(driver);
        PageFactory.initElements(driver, this);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_SECONDS));
    }
}
