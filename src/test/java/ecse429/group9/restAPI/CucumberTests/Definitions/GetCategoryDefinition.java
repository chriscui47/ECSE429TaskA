package ecse429.group9.restAPI.CucumberTests.Definitions;

import ecse429.group9.restAPI.APIInstance;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.json.JSONObject;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class GetCategoryDefinition {
    @Given("the get category API server is running")
    public void the_Todo_API_server_is_running(){
        APIInstance.runApplication();
    }

    @Given("there exists 2 categories for get category")
    public void thereExistsTwoCategories() throws IOException {
        JSONObject response = APIInstance.request("GET", "/categories");
        assertEquals(2, response.getJSONArray("categories").length());
    }

    @When("I get a category with id {string} and title {string}")
    public void iGetACategoryWithIdAndTitle(String categoryID, String categoryTitle) throws IOException {
        JSONObject response = APIInstance.request("GET", "/categories/" + categoryID);
        assertEquals(categoryTitle, response.getJSONArray("categories").getJSONObject(0).get("title"));
    }

    @When("I get a category with title {string}")
    public void iGetACategoryWithTitle(String categoryTitle) throws IOException {
        String request = "/categories?title=" + categoryTitle;
        assertEquals(200, APIInstance.getStatusCode("GET", request));
    }

    @When("I get a category with invalid id {string}")
    public void iGetACategoryWithInvalidId(int categoryID) throws IOException {
        String invalid_request = "/categories/" + categoryID;
        assertEquals(404, APIInstance.getStatusCode("GET", invalid_request));
    }

    @When("I get a category with invalid id {string} with title {string}")
    public void iGetACategoryWithInvalidIdWithTitle(String categoryID, String categoryTitle) throws IOException {
        JSONObject response = APIInstance.request("GET", "/categories/" + categoryID);
        assertEquals(null, response);
    }

    @After
    public void shutdown(){
        APIInstance.killInstance();
    }
}
