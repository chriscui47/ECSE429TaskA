package ecse429.group9.restAPI.CucumberTests.Definitions;

import ecse429.group9.restAPI.APIInstance;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONObject;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InterAddCategoryToProjectDefinition {
    @Given("there exists 2 categories and 1 project")
    public void thereExistsTwoCategories() throws IOException, InterruptedException {
        JSONObject response_cat = APIInstance.request("GET", "/categories");
        assertEquals(2, response_cat.getJSONArray("categories").length());
        JSONObject response_project = APIInstance.request("GET", "/projects");
        assertEquals(1, response_project.getJSONArray("projects").length());

        Thread.sleep(500);
    }

    @When("I add an category with the id {string} to an project with the id {string}")
    public void addNewCategoryToTodo(String catId, String projectId) throws IOException, InterruptedException {
        String option = "/projects/" + projectId + "/categories";

        JSONObject json = new JSONObject();
        json.put("id", catId);
        APIInstance.post(option, json.toString());

        Thread.sleep(500);
    }

    @Then("there will be a new category with id {string} to project {string}")
    public void thereWillBeNewCategoryProject(String catIdStr, String projectId) throws IOException, InterruptedException {
        String url = "/projects/" + projectId + "/categories";
        JSONObject value = APIInstance.request("GET", url);
        if (value.getJSONArray("categories").length() == 1) {
            assertEquals(catIdStr, value.getJSONArray("categories").getJSONObject(0).getString("id"));
        } else {
            if (value.getJSONArray("categories").getJSONObject(0).getString("id").equals(catIdStr)) {
                assertEquals(catIdStr, value.getJSONArray("categories").getJSONObject(0).getString("id"));
            } else {
                assertEquals(catIdStr, value.getJSONArray("categories").getJSONObject(1).getString("id"));
            }
        }
        Thread.sleep(500);
    }

    @Given("there exists at least 1 category and 1 project, this project {string} already has this category {string}")
    public void alreadyHasThisCat(String catIdStr, String projectId) throws IOException, InterruptedException {
        String url = "/projects/" + projectId + "/categories";

        JSONObject json = new JSONObject();
        json.put("id", catIdStr);
        APIInstance.post(url, json.toString());

        JSONObject value = APIInstance.request("GET", url);

        assertEquals(2, value.getJSONArray("categories").length());
        if (value.getJSONArray("categories").getJSONObject(0).getString("id").equals(catIdStr)) {
            assertEquals(catIdStr, value.getJSONArray("categories").getJSONObject(0).getString("id"));
        } else {
            assertEquals(catIdStr, value.getJSONArray("categories").getJSONObject(1).getString("id"));
        }

        Thread.sleep(500);
    }

    @Then("the category {string} is still assigned to this project {string}")
    public void catStillAssigned(String catIdStr, String projectId) throws IOException, InterruptedException {
        String url = "/projects/" + projectId + "/categories";
        JSONObject value = APIInstance.request("GET", url);
        assertEquals(2, value.getJSONArray("categories").length());
        if (value.getJSONArray("categories").getJSONObject(0).getString("id").equals(catIdStr)) {
            assertEquals(catIdStr, value.getJSONArray("categories").getJSONObject(0).getString("id"));
        } else {
            assertEquals(catIdStr, value.getJSONArray("categories").getJSONObject(1).getString("id"));
        }
        Thread.sleep(500);
    }

    @Given("there exists at least 1 project {string}")
    public void hasOneTodo(String projectId) throws IOException, InterruptedException {
        String url = "/projects/" + projectId;
        JSONObject response = APIInstance.request("GET", url);

        assertTrue("Test failed because the Project instance was not found and the GET response was null. " +
                "The given endpoint needs to be valid.",response != null);
    }

}