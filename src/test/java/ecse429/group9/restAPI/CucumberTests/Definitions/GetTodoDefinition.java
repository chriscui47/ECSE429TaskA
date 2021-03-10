package ecse429.group9.restAPI.CucumberTests.Definitions;

import ecse429.group9.restAPI.APIInstance;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONObject;

import java.io.IOException;

import static java.lang.Thread.sleep;
import static java.util.Objects.requireNonNull;
import static org.junit.Assert.*;

public class GetTodoDefinition {

    public static int prevCount;
    public static String todoID;

    //Story test conditions
    @Given("{string} is the title of an existing Todo")
    public void aTodoWithTitleExists(String title) throws IOException, InterruptedException {
        CommonDefinition.json.put("title", title);

        //GET all todos before the POST request and count them
        prevCount = requireNonNull(APIInstance.request("GET", "/todos"))
                .getJSONArray("todos").length();

        //Send POST Request
        APIInstance.post("/todos", CommonDefinition.json.toString());
        sleep(500);

        //GET all todos after the POST request and count them
        int currCount = requireNonNull(APIInstance.send("GET", "/todos"))
                .getJSONArray("todos").length();

        assertEquals("Error: Failed to create the Todo instance (with title: "+ title +") that will be retrieved.",
                prevCount + 1, currCount);
    }
    @Given("the user knows the ID of the {string} Todo")
    public void userKnowsTheTodoID(String title) throws IOException {
        //The title will have the white spaces replaced with %20 to become a URL Query Parameter to filter
        String filter = title.replaceAll(" ", "%20");
        //Filter added to the endpoint of the GET request.
        String endpoint = "/todos?title=" + filter;

        //Request using the filter
        JSONObject response = APIInstance.request("GET", endpoint);

        //Make sure the GET response is not null to avoid exceptions
        if(response == null)    {
            fail("Failed to GET todos instances using the given URL Query Parameters. The used filter could be " +
                    "invalid or have syntax errors. Usage: '/todos?title=...'");
        } else if(response.getJSONArray("todos").length() == 0) {
            //The request succeeded, but the response could be an empty array.
            fail("Error: No Todos with the title \""+ title +"\" exist.");
        } else if(response.getJSONArray("todos").length() > 1) {
            //The request succeeded, but the response array contained too many items
            fail("Error: Duplicate Todos exists with the title "+ title +"");
        } else {
            /* The response contains only one Todos instance,
             * which validates the existence of the Todos with the given title.
             */
            //Retrieve the ID
            todoID = response.getJSONArray("todos").getJSONObject(0).getString("id");

            assertNotNull("Error: failed to retrieve the Todo ID ("+ todoID +").",
                    todoID);
        }
    }
    @Given("{string} is not the id of an existing Todo")
    public void aTodoDoesNotExistWithID(String id) throws IOException {
        //Endpoint
        String endpoint = "/todos/" + id;
        //GET request should return null, and will be validated by this assertion
        assertNull("Error: A Todo instance with id \""+ id +"\" was found.",
                APIInstance.request("GET", endpoint));
    }
    @Given("{string} is the id of the Todo the user will retrieve")
    public void isTheIDThatWillBeRetrieved(String id) {
        //Set the value of the ID that the user will try to retrieve
        todoID = id;
    }




    @When("the user retrieves a Todo using it's ID")
    public void retrieveTodoUsingID() throws IOException {
        //Endpoint
        String endpoint = "/todos/" + todoID;
        //GET request should not return null, but the response will be validated in the next step
        CommonDefinition.json = APIInstance.request("GET", endpoint);
    }
    @When("the user uses a URL filter to search for the {string} Todo")
    public void retrieveTodoUsingURL(String title) throws IOException, InterruptedException {
        //The title will have the white spaces replaced with %20 to become a URL Query Parameter to filter
        String filter = title.replaceAll(" ", "%20");
        //Filter added to the endpoint of the GET request.
        String endpoint = "/todos?title=" + filter;

        //Request using the filter
        CommonDefinition.json = APIInstance.request("GET", endpoint);
    }




    //Normal flow operations
    @Then("the {string} todo instance will be returned")
    public void validateRetrievedTodo(String title) {
        //Validate that the GET response is not null
        if(CommonDefinition.json == null)    {
            fail("Error: A Todo instance with id \""+ todoID +"\" was not found.");
        } else {
            //Extract title of the retrieved Todos
            String actualTitle =CommonDefinition.json.getJSONArray("todos").getJSONObject(0).getString("title");

            //Validate the title of the returned Todos
            assertEquals("Error: The title of the retrieved Todo does not match the expected value.",
                    title, actualTitle);
        }
    }

    //Alternate flow operations
    @Then("one or more {string} todo instances will be returned")
    public void removeCompletedTodos(String title) {
        //Make sure the GET response is not null to avoid exceptions
        if(CommonDefinition.json == null)    {
            fail("Failure to GET todos instances using the given URL Query Parameters: /todos?title="+ title +"");
        } else if(CommonDefinition.json.getJSONArray("todos").length() != 0) {
            //Loops for each completed Todos
            for (int i = 0; i < CommonDefinition.json.getJSONArray("todos").length(); i++) {
                String actualTitle = CommonDefinition.json.getJSONArray("todos").getJSONObject(i).getString("title");
                //Validate the title
                assertEquals("Error: The title (" + actualTitle + ") of a returned Todo does not match" +
                        " the expected title (" + title + ").", title, actualTitle);
            }
        }
    }

    //Error flow operations
    @Then("no Todo instances will be returned")
    public void failureToRemoveATodo() {
        //Validate that no Todos instances were retrieved using the invalid ID
        assertNull("Error: The title of the retrieved Todo does not match the expected value.",
                CommonDefinition.json);
    }
}
