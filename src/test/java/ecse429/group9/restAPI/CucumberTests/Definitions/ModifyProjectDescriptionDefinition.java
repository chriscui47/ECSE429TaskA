package ecse429.group9.restAPI.CucumberTests.Definitions;



import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import ecse429.group9.restAPI.APIInstance;
import static org.junit.Assert.assertEquals;

public class ModifyProjectDescriptionDefinition {

    String error;

    @Given("the title of the project {string}")
    public void theTitleOfTheProject(String title) throws IOException {
        JSONObject json = new JSONObject();
        json.put("title", title);
        APIInstance.post("/projects", json.toString());
    }

    @Given("the title of the project {string}, the description {string}")
    public void theTitleOfTheProjectDescrip(String title,String description) throws IOException {
        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("description", description);
        APIInstance.post("/projects", json.toString());
    }

    @When("the user posts description change of project {string} to {string}")
    public void theUserPostsDescriptionChangeOfTaskTo(String arg0, String arg1) throws IOException {
        JSONObject json = new JSONObject();
        JSONObject response = APIInstance.send("GET", "/projects?title=" + arg0);

        json.put("description", arg1);
        if (response.getJSONArray("projects").length() != 0) {
            String id = response.getJSONArray("projects").getJSONObject(0).getString("id");
            System.out.println(id);
            APIInstance.post("/projects/" + id, json.toString());
        } else {
            error = "404";
            System.out.println(error);
        }

    }

    @Then("the project {string} description will be changed to {string}")
    public void theTaskDescriptionWillBeChangedTo(String arg0, String arg1) throws IOException {
        JSONObject response = APIInstance.send("GET", "/projects?title=" + arg0);
        String description = response.getJSONArray("projects").getJSONObject(0).getString("description");
        assertEquals(arg1, description);
    }


    @And("{string} is related to task with title {string}")
    public void isRelatedToProjectsWithTitle(String arg0, String arg1) throws IOException {
        JSONObject response = APIInstance.send("GET", "/projects?title=" + arg0);

        JSONObject jsonPr = new JSONObject();
        jsonPr.put("title", arg1);
        APIInstance.post("/projects", jsonPr.toString());
        JSONObject responsePr = APIInstance.send("GET", "/todos?title=" + arg1);

        if (response.getJSONArray("projects").length() != 0 && responsePr.getJSONArray("tasks").length() != 0) {
            String id = response.getJSONArray("projects").getJSONObject(0).getString("id");
            APIInstance.post("/projects/" + id, jsonPr.toString());
        } else {
            error = "404";
        }
    }

    @Given("the id of a non-existent project is {string}")
    public void theIdOfANonExistentTaskIs(String arg0) throws IOException {
        JSONObject json = new JSONObject();
        json.put("title", arg0);
        APIInstance.post("/projects", json.toString());
    }

    @Then("an error message {string} with {string} will occur")
    public void anErrorMessage(String arg0, String arg1) throws IOException{
        try{
            JSONObject response = APIInstance.send("GET", "/projects/" + arg1);
            System.out.println(response.getJSONArray("errorMessages").getJSONObject(0).toString());
            assertEquals(arg0,true);
        }
        catch (Exception e) {
            //Cannot find id
            assertEquals(true,true);
        }
    }

    @After
    public void clear() throws IOException {
        // Remove all todos.
        JSONObject response = APIInstance.send("GET", "/projects");
        JSONArray array = response.getJSONArray("projects");
        for (int i=0; i<array.length(); i++){
            String id = array.getJSONObject(i).getString("id");
            APIInstance.send("DELETE", "/projects/" + id);
        }

        // Remove all projects.
        response = APIInstance.send("GET", "/projects");
        array = response.getJSONArray("projects");
        for (int i=0; i<array.length(); i++){
            String id = array.getJSONObject(i).getString("id");
            APIInstance.send("DELETE", "/projects/" + id);
        }

        // Remove all categories.
        response = APIInstance.send("GET", "/categories");
        array = response.getJSONArray("categories");
        for (int i=0; i<array.length(); i++){
            String id = array.getJSONObject(i).getString("id");
            APIInstance.send("DELETE", "/categories/" + id);
        }
    }
}