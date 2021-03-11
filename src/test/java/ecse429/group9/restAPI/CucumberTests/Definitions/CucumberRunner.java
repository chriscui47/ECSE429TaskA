package ecse429.group9.restAPI.CucumberTests.Definitions;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = "pretty",
        features = "src/test/java/ecse429/group9/restAPI/CucumberTests/Features")
public class CucumberRunner {
}