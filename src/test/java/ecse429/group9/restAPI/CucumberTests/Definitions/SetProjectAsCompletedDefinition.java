package ecse429.group9.restAPI.CucumberTests.Definitions;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import ecse429.group9.restAPI.APIInstance;

import static org.junit.Assert.assertEquals;

public class SetProjectAsCompletedDefinition {

    String error;
    public static JSONObject json = null;


    @Given("a project with the title {string} and completed status {string}")
    public void a_todo_with_the_title_and_done_status(String title, String prevCompletedStatus) throws IOException {
        JSONObject json = new JSONObject();

        json.put("title", title);

        boolean completedStatus = false;
        if (prevCompletedStatus.equals("true")){
            completedStatus = true;
        }
        json.put("completed", completedStatus);
        APIInstance.post("/project", json.toString());



        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }






    @When("the user requests to mark the project {string} with a completed status {string}")
    public void the_user_requests_to_mark_the_project_with_a_done_status(String title, String nextCompletedStatus) throws IOException {
        JSONObject json = new JSONObject();
        boolean completedStatus = false;
        if (nextCompletedStatus.equals("true")){
            completedStatus = true;
        }
        json.put("completed", completedStatus);
        JSONObject response = APIInstance.send("GET", "/projects?title=" + title);

        if (response.getJSONArray("projects").length() != 0){
            String id = response.getJSONArray("projects").getJSONObject(0).getString("id");
            APIInstance.post("/projects/" + id, json.toString());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            error = "404";
        }
    }

    @Then("the project {string} will be marked with the completed status {string}")
    public void the_task_will_be_marked_with_the_done_status(String title, String nextCompletedStatus) throws IOException {
        JSONObject response = APIInstance.send("GET", "/projects?title=" + title);
        String completedStatus = response.getJSONArray("projects").getJSONObject(0).getString("completed");
        assertEquals(nextCompletedStatus, completedStatus);
    }

    @Given("no project with id {string} is registered in the API server")
    public void no_todo_with_id_is_registered_in_the_API_server(String id) throws IOException {
        JSONObject response = APIInstance.send("DELETE", "/projects/" + id);
    }

    @Then("system will output an error with error code {string}")
    public void system_will_output_an_error_with_error_code(String errorCode){
        assertEquals(error, errorCode);
    }

}