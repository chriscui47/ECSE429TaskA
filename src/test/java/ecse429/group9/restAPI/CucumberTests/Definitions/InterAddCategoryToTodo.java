package ecse429.group9.restAPI.CucumberTests.Definitions;

import ecse429.group9.restAPI.APIInstance;
import ecse429.group9.restAPI.StatusCodes;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONObject;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InterAddCategoryToTodo {
    @Given("there exists 2 categories and 2 todo")
    public void thereExistsTwoCategories() throws IOException {
        JSONObject response_cat = APIInstance.request("GET", "/categories");
        assertEquals(2, response_cat.getJSONArray("categories").length());
        JSONObject response_todo = APIInstance.request("GET", "/todo");
        assertEquals(2, response_todo.getJSONArray("todo").length());
    }

    @When("I add a new category with the id {string} to an existing todo with the id {string}")
    public void addNewCategoryToTodo(String catId, String todoId) throws IOException, InterruptedException {
        String option = "/todos/" + todoId + "/categories";

        JSONObject json = new JSONObject();
        json.put("id", catId);
        APIInstance.post(option, json.toString());

        Thread.sleep(500);
    }

    @Then("there will be a new category with id {string} to {string}")
    public void thereWillBeNewCategory(String catIdStr, String todoId) throws IOException, InterruptedException {
        String url = "/todos/" + todoId + "/categories";
        JSONObject value = APIInstance.request("GET", url);
        assertEquals(1, value.getJSONArray("categories").length());
        assertEquals(catIdStr, value.getJSONArray("categories").getJSONObject(0).getString("id"));
        Thread.sleep(500);
    }

    @Given("there exists at least 1 category and 1 todo, this todo {string} already has this category {string}")
    public void alreadyHasThisCat(String catIdStr, String todoId) throws IOException, InterruptedException {
        String url = "/todos/" + todoId + "/categories";
        JSONObject value = APIInstance.request("GET", url);
        assertEquals(1, value.getJSONArray("categories").length());
        assertEquals(catIdStr, value.getJSONArray("categories").getJSONObject(0).getString("id"));
        Thread.sleep(500);
    }

    @When("I add an existing category with the id {string} to an existing todo with the id {string}")
    public void addExistingCatToTodo(String catId, String todoId) throws IOException, InterruptedException {
        String option = "/todos/" + todoId + "/categories";

        JSONObject json = new JSONObject();
        json.put("id", catId);
        APIInstance.post(option, json.toString());

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

    @When("I add a non existing category with the id {string} to an existing todo with the id {string}")
    public void addNonExistingCat(String catId, String todoId) throws IOException, InterruptedException {
        String option = "/todos/" + todoId + "/categories";

        JSONObject json = new JSONObject();
        json.put("id", catId);
        APIInstance.post(option, json.toString());

        Thread.sleep(500);
    }

    @Then("the system should inform the user that this category does not exist")
    public void catError(String catId) throws IOException, InterruptedException {
        String url = "/categories/" + catId;
        JSONObject value = APIInstance.request("GET", url);
        assertEquals(StatusCodes.SC_NOT_FOUND, value);
        Thread.sleep(500);
    }

}
