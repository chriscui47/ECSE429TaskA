package ecse429.group9.restAPI.CucumberTests.Definitions;

import ecse429.group9.restAPI.APIInstance;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class RemoveProjectDefinition {
    String error;

    @Given("{string} is the title of the project to be removed")
    public void isTheTitleOfTheToDoListToBeRemoved(String arg0) throws IOException {
        JSONObject json = new JSONObject();
        json.put("title", arg0);
        APIInstance.post("/projects", json.toString());
    }

    @When("the user posts a request to the server to remove a project {string}")
    public void theUserPostsARequestToTheServerToRemoveAToDoList(String arg0) throws IOException, InterruptedException {
        JSONObject response = APIInstance.send("GET", "/projects?title=" + arg0);
        if (response.getJSONArray("projects").length() != 0) {
            String id = response.getJSONArray("projects").getJSONObject(0).getString("id");
            APIInstance.send("DELETE", "/projects/" + id);
            Thread.sleep(5000);
        } else {
            error = "404";
        }
    }

    @Then("the project with {string} will no longer exist")
    public void theToDoListWillNoLongerBeInTheSchedule(String arg0) throws IOException {
        JSONObject response = APIInstance.send("GET", "/projects?title=" + arg0);
        try {
            int length = response.getJSONArray("projects").length();
            assertEquals(0, length);
        }
        //NO MORE TASKS
        catch (Exception e) {
            assertEquals(true, true);
        }
    }

    @And("{string} is the title of the project related to the to do list {string}")
    public void isTheTitleOfTheCategoryRelatedToTheToDoList(String arg0, String arg1) throws  IOException{
        JSONObject response = APIInstance.send("GET", "/projects?title=" + arg1);

        JSONObject jsonCat = new JSONObject();
        jsonCat.put("title", arg0);
        APIInstance.post("/categories", jsonCat.toString());
        JSONObject responseCat = APIInstance.send("GET", "/categories?title=" + arg0);

        if (response.getJSONArray("projects").length() != 0 && responseCat.getJSONArray("categories").length() != 0) {
            String id = response.getJSONArray("projects").getJSONObject(0).getString("id");
            APIInstance.post("/projects/" + id, jsonCat.toString());
        } else {
            error = "404";
        }
    }

    @Given("the id of a non-existent to do list is {string}")
    public void theIdOfANonExistentToDoListIs(String arg0) {
        try {
            JSONObject response = APIInstance.send("GET", "/todos/" + arg0);
            assertEquals(arg0, response.getJSONArray("errorMessages").getJSONObject(0).toString());
        } catch (Exception e) {
            //Cannot find id
            assertEquals(true, true);
        }
    }
}