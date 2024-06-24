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
        tags = "@UI or @dsdsd and not @ignore",
        plugin = {
                "pretty",
                "html:target/reports/Reports.html",
                "json:target/reports/Cucumber.json"}
)
public class TestRunner {
}