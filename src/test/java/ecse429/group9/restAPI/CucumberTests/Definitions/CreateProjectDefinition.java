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
    public void the_user_creates_a_new_class(){
        System.out.println(json.toString());
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

    @Then("a project with {string}, {string}, {string}, {string} will be created")
    public void aTodoInstanceWithWillBeCreated(String title, String active, String completed, String description){
        JSONObject response = null;

        try {
            response = APIInstance.send("GET", "/projects?title=" + title);
        } catch (IOException e) {
            error = true;
        }

        JSONObject todoList = response.getJSONArray("projects").getJSONObject(0);

        assertEquals(title, todoList.getString("title"));
        assertEquals(active, todoList.getString("active"));
        assertEquals(completed, todoList.getString("completed"));
        assertEquals(description, todoList.getString("description"));
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