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

public class SetProjectAsActive {

    String error;
    public static JSONObject json = null;


    @Given("a project with the title {string} and active status {string}")
    public void a_todo_with_the_title_and_done_status(String title, String prevActiveStatus) throws IOException {
        JSONObject json = new JSONObject();
        json.put("title", title);
        boolean activeStatus = false;
        if (prevActiveStatus.equals("true")){
            activeStatus = true;
        }
        json.put("active", activeStatus);
        APIInstance.post("/project", json.toString());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @When("the user requests to mark the project {string} with an active status {string}")
    public void the_user_requests_to_mark_the_task_with_a_done_status(String title, String nextActiveStatus) throws IOException {
        JSONObject json = new JSONObject();
        boolean activeStatus = false;
        if (nextActiveStatus.equals("true")){
            activeStatus = true;
        }
        json.put("active", activeStatus);
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

    @Then("the project {string} will be marked with the active status {string}")
    public void the_task_will_be_marked_with_the_done_status(String title, String nextActiveStatus) throws IOException {
        JSONObject response = APIInstance.send("GET", "/projects?title=" + title);
        String activeStatus = response.getJSONArray("projects").getJSONObject(0).getString("active");
        assertEquals(activeStatus, nextActiveStatus);
    }

    @Given("no project with id {string} is registered in the API server 1")
    public void no_todo_with_id_is_registered_in_the_API_server(String id) throws IOException {
        JSONObject response = APIInstance.send("DELETE", "/projects/" + id);
    }

    @Then("system will output an error with error code {string} 1")
    public void system_will_output_an_error_with_error_code(String errorCode){
        assertEquals(error, errorCode);
    }


    /*
    @After
    public void clear() throws IOException {
        // Remove all todos.
        JSONObject response = APIInstance.send("GET", "/todos");
        JSONArray array = response.getJSONArray("todos");
        for (int i=0; i<array.length(); i++){
            String id = array.getJSONObject(i).getString("id");
            APIInstance.send("DELETE", "/todos/" + id);
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
    }*/
}