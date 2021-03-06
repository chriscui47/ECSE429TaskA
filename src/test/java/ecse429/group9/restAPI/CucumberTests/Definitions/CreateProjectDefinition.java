package ecse429.group9.restAPI.CucumberTests.Definitions;



import ecse429.group9.restAPI.APIInstance;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONObject;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class CreateProjectDefinition {

    public static JSONObject json = null;
    public static boolean error = false;

    @Given("the Todo API server is running")
    public void the_Todo_API_server_is_running(){
        APIInstance.runApplication();
        json = new JSONObject();
    }

    @Given("{string} is the title of the project")
    public void is_the_title_of_the_class(String title){
        json.put("title", title);
    }

    @Given("{string} is the id of the project")
    public void is_the_id_of_the_class(String id){
        json.put("id", id);
    }

    @Given("{string} is the description of the project")
    public void isTheDescriptionOfTheClass(String description){
        json.put("description", description);
    }

    @Given("{string} is the active state of the project")
    public void is_the_done_status_of_the_project(String active){
        boolean status = false;
        if (active.equals("true")){
            status = true;
        }
        json.put("active", status);
    }
    @Then("there exists 0 projects")
    public void thereExistsTwoProjects() throws IOException {
        JSONObject response = APIInstance.request("GET", "/projects");
        assertEquals(0, response.getJSONArray("projects").length());
    }

    @Given("{string} is the completion state of the project")
    public void is_the_completion_state_of_the_project(String completed){
        boolean status = false;
        if (completed.equals("true")){
            status = true;
        }
        json.put("completed", status);
    }

    //Scenario Outline: Normal Flow

    @When("the user creates a project for a class")
    public void the_user_creates_a_new_project(){
        if (!(json.has("title"))){
            error = true;
        }
        try {
            APIInstance.post("/projects", json.toString());
        } catch (IOException e) {
            error = true;
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Then("a project with {string} will be created")
    public void aTodoInstanceWithWillBeCreated(String title){
        JSONObject response = null;
        try {
            response = APIInstance.send("GET", "/projects?title=" + title);
        } catch (IOException e) {
            error = true;
        }

        assertEquals(title, response.getJSONArray("projects").getJSONObject(0).getString("title"));
    }

    //Scenario Outline: Alternative Flow

    @Then("a project with {string}, {string}, {string} will be created")
    public void aProjectInstanceWithWillBeCreated(String title, String active, String description){
        JSONObject response = null;

        try {
            response = APIInstance.send("GET", "/projects?title=" + title);
            System.out.println(response.toString());
        } catch (IOException e) {
            error = true;
        }

        JSONObject projects = response.getJSONArray("projects").getJSONObject(0);

        assertEquals(title, projects.getString("title"));
        assertEquals(description, projects.getString("description"));
    }

    //Scenario Outline: Error Flow
    @Then("error 404 will occur")
    public void error404WillOccur(){
        // assuming we aren't supposed to be able to create a project without a title.
        assertEquals(true, error);
    }

    @After
    public void shutdown(){
        APIInstance.killInstance();
        error = false;
    }

}