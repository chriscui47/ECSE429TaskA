package ecse429.group9.restAPI.CucumberTests.Definitions;

import ecse429.group9.restAPI.APIInstance;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONObject;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class EditCategoryPutDefinition {
    @Given("the edit category PUT API server is running")
    public void the_Todo_API_server_is_running(){
        APIInstance.runApplication();
    }

    @Given("there exists 2 categories for edit category PUT")
    public void thereExistsTwoCategories() throws IOException {
        JSONObject response = APIInstance.request("GET", "/categories");
        assertEquals(2, response.getJSONArray("categories").length());
    }

    @Given("there exists a new category with title {string} with POST for PUT")
    public void thereExistsANewCategoryWithTitleWithPOST(String categoryTitle) throws IOException, InterruptedException {
        String option = "/categories";

        JSONObject json = new JSONObject();
        json.put("title", categoryTitle);
        json.put("description", "");
        APIInstance.post(option, json.toString());
        Thread.sleep(500);
    }

    @Given("there exists a new category with title {string} and description {string} with POST for PUT")
    public void thereExistsANewCategoryWithTitleAndDescriptionWithPOST(String categoryTitle, String categoryDescription) throws IOException, InterruptedException {
        String option = "/categories";

        JSONObject json = new JSONObject();
        json.put("title", categoryTitle);
        json.put("description", categoryDescription);
        APIInstance.post(option, json.toString());
        Thread.sleep(500);
    }


    @When("I change a category with title {string} to {string} with PUT")
    public void iChangeACategoryWithTitleToWithPUT(String categoryTitle, String newCategoryTitle) throws IOException, InterruptedException {
        JSONObject response1 = APIInstance.send("GET", "/categories?title=" + categoryTitle);
        String categoryID = response1.getJSONArray("categories").getJSONObject(0).getString("id");

        JSONObject json = new JSONObject();
        json.put("title", newCategoryTitle);

        APIInstance.put("/categories/" + categoryID, json.toString());
        Thread.sleep(500);
    }

    @When("I change a category's description with title {string} to {string} with PUT")
    public void iChangeACategorySDescriptionWithTitleToWithPUT(String categoryTitle, String categoryDescription) throws IOException, InterruptedException {
        JSONObject response1 = APIInstance.send("GET", "/categories?title=" + categoryTitle);
        String categoryID = response1.getJSONArray("categories").getJSONObject(0).getString("id");

        JSONObject json = new JSONObject();
        json.put("title", categoryTitle);
        json.put("description", categoryDescription);

        APIInstance.put("/categories/" + categoryID, json.toString());
        Thread.sleep(500);
    }

    @When("I change a category's title with id {string} to {string} with PUT")
    public void iChangeACategorySTitleWithIdToWithPUT(String categoryID, String categoryTitle) throws IOException, InterruptedException {
        String option = "/categories/" + categoryID;

        JSONObject json = new JSONObject();
        json.put("title", categoryTitle);
        json.put("description", "");
        APIInstance.put(option, json.toString());
        Thread.sleep(500);
    }

    @Then("there will be a new category with the title {string} with PUT")
    public void thereWillBeANewCategoryWithTheTitle(String categoryTitle) throws IOException, InterruptedException {
        JSONObject response = APIInstance.send("GET", "/categories?title=" + categoryTitle);
        assertEquals(categoryTitle, response.getJSONArray("categories").getJSONObject(0).getString("title"));

        String categoryID = response.getJSONArray("categories").getJSONObject(0).getString("id");
        APIInstance.request("DELETE", "/categories/" + categoryID);
        Thread.sleep(500);
    }

    @Then("there will be a new category with the title {string} and description {string} with PUT")
    public void thereWillBeANewCategoryWithTheTitleAndDescription(String categoryTitle, String categoryDescription) throws IOException, InterruptedException {
        JSONObject response = APIInstance.send("GET", "/categories?title=" + categoryTitle);
        assertEquals(categoryDescription, response.getJSONArray("categories").getJSONObject(0).getString("description"));

        String categoryID = response.getJSONArray("categories").getJSONObject(0).getString("id");
        APIInstance.request("DELETE", "/categories/" + categoryID);
        Thread.sleep(500);
    }

    @Then("the category with id {string} will not have {string} as title with PUT")
    public void theCategoryWithIdWillNotHaveAsTitle(String categoryID, String categoryTitle) throws IOException {
        JSONObject response = APIInstance.send("GET","/categories/" + categoryID);
        assertEquals(null, response);
    }


    @After
    public void shutdown(){
        APIInstance.killInstance();
    }
}
