package ecse429.group9.restAPI.CucumberTests.Definitions;

import ecse429.group9.restAPI.APIInstance;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import static java.lang.Thread.sleep;
import static java.util.Objects.requireNonNull;
import static org.junit.Assert.*;

public class RemoveTodoDefinition {

    public static int prevCount;
    public static int trueCount;
    public static int falseCount;
    public static String deleteEndpoint;

    //Story test conditions
    @Given("{string} is the title of a Todo that needs to be removed")
    public void createTodoThatWillBeDeleted(String title) throws IOException, InterruptedException {
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

        assertEquals("Error: Failed to create the Todo instance (with title: "+ title +") that will be deleted.",
                prevCount + 1, currCount);
    }
    @Given("the {string} Todo exists")
    public void checkIfATodoExists(String title) throws IOException {

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
        } else {
            /* The request succeeded, but the response could be an empty array
             * if the values given in the URL Query Parameters do not exist.
             */
            //Validate that a Todos instance exists with the given title
            assertTrue("A Todo instance with title: "+ title +" does not exist and cannot be deleted.",
                    response.getJSONArray("todos").length() > 0);
        }
    }
    @Given("{string} and {string} are existing Todos with the completion status {string} and {string}")
    public void createCompletedTodosThatWillBeDeleted(String title1, String title2, String doneStatus1, String doneStatus2)
            throws IOException, InterruptedException {

        //Create Dummy1 Todos
        JSONObject json1 = new JSONObject();
        json1.put("title", title1);
        json1.put("doneStatus", doneStatus1.equals("true"));

        //Create Dummy2 Todos
        JSONObject json2 = new JSONObject();
        json2.put("title", title2);
        json2.put("doneStatus", doneStatus2.equals("true"));

        //GET all todos before the POST request and count them
        prevCount = requireNonNull(APIInstance.request("GET", "/todos"))
                .getJSONArray("todos").length();

        //Send POST Requests
        APIInstance.post("/todos", json1.toString());
        APIInstance.post("/todos", json2.toString());
        sleep(500);

        //GET all todos after the POST request and count them
        int currCount = requireNonNull(APIInstance.send("GET", "/todos"))
                .getJSONArray("todos").length();

        assertEquals("Error: Failed to create the Todo instances with titles: "+ title1 +" and "+
                        title2 +".",
                prevCount + 2, currCount);
    }
    @Given("{string} is the title of a Todo that does not exist")
    public void checkIfATodoDoesNotExist(String title) throws IOException {

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
        } else {
            /* The request succeeded, but the response could be an empty array
             * if the values given in the URL Query Parameters do not exist.
             */
            //Validate that a Todos instance exists with the given title
            assertEquals("A Todo instance with title: "+ title +" exists and can be deleted.",
                    0, response.getJSONArray("todos").length());
        }
    }




    @When("the user wants to remove the {string} Todo")
    public void setupRemoveATodo(String title) throws IOException {

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
            /* The request succeeded.
             * The response will be an empty array if the Todos do not exist.
             * The response will not be empty if the Todos exist.
             */

            //Invalid Endpoint used because the Todos was not found
            deleteEndpoint = "/todos/999";
        } else {
            //Retrieve the ID of the todos that will be deleted
            String validID = response.getJSONArray("todos").getJSONObject(0).getString("id");
            //Valid Endpoint used for the DELETE request
            deleteEndpoint = "/todos/" + validID;
        }
    }
    @When("the user wants to remove the completed Todos")
    public void setupCompletedTodos() throws IOException, InterruptedException {

        //Filter added at the endpoint of the URL Query parameters.
        String endpoint = "/todos?doneStatus=true";
        //GET Request using the filter
        JSONObject response = APIInstance.request("GET", endpoint);

        //Make sure the GET response is not null to avoid exceptions
        if(response == null)    {
            fail("Failure to GET todos instances using the given URL Query Parameters: /todos?doneStatus=true");
        } else {
            //Count the Todos with doneStatus=true
            trueCount = response.getJSONArray("todos").length();
        }

        //Filter added at the endpoint of the URL Query parameters.
        endpoint = "/todos?doneStatus=false";
        //GET Request using the filter
        response = APIInstance.request("GET", endpoint);

        //Make sure the GET response is not null to avoid exceptions
        if(response == null)    {
            fail("Failure to GET todos instances using the given URL Query Parameters: /todos?doneStatus=false");
        } else {
            //Count the Todos with doneStatus=false
            falseCount = response.getJSONArray("todos").length();
        }
    }



    //Normal flow operations
    @Then("the Todo will be removed")
    public void removeATodo() throws IOException {

        //Count Todos before the DELETE request
        prevCount = requireNonNull(APIInstance.send("GET", "/todos"))
                .getJSONArray("todos").length();

        //DELETE request
        APIInstance.request("DELETE", deleteEndpoint);

        //GET all todos after the DELETE request and count them
        int currCount = requireNonNull(APIInstance.send("GET", "/todos"))
                .getJSONArray("todos").length();

        assertEquals("Error: The valid Todo was not removed.",prevCount - 1, currCount);
    }
    //Alternate flow operations
    @Then("the completed Todo instances will be removed")
    public void removeCompletedTodos() throws IOException {

        //GET all todos before the DELETE requests and count them
        int prevCount = requireNonNull(APIInstance.send("GET", "/todos"))
                .getJSONArray("todos").length();

        //Filter added at the endpoint of the URL Query parameters.
        String endpoint = "/todos?doneStatus=true";
        //GET Request using the filter
        JSONObject response = APIInstance.request("GET", endpoint);

        //Make sure the GET response is not null to avoid exceptions
        if(response == null)    {
            fail("Failure to GET todos instances using the given URL Query Parameters: /todos?doneStatus=true");
        } else {
            //Error checking
            if(response.getJSONArray("todos").length() != trueCount) {
                fail("Error: Previous true count ("+ trueCount +") does not match the current true count.");
            }
            //Loops for each completed Todos
            for (int i = 0; i < trueCount; i++) {
                String id = response.getJSONArray("todos").getJSONObject(i).getString("id");
                //Valid Endpoint used for the DELETE request
                deleteEndpoint = "/todos/" + id;
                //DELETE request
                APIInstance.request("DELETE", deleteEndpoint);
            }
            //GET all todos after the DELETE requests and count them
            int currCount = requireNonNull(APIInstance.send("GET", "/todos"))
                    .getJSONArray("todos").length();

            //Validate that all completed Todos have been removed
            assertTrue("Error: Failed to remove all completed Todos correctly: " +
                            "currCount ("+ currCount +")",
                    currCount == falseCount
                            && currCount == prevCount - trueCount
            );
        }
    }
    //Error flow operations
    @Then("the Todo will not be removed")
    public void failureToRemoveATodo() throws IOException {

        //DELETE request
        CommonDefinition.json = APIInstance.request("DELETE", deleteEndpoint);

        //The response must be null, otherwise the Todos exists and this test fails
        assertNull("This endpoint exists, thus it can be deleted. Test failed because the given endpoint must" +
                " be invalid such that the DELETE request would fail.", CommonDefinition.json);
    }
}
