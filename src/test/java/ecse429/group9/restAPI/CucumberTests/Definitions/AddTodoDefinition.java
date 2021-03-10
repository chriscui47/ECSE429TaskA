package ecse429.group9.restAPI.CucumberTests.Definitions;

import ecse429.group9.restAPI.APIInstance;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import static java.lang.Thread.sleep;
import static java.util.Objects.requireNonNull;
import static org.junit.Assert.*;

public class AddTodoDefinition {

    public static int prevCount;

    //Story test conditions
    @Given("{string} is the title of the new Todo")
    public void isTheTitleOfTheTodo(String title){
        CommonDefinition.json.put("title", title);
    }
    @Given("{string} is the completion status of the new Todo")
    public void isTheDoneStatusOfTheTodo(String doneStatus){
        CommonDefinition.json.put("doneStatus", doneStatus.equals("true"));
    }
    @Given("{string} is the description of the new Todo")
    public void isTheDescriptionOfTheTodo(String description){
        CommonDefinition.json.put("description", description);
    }




    //Default user request using POST to add the current JSON object
    @When("the user creates a new Todo")
    public void createNewTodo() throws IOException, InterruptedException {

        //GET all todos before the POST request and count them
        prevCount = requireNonNull(APIInstance.request("GET", "/todos"))
                .getJSONArray("todos").length();

        //Send POST Request
        APIInstance.post("/todos", CommonDefinition.json.toString());
        sleep(500);
    }




    //Normal flow operations
    @Then("a todo instance with {string}, {string} and {string} will be created")
    public void aNewTodoWithTitleStatusDescriptionWillBeCreated(String title, String doneStatus, String description)
            throws IOException {

        //GET all todos after the POST request and count them
        int currCount = requireNonNull(APIInstance.send("GET", "/todos"))
                .getJSONArray("todos").length();

        //Validate that the newest Todos has been created
        if(currCount != prevCount + 1) {
            fail("Error: Failed to create the new Todo instance.");
        }

        //The title will have the white spaces replaced with %20 to become a URL Query Parameter to filter
        String filter = title.replaceAll(" ", "%20");
        //Filter added to the endpoint of the GET request.
        String endpoint = "/todos?title=" + filter;

        //GET the newest created Todos
        JSONArray todoArray = requireNonNull(APIInstance.request("GET", endpoint)).getJSONArray("todos");

        if(todoArray.length() != 1) {
            fail("Error: Duplicate Todo instances with the Title: "+ title +" .  " + todoArray.length());
        } else {
            //GET the newest created Todos
            JSONObject currentTodo = todoArray.getJSONObject(0);

            //Extract the title, the doneStatus and the Description of the newest Todos instance
            String todoTitle = currentTodo.getString("title");
            boolean todoStatus = currentTodo.getBoolean("doneStatus");
            String todoDescription = currentTodo.getString("description");

            //Validate all the fields of the new Todos
            assertTrue("Error: The new Todo instance does not have the correct Title: " + todoTitle + ";" +
                            " doneStatus: " + todoStatus + "; or Description: " + todoDescription + ".",
                    todoTitle.equals(title)
                            && todoStatus == doneStatus.equals("true")
                            && todoDescription.equals(description)
            );
        }
        //APIInstance.killInstance();
    }

    //Alternate flow operations
    @Then("a Todo instance with {string} will be created")
    public void aNewTodoWithOnlyTitleWillBeCreated(String title) throws IOException {

        //GET all todos after the POST request and count them
        int currCount = requireNonNull(APIInstance.send("GET", "/todos"))
                .getJSONArray("todos").length();

        //Validate that the newest Todos has been created
        if(currCount != prevCount + 1) {
            fail("Error: Failed to create the new Todo instance.");
        }


        //The title will have the white spaces replaced with %20 to become a URL Query Parameter to filter
        String filter = title.replaceAll(" ", "%20");
        //Filter added to the endpoint of the GET request.
        String endpoint = "/todos?title=" + filter;

        //GET the newest created Todos
        JSONArray todoArray = requireNonNull(APIInstance.request("GET", endpoint)).getJSONArray("todos");

        if(todoArray.length() != 1) {
            fail("Error: Duplicate Todo instances with the Title: "+ title +" .  " + todoArray.length());
        } else {
            //GET the newest created Todos
            JSONObject currentTodo = todoArray.getJSONObject(0);

            //Extract the title of the newest Todos instance and Validate the field
            assertEquals("Error: The new Todo instance does not have the correct Title." + currentTodo.getString("title") + ".",
                    title, currentTodo.getString("title"));
        }
        //APIInstance.killInstance();
    }

    //Error flow operations
    @Then("a Todo instance will not be created")
    public void aTodoWithoutATitleWillNotBeCreated() throws IOException {

        //GET all todos after the POST request and count them
        int currCount = requireNonNull(APIInstance.request("GET", "/todos"))
                .getJSONArray("todos").length();

        //Compare the quantity of todos after the POST request with the quantity before
        assertEquals(prevCount, currCount);

        //APIInstance.killInstance();
    }
}
