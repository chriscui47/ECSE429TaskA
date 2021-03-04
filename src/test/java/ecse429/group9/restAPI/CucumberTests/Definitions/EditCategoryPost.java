package ecse429.group9.restAPI.CucumberTests.Definitions;

import ecse429.group9.restAPI.APIInstance;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONObject;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class EditCategoryPost {
    @Given("there exists a new category with title {string} with POST")
    public void thereExistsANewCategoryWithTitleWithPOST(String categoryTitle) throws IOException, InterruptedException {
        String option = "/categories";

        JSONObject json = new JSONObject();
        json.put("title", categoryTitle);
        json.put("description", "");
        APIInstance.post(option, json.toString());
        Thread.sleep(500);
    }

    @When("I change a category with title {string} to {string} with POST")
    public void iChangeACategoryWithTitleToWithPOST(String categoryTitle, String newCategoryTitle) throws IOException, InterruptedException {
        JSONObject response1 = APIInstance.send("GET", "/categories?title=" + categoryTitle);
        String categoryID = response1.getJSONArray("categories").getJSONObject(0).getString("id");

        JSONObject json = new JSONObject();
        json.put("title", newCategoryTitle);

        APIInstance.post("/categories/" + categoryID, json.toString());
        Thread.sleep(500);
    }

    @Then("there will be a new category with the title {string}")
    public void thereWillBeANewCategoryWithTheTitle(String categoryTitle) throws IOException, InterruptedException {
        JSONObject response = APIInstance.send("GET", "/categories?title=" + categoryTitle);
        assertEquals(categoryTitle, response.getJSONArray("categories").getJSONObject(0).getString("title"));

        String categoryID = response.getJSONArray("categories").getJSONObject(0).getString("id");
        APIInstance.request("DELETE", "/categories/" + categoryID);
        Thread.sleep(500);
    }

    @Given("there exists a new category with title {string} and description {string} with POST")
    public void thereExistsANewCategoryWithTitleAndDescriptionWithPOST(String categoryTitle, String categoryDescription) throws IOException, InterruptedException {
        String option = "/categories";

        JSONObject json = new JSONObject();
        json.put("title", categoryTitle);
        json.put("description", categoryDescription);
        APIInstance.post(option, json.toString());
        Thread.sleep(500);
    }

    @When("I change a category's description with title {string} to {string} with POST")
    public void iChangeACategorySDescriptionWithTitleToWithPOST(String categoryTitle, String categoryDescription) throws IOException, InterruptedException {
        JSONObject response1 = APIInstance.send("GET", "/categories?title=" + categoryTitle);
        String categoryID = response1.getJSONArray("categories").getJSONObject(0).getString("id");

        JSONObject json = new JSONObject();
        json.put("description", categoryDescription);

        APIInstance.post("/categories/" + categoryID, json.toString());
        Thread.sleep(500);
    }


    @Then("there will be a new category with the title {string} and description {string}")
    public void thereWillBeANewCategoryWithTheTitleAndDescription(String categoryTitle, String categoryDescription) throws IOException, InterruptedException {
        JSONObject response = APIInstance.send("GET", "/categories?title=" + categoryTitle);
        assertEquals(categoryDescription, response.getJSONArray("categories").getJSONObject(0).getString("description"));

        String categoryID = response.getJSONArray("categories").getJSONObject(0).getString("id");
        APIInstance.request("DELETE", "/categories/" + categoryID);
        Thread.sleep(500);
    }


    @When("I change a category's title with id {string} to {string} with POST")
    public void iChangeACategorySTitleWithIdToWithPOST(String categoryID, String categoryTitle) throws IOException, InterruptedException {
        String option = "/categories/" + categoryID;

        JSONObject json = new JSONObject();
        json.put("title", categoryTitle);
        json.put("description", "");
        APIInstance.post(option, json.toString());
        Thread.sleep(500);
    }


    @Then("the category with id {string} will not have {string} as title")
    public void theCategoryWithIdWillNotHaveAsTitle(String categoryID, String categoryTitle) throws IOException {
        JSONObject response = APIInstance.send("GET","/categories/" + categoryID);
        assertEquals(null, response);
    }
}
