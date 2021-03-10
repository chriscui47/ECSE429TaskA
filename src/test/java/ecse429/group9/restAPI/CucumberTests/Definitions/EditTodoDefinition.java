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

public class EditTodoDefinition {

    public static int prevCount;
    public static String ID;
    public static int requestStatusCode;

    //Story test conditions
    @Given("the {string} Todo with description {string} exists")
    public void titleAndDescriptionTodoExists(String title, String description) throws IOException, InterruptedException {
        CommonDefinition.json.put("title", title);
        CommonDefinition.json.put("description", description);

        //GET all todos before the POST request and count them
        prevCount = requireNonNull(APIInstance.request("GET", "/todos"))
                .getJSONArray("todos").length();

        //Send POST Request
        APIInstance.post("/todos", CommonDefinition.json.toString());
        sleep(500);

        //GET all todos after the POST request and count them
        int currCount = requireNonNull(APIInstance.send("GET", "/todos"))
                .getJSONArray("todos").length();

        //Validate that the Todos with the given title and description now exists
        assertEquals("Error: The specified Todo instance does not exist.",prevCount + 1, currCount);
    }
    //Story test conditions
    @Given("the {string} Todo with completion status {string} exists")
    public void titleAndStatusTodoExists(String title, String doneStatus) throws IOException, InterruptedException {
        CommonDefinition.json.put("title", title);
        CommonDefinition.json.put("doneStatus", doneStatus.equals("true"));

        //GET all todos before the POST request and count them
        prevCount = requireNonNull(APIInstance.request("GET", "/todos"))
                .getJSONArray("todos").length();

        //Send POST Request
        APIInstance.post("/todos", CommonDefinition.json.toString());
        sleep(500);

        //GET all todos after the POST request and count them
        int currCount = requireNonNull(APIInstance.send("GET", "/todos"))
                .getJSONArray("todos").length();

        //Validate that the Todos with the given title and description now exists
        assertEquals("Error: The specified Todo instance does not exist.",prevCount + 1, currCount);
    }

    @Given("the user knows the {string} Todo ID")
    public void theTitleTodoHasID(String title) throws IOException {
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
            //The request succeeded, but the response was an empty array.
            fail("Error: No Todos with the title \""+ title +"\" exist.");
        } else if(response.getJSONArray("todos").length() > 1) {
            //The request succeeded, but the response array contained too many items
            fail("Error: Duplicate Todos exists with the title "+ title +"");
        } else {
            /* The response contains only one Todos instance,
             * which validates the existence of the Todos with the given title.
             */
            //Retrieve the ID
            ID = response.getJSONArray("todos").getJSONObject(0).getString("id");

            assertNotNull("Error: failed to retrieve the Todo ID ("+ ID +").",
                    ID);
        }
    }

    @Given("{string} is the ID of a Todo that does not exist")
    public void aTodoWithIDDoesNotExist(String id) throws IOException {
        //Endpoint
        String endpoint = "/todos/" + id;
        //GET request should return null, and will be validated by the assertion
        assertNull("Error: A Todo instance with id \""+ id +"\" was found.",
                APIInstance.request("GET", endpoint));
    }





    @When("the user wants to modify a Todos title and description to {string} and {string}")
    public void newTitleAndDescription(String newTitle, String newDescription) {

        CommonDefinition.json = new JSONObject();
        CommonDefinition.json.put("title", newTitle);
        CommonDefinition.json.put("description", newDescription);
    }

    @When("the user wants to modify a Todos completion status to {string}")
    public void newTitleAndCompletionStatus(String newDoneStatus) {

        CommonDefinition.json = new JSONObject();
        CommonDefinition.json.put("doneStatus", newDoneStatus.equals("true"));
    }

    @When("the user wants to modify the {string} of a Todo with ID {string}")
    public void modifyTodoWithID(String field, String id) {

        CommonDefinition.json = new JSONObject();
        ID = id;

        //Cases for each field value
        if(field.equals("title")) {
            CommonDefinition.json.put("title", "Error");
        } else if(field.equals("doneStatus")) {
            CommonDefinition.json.put("doneStatus", true);
        } else if(field.equals("description")) {
            CommonDefinition.json.put("description", "Invalid endpoint used!");
        } else {
            fail("Error: The field given is not valid.");
        }
    }




    //Normal flow operations
    @Then("the Todo will have a new title {string} and a new description {string}")
    public void changeTitleAndDescription(String newTitle, String newDescription) throws IOException, InterruptedException {

        //Endpoint to be updated
        String endpoint = "/todos/" + ID;

        //POST request for the updated todos
        requestStatusCode = APIInstance.post2(endpoint, CommonDefinition.json.toString());
        sleep(500);

        /* The endpoint should be valid and will return Null if unexpected errors occur.
         * Returns the newly modified Todos instance with the given ID
         */
        JSONObject modifiedTodo = requireNonNull(APIInstance.request("GET", endpoint)).
                getJSONArray("todos").getJSONObject(0);

        //Extract the newly modified title and description of the Todos
        String modifiedTitle = modifiedTodo.getString("title");
        String modifiedDescription = modifiedTodo.getString("description");

        System.out.println(requestStatusCode);

        //Validate that all the fields have been changed and that they match the expected new values
        assertTrue("Error: The Todo instance with ID \""+ ID +"\" was not modified correctly. " +
                        "Expected title: \""+ newTitle +"\". " +
                        "Actual title: \""+ modifiedTitle +"\". " +
                        "Expected description: \""+ newDescription +"\". " +
                        "Actual description: \""+ modifiedDescription +"\".",
                requestStatusCode == 200
                        && modifiedTitle.equals(newTitle)
                        && modifiedDescription.equals(newDescription)
            );
    }

    //Alternate flow operations
    @Then("the Todo will have a new completion status {string}")
    public void changeCompletionStatus(String newDoneStatus) throws IOException, InterruptedException {

        //Endpoint to be updated
        String endpoint = "/todos/" + ID;

        //POST request for the updated todos
        requestStatusCode = APIInstance.post2(endpoint, CommonDefinition.json.toString());
        sleep(500);

        /* The endpoint should be valid and will return Null if unexpected errors occur.
         * Returns the newly modified Todos instance with the given ID
         */
        JSONObject modifiedTodo = requireNonNull(APIInstance.request("GET", endpoint)).
                getJSONArray("todos").getJSONObject(0);

        //Extract the newly modified title and description of the Todos
        String modifiedStatus = modifiedTodo.getString("doneStatus");

        System.out.println(requestStatusCode);

        //Validate that all the fields have been changed and that they match the expected new values
        assertTrue("Error: The Todo instance with ID \"" + ID + "\" was not modified correctly. " +
                "Expected completion status: \"" + newDoneStatus + "\". " +
                "Actual title: \"" + modifiedStatus + "\". ",
                requestStatusCode == 200
                        && modifiedStatus.equals(newDoneStatus)
        );
    }

    //Error flow operations
    @Then("a Todo instance will not be modified")
    public void aTodoWillNotBeModified() throws IOException, InterruptedException {

        //Endpoint to be updated
        String endpoint = "/todos/" + ID;

        //POST request for the updated todos
        requestStatusCode = APIInstance.post2(endpoint, CommonDefinition.json.toString());
        sleep(500);

        System.out.println(requestStatusCode);

        assertEquals("Error: The post request did not fail when using an invalid Endpoint",
                404, requestStatusCode
        );
    }
}
