package ecse429.group9.restAPI.CucumberTests.Definitions;

import ecse429.group9.restAPI.APIInstance;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.junit.Assert;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class InterGetCategoryOfToDoDefinition {

    @Given("The todo {string} has at least 1 category {string}")
    public void todoHasOneCat(String todoId, String catId) throws IOException, InterruptedException {
        String option = "/todos/" + todoId + "/categories";

        JSONObject json = new JSONObject();
        json.put("id", catId);
        APIInstance.post(option, json.toString());

        Thread.sleep(500);

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

    @When("I get the category {string} of the todo {string}")
    public void getCatOfTodo(String catId, String todoId) throws IOException, InterruptedException {
        String option = "/todos/" + todoId + "/categories";

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
    }

    @Then("The todo {string} still has at least 1 category {string}")
    public void stillOneCat(String todoId, String catId) throws IOException, InterruptedException {
        String option = "/todos/" + todoId + "/categories";

        JSONObject value = APIInstance.request("GET", option);

        List<Integer> VALID_VALUES = Arrays.asList(1, 2);

        Assert.assertTrue(VALID_VALUES.contains(value.getJSONArray("categories").length()));
    }

    @Given("The todo {string} has 2 categories")
    public void todoHasTwoCat(String todoId) throws IOException, InterruptedException {
        String option = "/todos/" + todoId + "/categories";

        JSONObject json1 = new JSONObject();
        json1.put("id", "1");
        APIInstance.post2(option, json1.toString());

        JSONObject json2 = new JSONObject();
        json2.put("id", "2");
        APIInstance.post2(option, json2.toString());

        Thread.sleep(500);

        JSONObject value = APIInstance.request("GET", option);

        assertEquals(2, value.getJSONArray("categories").length());
    }

    @Then("The todo {string} still has 2 categories")
    public void stillTwoCat(String todoId) throws IOException, InterruptedException {
        String option = "/todos/" + todoId + "/categories";
        JSONObject value = APIInstance.request("GET", option);

        assertEquals(2, value.getJSONArray("categories").length());
    }

    @When("I get the non existing category {string} of the todo {string}")
    public void nonExistingCat(String catId, String todoId) throws IOException, InterruptedException {
        String option = "/todos/" + todoId + "/categories?id=" + catId;
        JSONObject value = APIInstance.request("GET", option);
        assertEquals(0, value.getJSONArray("categories").length());
    }
}