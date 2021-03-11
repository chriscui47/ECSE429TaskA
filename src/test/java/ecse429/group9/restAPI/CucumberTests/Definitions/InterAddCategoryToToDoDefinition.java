package ecse429.group9.restAPI.CucumberTests.Definitions;

import ecse429.group9.restAPI.APIInstance;
import ecse429.group9.restAPI.StatusCodes;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONObject;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InterAddCategoryToToDoDefinition {
    @Given("there exists 2 categories and 2 todo")
    public void thereExistsTwoCategories() throws IOException, InterruptedException {
        JSONObject response_cat = APIInstance.request("GET", "/categories");
        assertEquals(2, response_cat.getJSONArray("categories").length());
        JSONObject response_todo = APIInstance.request("GET", "/todos");
        assertEquals(2, response_todo.getJSONArray("todos").length());

        Thread.sleep(500);
    }

    @When("I add an category with the id {string} to an todo with the id {string}")
    public void addNewCategoryToTodo(String catId, String todoId) throws IOException, InterruptedException {
        String option = "/todos/" + todoId + "/categories";

        JSONObject json = new JSONObject();
        json.put("id", catId);
        APIInstance.post(option, json.toString());

        Thread.sleep(500);
    }

    @Given("there exists at least 1 category and 1 todo, this todo {string} already has this category {string}")
    public void alreadyHasThisCat(String catIdStr, String todoId) throws IOException, InterruptedException {
        String url = "/todos/" + todoId + "/categories";
        JSONObject value = APIInstance.request("GET", url);
        assertEquals(2, value.getJSONArray("categories").length());
        if (value.getJSONArray("categories").getJSONObject(0).getString("id").equals(catIdStr)) {
            assertEquals(catIdStr, value.getJSONArray("categories").getJSONObject(0).getString("id"));
        } else {
            assertEquals(catIdStr, value.getJSONArray("categories").getJSONObject(1).getString("id"));
        }

        Thread.sleep(500);
    }

    @Then("the category {string} is still assigned to this todo {string}")
    public void catStillAssigned(String catIdStr, String todoId) throws IOException, InterruptedException {
        String url = "/todos/" + todoId + "/categories";
        JSONObject value = APIInstance.request("GET", url);
        assertEquals(1, value.getJSONArray("categories").length());
        assertEquals(catIdStr, value.getJSONArray("categories").getJSONObject(0).getString("id"));
        Thread.sleep(500);
    }

    @Given("there exists at least 1 todo {string}")
    public void hasOneTodo(String todoId) throws IOException, InterruptedException {
        String url = "/todos/" + todoId;
        JSONObject response = APIInstance.request("GET", url);

        assertTrue("Test failed because the Todo instance was not found and the GET response was null. " +
                "The given endpoint needs to be valid.",response != null);
    }

    @Then("the system should be aware that this category {string} does not exist")
    public void catError(String catId) throws IOException, InterruptedException {
        String url = "/categories/" + catId;
        JSONObject value = APIInstance.request("GET", url);
        assertEquals(null, value);
        Thread.sleep(500);
    }

    @Then("there will be a new category with id {string} to {string}")
    public void thereWillBeNewCategory(String catIdStr, String projectId) throws IOException, InterruptedException {
        String url = "/todos/" + projectId + "/categories";
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

}