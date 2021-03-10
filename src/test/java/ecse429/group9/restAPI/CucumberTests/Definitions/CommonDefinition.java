package ecse429.group9.restAPI.CucumberTests.Definitions;

import ecse429.group9.restAPI.APIInstance;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import static java.lang.Thread.sleep;
import static java.util.Objects.requireNonNull;
import static org.junit.Assert.*;

public class CommonDefinition {

    public static JSONObject json = null;

    //Background operation definitions
    @Given("an instance of the Todo API server is running")
    public void the_Todo_API_server_is_running() {
        APIInstance.runApplication();
    }
    @Then("the user can access the Todo manager")
    public void the_Todo_API_can_be_used() throws IOException {
        if (APIInstance.getStatusCode("/todos") != 200) {
            fail("Error: API server not running.");
        } else {
            json = new JSONObject();
        }
    }

    //Resets the API for the next tests
    @After
    public void shutdown(){
        APIInstance.killInstance();
    }
}
