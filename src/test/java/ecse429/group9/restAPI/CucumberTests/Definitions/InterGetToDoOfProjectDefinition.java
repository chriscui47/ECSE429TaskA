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

public class InterGetToDoOfProjectDefinition {

    @Given("The project {string} has at least 1 todo {string}")
    public void projectHasOneTodo(String projectId, String todoId) throws IOException, InterruptedException {
        String option = "/projects/" + projectId + "/tasks";

        JSONObject json = new JSONObject();
        json.put("id", todoId);
        APIInstance.post(option, json.toString());

        JSONObject value = APIInstance.request("GET", option);

        if (value.getJSONArray("todos").length() == 1) {
            assertEquals(todoId, value.getJSONArray("todos").getJSONObject(0).getString("id"));
        } else {
            if (value.getJSONArray("todos").getJSONObject(0).getString("id").equals(todoId)) {
                assertEquals(todoId, value.getJSONArray("todos").getJSONObject(0).getString("id"));
            } else {
                assertEquals(todoId, value.getJSONArray("todos").getJSONObject(1).getString("id"));
            }
        }

        Thread.sleep(500);

    }

    @When("I get the todo {string} of the project {string}")
    public void getTodoOfProject(String todoId, String projectId) throws IOException, InterruptedException {
        String option = "/projects/" + projectId + "/tasks";

        JSONObject value = APIInstance.request("GET", option);

        if (value.getJSONArray("todos").length() == 1) {
            assertEquals(todoId, value.getJSONArray("todos").getJSONObject(0).getString("id"));
        } else {
            if (value.getJSONArray("todos").getJSONObject(0).getString("id").equals(todoId)) {
                assertEquals(todoId, value.getJSONArray("todos").getJSONObject(0).getString("id"));
            } else {
                assertEquals(todoId, value.getJSONArray("todos").getJSONObject(1).getString("id"));
            }
        }

        Thread.sleep(500);

    }

    @Then("The project {string} still has at least 1 todo {string}")
    public void stillOneTodo(String projectId, String todoId) throws IOException, InterruptedException {
        String option = "/projects/" + projectId + "/tasks";

        JSONObject value = APIInstance.request("GET", option);

        List<Integer> VALID_VALUES = Arrays.asList(1, 2);

        Assert.assertTrue(VALID_VALUES.contains(value.getJSONArray("todos").length()));

        Thread.sleep(500);

    }

    @When("I get the done status of the todo {string} of the project {string}")
    public void getDoneStatusOfToDo(String todoId, String projectId) throws IOException, InterruptedException {
        String option = "/projects/" + projectId + "/tasks";

        JSONObject value = APIInstance.request("GET", option);

        value.getJSONArray("todos").getJSONObject(0).getString("doneStatus");

        Thread.sleep(500);

    }

    @Then("The project {string} still has at least 1 todo {string} with the done status {string}")
    public void stillTwoCat(String projectId, String todoId, String doneStatus) throws IOException, InterruptedException {
        String option = "/projects/" + projectId + "/tasks";

        JSONObject value = APIInstance.request("GET", option);

        if (value.getJSONArray("todos").length() == 1) {
            assertEquals(doneStatus, value.getJSONArray("todos").getJSONObject(0).getString("doneStatus"));
        } else {
            if (value.getJSONArray("todos").getJSONObject(0).getString("id").equals(todoId)) {
                assertEquals(doneStatus, value.getJSONArray("todos").getJSONObject(0).getString("doneStatus"));
            } else {
                assertEquals(doneStatus, value.getJSONArray("todos").getJSONObject(1).getString("doneStatus"));
            }
        }

        Thread.sleep(500);

    }

    @When("I get the non existing todo {string} of the project {string}")
    public void nonExistingTodo(String todoId, String projectId) throws IOException, InterruptedException {
        String option = "/projects/" + projectId + "/tasks?id=" + todoId;
        JSONObject value = APIInstance.request("GET", option);
        assertEquals(0, value.getJSONArray("todos").length());

        Thread.sleep(500);

    }
}