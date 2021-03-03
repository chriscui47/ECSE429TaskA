package ecse429.group9.restAPI.CucumberTests.Definitions;

import ecse429.group9.restAPI.APIInstance;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.json.JSONObject;

import java.io.IOException;

public class EditCategoryPut {
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
}
