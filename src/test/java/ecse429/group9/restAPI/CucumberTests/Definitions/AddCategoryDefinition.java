package ecse429.group9.restAPI.CucumberTests.Definitions;

import ecse429.group9.restAPI.APIInstance;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONObject;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class AddCategoryDefinition {
    @Given("there exists 2 categories for add category")
    public void thereExistsTwoCategories() throws IOException {
        JSONObject response = APIInstance.request("GET", "/categories");
        assertEquals(2, response.getJSONArray("categories").length());
    }

    @When("I add a new category with the title {string}")
    public void addNewCategoryTitle(String categoryTitle) throws IOException, InterruptedException {
        String option = "/categories";

        JSONObject json = new JSONObject();
        json.put("title", categoryTitle);
        json.put("description", "");
        APIInstance.post(option, json.toString());
        Thread.sleep(500);
    }

    @Then("there will be a new category with title {string}")
    public void thereWillBeNewCategoryTitle(String categoryTitle) throws IOException, InterruptedException {
        JSONObject response1 = APIInstance.send("GET", "/categories?title=" + categoryTitle);
        String categoryID = response1.getJSONArray("categories").getJSONObject(0).getString("id");

        JSONObject response2 = APIInstance.send("GET","/categories/" + categoryID);
        assertEquals(categoryTitle, response2.getJSONArray("categories").getJSONObject(0).get("title"));

        APIInstance.request("DELETE", "/categories/" + categoryID);
        Thread.sleep(500);
    }

    @When("I add a new category with the title {string} and description {string}")
    public void addNewCategoryTitleAndDescription(String categoryTitle, String categoryDescription) throws IOException, InterruptedException {
        String option = "/categories";

        JSONObject json = new JSONObject();
        json.put("title", categoryTitle);
        json.put("description", categoryDescription);
        APIInstance.post(option, json.toString());
        Thread.sleep(500);
    }

    @Then("there will be a new category with title {string} and description {string}")
    public void thereWillBeNewCategoryTitleAndDescription(String categoryTitle, String categoryDescription) throws IOException, InterruptedException {
        JSONObject response1 = APIInstance.send("GET", "/categories?title=" + categoryTitle);
        String categoryID = response1.getJSONArray("categories").getJSONObject(0).getString("id");

        JSONObject response2 = APIInstance.send("GET","/categories/" + categoryID);
        assertEquals(categoryTitle, response2.getJSONArray("categories").getJSONObject(0).get("title"));
        assertEquals(categoryDescription, response2.getJSONArray("categories").getJSONObject(0).get("description"));

        APIInstance.request("DELETE", "/categories/" + categoryID);
        Thread.sleep(500);
    }

    @When("I add a new category with the title {string} and id {string}")
    public void addNewCategoryTitleAndID(String categoryTitle, String categoryID) throws IOException, InterruptedException {
        String option = "/categories";

        JSONObject json = new JSONObject();
        json.put("title", categoryTitle);
        json.put("id", categoryID);
        APIInstance.post(option, json.toString());
        Thread.sleep(500);
    }
}
