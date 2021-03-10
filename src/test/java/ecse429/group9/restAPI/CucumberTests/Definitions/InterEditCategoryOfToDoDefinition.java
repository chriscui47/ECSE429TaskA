package ecse429.group9.restAPI.CucumberTests.Definitions;

import ecse429.group9.restAPI.APIInstance;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONObject;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InterEditCategoryOfToDoDefinition {

    @Given("The todo {string} has at least one category {string}")
    public void todoHasOneCat(String todoId, String catId) throws IOException, InterruptedException {
        String option = "/todos/" + todoId + "/categories";

        JSONObject json = new JSONObject();
        json.put("id", catId);
        APIInstance.post(option, json.toString());

        JSONObject value = APIInstance.request("GET", option);

        if (value.getJSONArray("categories").length() == 1) {
            assertEquals(catId, value.getJSONArray("categories").getJSONObject(0).getString("id"));
        } else {
            if (value.getJSONArray("categories").getJSONObject(0).getString("id").equals(catId)) {
                assertEquals(catId, value.getJSONArray("categories").getJSONObject(0).getString("id"));
            } else {
                assertEquals(catId, value.getJSONArray("categories").getJSONObject(1).getString("id"));
            }
        }

        Thread.sleep(500);

    }

    @When("I modify the description {string} with the id {string} to an todo with the id {string}")
    public void modifyDescriptionOfCat(String description, String catId, String todoId) throws IOException, InterruptedException {
        String opt = "/todos/" + todoId + "/categories" ;

        JSONObject value = APIInstance.request("GET", opt);
        if (value.getJSONArray("categories").length() == 1) {
            String option = "/categories/" + catId;
            JSONObject json = new JSONObject();
            json.put("description", description);
            APIInstance.post(option, json.toString());
        } else {
            if (value.getJSONArray("categories").getJSONObject(0).getString("id").equals(catId)) {
                String option = "/categories/" + catId;
                JSONObject json = new JSONObject();
                json.put("description", description);
                APIInstance.post(option, json.toString());
            } else {
                String option = "/categories/" + catId;
                JSONObject json = new JSONObject();
                json.put("description", description);
                APIInstance.post(option, json.toString());
            }
        }

        Thread.sleep(500);
    }

    @Then("the description of the category {string} of {string} will change to {string}")
    public void checkDescriptionOfCatChangesId(String catId, String todoId, String description) throws IOException, InterruptedException {
        String option = "/todos/" + todoId + "/categories" ;

        JSONObject value = APIInstance.request("GET", option);
        if (value.getJSONArray("categories").length() == 1) {
            assertEquals(description, value.getJSONArray("categories").getJSONObject(0).getString("description"));
        } else {
            if (value.getJSONArray("categories").getJSONObject(0).getString("id").equals(catId)) {
                assertEquals(description, value.getJSONArray("categories").getJSONObject(0).getString("description"));
            } else {
                assertEquals(description, value.getJSONArray("categories").getJSONObject(1).getString("description"));
            }
        }

        Thread.sleep(500);
    }

    // 2

    @Given("The todo {string} has at least one category {string}, and it has a description {string}")
    public void catHasDescription(String todoId, String catId, String description) throws IOException, InterruptedException {
        String option = "/todos/" + todoId + "/categories";

        JSONObject json = new JSONObject();
        json.put("id", catId);
        APIInstance.post(option, json.toString());

        JSONObject value = APIInstance.request("GET", option);

        if (value.getJSONArray("categories").length() == 1) {
            assertEquals(catId, value.getJSONArray("categories").getJSONObject(0).getString("id"));
            value.getJSONArray("categories").getJSONObject(0).put("description", description);
            assertEquals(description, value.getJSONArray("categories").getJSONObject(0).getString("description"));
        } else {
            if (value.getJSONArray("categories").getJSONObject(0).getString("id").equals(catId)) {
                assertEquals(catId, value.getJSONArray("categories").getJSONObject(0).getString("id"));
                value.getJSONArray("categories").getJSONObject(0).put("description", description);
                assertEquals(description, value.getJSONArray("categories").getJSONObject(0).getString("description"));
            } else {
                assertEquals(catId, value.getJSONArray("categories").getJSONObject(1).getString("id"));
                value.getJSONArray("categories").getJSONObject(1).put("description", description);
                assertEquals(description, value.getJSONArray("categories").getJSONObject(1).getString("description"));
            }
        }

        Thread.sleep(500);
    }

    @When("I modify the description {string} with the id {string} that does not exist to an todo with the id {string}")
    public void modifyError(String description, String catId, String todoId) throws IOException, InterruptedException {
        String opt = "/todos/" + todoId + "/categories" ;

        JSONObject value = APIInstance.request("GET", opt);
        if (value.getJSONArray("categories").length() == 1) {
            String option = "/categories/" + catId;
            JSONObject json = new JSONObject();
            json.put("description", description);
            APIInstance.post(option, json.toString());
        } else {
            if (value.getJSONArray("categories").getJSONObject(0).getString("id").equals(catId)) {
                String option = "/categories/" + catId;
                JSONObject json = new JSONObject();
                json.put("description", description);
                APIInstance.post(option, json.toString());
            } else {
                String option = "/categories/" + catId;
                JSONObject json = new JSONObject();
                json.put("description", description);
                APIInstance.post(option, json.toString());
            }
        }

        Thread.sleep(500);
    }

}