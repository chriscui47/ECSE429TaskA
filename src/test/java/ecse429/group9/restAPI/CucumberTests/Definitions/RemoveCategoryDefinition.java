package ecse429.group9.restAPI.CucumberTests.Definitions;

import ecse429.group9.restAPI.APIInstance;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONObject;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class RemoveCategoryDefinition {
    @Given("there exists a new category {string}")
    public void thereExistsNewCategoryTitle(String categoryTitle) throws IOException, InterruptedException {
        String option = "/categories";

        JSONObject json = new JSONObject();
        json.put("title", categoryTitle);
        json.put("description", "");
        APIInstance.post(option, json.toString());
        Thread.sleep(500);

        JSONObject response = APIInstance.request("GET", "/categories");
        assertEquals(3, response.getJSONArray("categories").length());
    }


    @When("I want to remove a category with the title {string}")
    public void removeCategoryTitle(String categoryTitle) throws IOException, InterruptedException {
        JSONObject response1 = APIInstance.send("GET", "/categories?title=" + categoryTitle);
        String categoryID = response1.getJSONArray("categories").getJSONObject(0).getString("id");

        APIInstance.request("DELETE", "/categories/" + categoryID);
        Thread.sleep(500);
    }


    @Given("there exists 2 categories for remove category")
    public void thereExistsTwoCategories() throws IOException {
        JSONObject response = APIInstance.request("GET", "/categories");
        assertEquals(2, response.getJSONArray("categories").length());
    }


    @Given("there exists a new category {string} with description {string}")
    public void thereExistsANewCategoryWithDescription(String categoryTitle, String categoryDescription) throws IOException, InterruptedException {
        String option = "/categories";

        JSONObject json = new JSONObject();
        json.put("title", categoryTitle);
        json.put("description", categoryDescription);
        APIInstance.post(option, json.toString());
        Thread.sleep(500);

        JSONObject response = APIInstance.request("GET", "/categories");
        assertEquals(3, response.getJSONArray("categories").length());
    }


    @When("I want to remove a category with the title {string} and description {string}")
    public void iWantToRemoveACategoryWithTheTitleAndDescription(String categoryTitle, String categoryDescription) throws IOException, InterruptedException {
        JSONObject response1 = APIInstance.send("GET", "/categories?title=" + categoryTitle);
        String categoryID = response1.getJSONArray("categories").getJSONObject(0).getString("id");

        APIInstance.request("DELETE", "/categories/" + categoryID);
        Thread.sleep(500);
    }


    @When("I want to remove a category with id {int}")
    public void iWantToRemoveACategoryWithId(int categoryID) throws InterruptedException, IOException {
        APIInstance.request("DELETE", "/categories/" + categoryID);
        Thread.sleep(500);
    }

    @Then("there will still be 3 categories, remove {string} to reset")
    public void thereWillStillBeCategoriesRemoveToReset(String categoryTitle) throws IOException, InterruptedException {
        JSONObject response = APIInstance.request("GET", "/categories");
        assertEquals(3, response.getJSONArray("categories").length());

        JSONObject response1 = APIInstance.send("GET", "/categories?title=" + categoryTitle);
        String categoryID = response1.getJSONArray("categories").getJSONObject(0).getString("id");

        APIInstance.request("DELETE", "/categories/" + categoryID);
        Thread.sleep(500);
    }
}
