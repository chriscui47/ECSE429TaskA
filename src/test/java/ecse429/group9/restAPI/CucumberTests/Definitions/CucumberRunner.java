package ecse429.group9.restAPI.CucumberTests.Definitions;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = "pretty",
        features = "src/test/java/ecse429/group9/restAPI/CucumberTests/Features"
        //glue = "src/test/java/ecse429/group9/restAPI/CucumberTests/Definitions"
        )
public class CucumberRunner {

}