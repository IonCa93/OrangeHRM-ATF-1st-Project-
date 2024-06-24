package runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"stepDefinitions", "Hooks"},
        monochrome = true,
        tags = "@UI or @sdsd and not @ignore", //TODO add StepNotifications
        plugin = {
                "pretty",
                "html:target/reports/Reports.html", //TODO add logs in reports
                "json:target/reports/Cucumber.json" } //TODO add git ignore for target folder!!! Why required all evidence in target, but not in project
)
public class TestRunner {
}