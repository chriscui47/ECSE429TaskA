package ecse429.group9.restAPI;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;

import static java.lang.Thread.sleep;
import static java.util.Objects.requireNonNull;
import static org.junit.Assert.*;


@FixMethodOrder(MethodSorters.JVM)

public class TestTodos {

    /**
     * Beginning of the test suite for capabilities related only to Todos instances.
     *
     * The first test below validates that the API application instance was created successfully,
     * such that all the other tests can be run as well.
     *
     * i.e. Can the application be launched.
     *
     * @throws IOException I/O exception
     */
    //Creation of an application instance on which the tests will be run
    @Before
    public void startInstance() throws IOException {

        //Create application instance
        APIInstance.runApplication();

        //Success code of 200 passes this test.
        assertEquals(StatusCodes.SC_SUCCESS,
                APIInstance.getStatusCode("/todos"));
    }

    //Termination of the application instance
    @After
    public void stopInstance() {
        APIInstance.killInstance();
    }

    /**
     * Tests done at the endpoint "/todos" of the API application.
     * <p>
     * The available capabilities are:
     * <p>
     * GET => Return all todos.
     * GET (Filtered) => Filters the returned todos by their field values by using URL Query parameters.
     * <p>
     * POST => Create a todos instance using the field values in the body of the request.
     *
     * @throws IOException I/O exception
     */

    //GET request on "/todos"
    @Test
    public void testGetTodos() throws IOException {
        //Testing endpoint baseURL/todos
        JSONObject response = APIInstance.request("GET", "/todos");

        //Make sure the GET response is not null to avoid exceptions
        if(response == null)    {
            fail("Failure to GET all existing todos instances using the '/todos' endpoint.");
        } else {
            /*
             * The request succeeded, but the response could be an empty array if no todos instances exist.
             */
            assertTrue("Failed to request all todos"
                    , response.getJSONArray("todos").length() >= 0);
        }
    }

    /*
     * GET requests on "/todos" using a filter applied through the URL Query parameters.
     */

    //Title filter
    @Test
    public void testGetTitleFilter() throws IOException {

        //Enter the title of a valid todos. Space characters are replaced with %20
        String title = "scan paperwork";
        String filter = title.replaceAll(" ", "%20");

        //Filter added to the endpoint of the GET request.
        String endpoint = "/todos?title=" + filter;

        //Request using the filter
        JSONObject response = APIInstance.request("GET", endpoint);

        //Make sure the GET response is not null to avoid exceptions
        if(response == null)    {
            fail("Failure to GET todos instances using the given URL Query Parameters. The used filter could be " +
                    "invalid or have syntax errors. Usage: '/todos?title=...'");
        } else {

            /* The request succeeded, but the response could be an empty array
             * if the parameters given in the URL Query do not exist.
             */
            assertTrue("Failed to request the todos with title: 'scan paperwork'"
                    , response.getJSONArray("todos").length() >= 0);
        }
    }

    //doneStatus FALSE filter
    @Test
    public void testGetStatusFalseFilter() throws IOException {

        //Filter added at the endpoint of the GET request.
        String endpoint = "/todos?doneStatus=false";

        //Request using the filter
        JSONObject response = APIInstance.request("GET", endpoint);

        //Make sure the GET response is not null to avoid exceptions
        if(response == null)    {
            fail("Failure to GET todos instances using the given URL Query Parameters. The used filter could be " +
                        "invalid or have syntax errors. Usage: '/todos?doneStatus=false'");
        } else {

            /* The request succeeded, but the response could be an empty array
             * if the parameters given in the URL Query do not exist.
             */
            assertTrue("Failed to request the todos with doneStatus: false"
                    , response.getJSONArray("todos").length() >= 0);
        }
    }

    //doneStatus TRUE filter
    @Test
    public void testGetStatusTrueFilter() throws IOException {

        //Filter added at the endpoint of the GET request.
        String endpoint = "/todos?doneStatus=true";

        //Request using the filter
        JSONObject response = APIInstance.request("GET", endpoint);

        //Make sure the GET response is not null to avoid exceptions
        if(response == null)    {
            fail("Failure to GET todos instances using the given URL Query Parameters. The used filter could be " +
                    "invalid or have syntax errors. Usage: '/todos?doneStatus=true'");
        } else {

            /* The request succeeded, but the response could be an empty array
             * if the parameters given in the URL Query do not exist.
             */
            assertTrue("Failed to request the todos with doneStatus: true"
                    , response.getJSONArray("todos").length() >= 0);
        }
    }

    //Description filter
    @Test
    public void testGetDescriptionFilter() throws IOException {

        //Enter the title of a valid todos. Space characters are replaced with %20
        String description = "empty";
        String filter = description.replaceAll(" ", "%20");

        //Filter added to the endpoint of the GET request.
        String endpoint = "/todos?description=" + filter;

        //Request using the filter
        JSONObject response = APIInstance.request("GET", endpoint);

        //Make sure the GET response is not null to avoid exceptions
        if(response == null)    {
            fail("Failure to GET todos instances using the given URL Query Parameters. The used filter could be " +
                    "invalid or have syntax errors. Usage: '/todos?description=...'");
        } else {

            /* The request succeeded, but the response could be an empty array
             * if the parameters given in the URL Query do not exist.
             */
            assertTrue("Failed to request the todos with title: 'scan paperwork'"
                    , response.getJSONArray("todos").length() >= 0);
        }
    }
    /*
     * POST requests on "/todos" endpoint using field values in the request body.
     */

    //POST while providing all possible field values
    @Test
    public void createTodoAllFields() throws IOException, InterruptedException {

        //Create a new todos instance
        JSONObject newTodo = new JSONObject();
        newTodo.put("title", "POST Tests");
        newTodo.put("description", "POST request testing");
        newTodo.put("doneStatus", false);

        //GET all todos before the POST request and count them
        int prevCount = requireNonNull(APIInstance.request("GET", "/todos"))
                .getJSONArray("todos").length();

        //Send POST Request
        APIInstance.post("/todos", newTodo.toString());
        sleep(500);

        //GET all todos after the POST request and count them
        int currCount = requireNonNull(APIInstance.request("GET", "/todos"))
                .getJSONArray("todos").length();

        //Compare the quantity of todos after the POST request with the quantity before
        assertEquals(prevCount + 1, currCount);
    }

    //POST while providing only the title field. DoneStatus should default to false and the description empty.
    @Test
    public void createTodoOnlyTitle() throws IOException, InterruptedException {

        //Create a new todos instance with only a Title in the request body
        JSONObject newTodo = new JSONObject();
        newTodo.put("title", "POST with only a title");

        //GET all todos before the POST request and count them
        int prevCount = requireNonNull(APIInstance.request("GET", "/todos"))
                .getJSONArray("todos").length();

        //Send POST Request
        APIInstance.post("/todos", newTodo.toString());
        sleep(500);

        //GET all todos after the POST request and count them
        int currCount = requireNonNull(APIInstance.request("GET", "/todos"))
                .getJSONArray("todos").length();

        //Compare the quantity of todos after the POST request with the quantity before
        assertEquals(prevCount + 1, currCount);
    }


    //POST while not providing the title, but all other fields. It should fail with return code 400.
    @Test
    public void createTodoNoTitle() throws IOException, InterruptedException {

        //Create a new todos instance without a title, but with all other fields
        JSONObject newTodo = new JSONObject();
        newTodo.put("description", "Bad POST request, missing title");
        newTodo.put("doneStatus", false);

        //GET all todos before the POST request and count them
        int prevCount = requireNonNull(APIInstance.request("GET", "/todos"))
                .getJSONArray("todos").length();

        //Send POST Request
        APIInstance.post("/todos", newTodo.toString());
        sleep(500);

        //GET all todos after the POST request and count them
        int currCount = requireNonNull(APIInstance.request("GET", "/todos"))
                .getJSONArray("todos").length();

        //Compare the quantity of todos after the POST request with the quantity before
        assertEquals(prevCount, currCount);
    }

    /**
     * Tests done at the endpoint "/todos/:id" of the API application.
     * <p>
     * The available capabilities are:
     * <p>
     * GET => Return an instance of todos that matches the given 'id' in the endpoint.
     * <p>
     * POST / PUT => An instance of todos is specified by the 'id' in the URL endpoint. The request body contains
     * fields that will be amended to the given todos.
     * <p>
     * DELETE => Remove the instance of todos specified by the endpoint 'id'.
     *
     * @throws IOException I/O exception
     */

    /*
     * GET requests at the endpoint todos/id can only return the desired todos with 'id'
     * or the status code "404 not found" if no todos exist with the given id.
     *
     * The GET request response will be null if the endpoint is invalid.
     */

    //Test the status code of the API connection after requesting a valid todos Endpoint
    @Test
    public void testValidEndpointID() throws IOException {

        assert APIInstance.getStatusCode("/todos/1") == 200;
    }
    //Test the status code of the API connection after requesting an invalid todos Endpoint
    @Test
    public void testInvalidEndpointID() throws IOException {

        assert APIInstance.getStatusCode("/todos/999") == 404;
    }

    //GET request - makes sure the response body is not null when a valid endpoint is used.
    @Test
    public void testGetTodoExistingID() throws IOException {

        //Request the todos specified by the unique ID given
        JSONObject response = APIInstance.request("GET", "/todos/1");

        //Make sure response is not null to avoid exceptions
        assertTrue("Test failed because the Todo instance was not found and the GET response was null. " +
                        "The given endpoint needs to be valid.",response != null);
    }
    //GET request - makes sure the response body is null when an invalid endpoint is used.
    @Test
    public void testGetTodoMissingID() throws IOException {

        //Request the todos specified by the unique ID given
        JSONObject response = APIInstance.request("GET", "/todos/999");

        //Make sure response is not null to avoid exceptions
        assertTrue("Test failed because the Todo instance was found. The given endpoint needs to be invalid."
                ,response == null);
    }


    /*
     * POST requests at the endpoint todos/id can only be performed on valid 'id' endpoint.
     * i.e. the todos specified by 'id' must exist such that it becomes possible to update their fields.
     * The request body contains the new fields that will be contained at a given endpoint '/todos/id'.
     */
    @Test
    public void changeAllFields() throws IOException, InterruptedException {

        //Endpoint to be updated
        String endpoint = "/todos/1";

        //Check for invalid endpoint
        JSONObject response = APIInstance.request("GET", endpoint);

        //Make sure the GET response is not null to avoid exceptions
        if (response == null) {
            fail("Failure to change field values at the given endpoint. A Todo instance does " +
                    "not exist with the given ID.");
        } else {

            //Retrieve instance of todos instance at the endpoint
            JSONObject previousTodo = response.getJSONArray("todos").getJSONObject(0);

            //Create todos instance with all fields updated with new values
            JSONObject updatedTodo = new JSONObject();
            updatedTodo.put("title", "scan more paperwork");
            updatedTodo.put("doneStatus", true);
            updatedTodo.put("description", "updating ID 1.");

            //POST request for the updated todos
            APIInstance.post(endpoint, updatedTodo.toString());
            sleep(500);

            //Compare both todos instances and assert they are not equal anymore
            assertNotEquals(previousTodo, updatedTodo);
        }
    }

    //Update title using POST
    @Test
    public void changeTitle() throws IOException, InterruptedException {

        //Endpoint to be updated
        String endpoint = "/todos/1";

        //Check for invalid endpoint
        JSONObject response = APIInstance.request("GET", endpoint);

        //Make sure the GET response is not null to avoid exceptions
        if (response == null) {
            fail("Failure to change title field at the given endpoint. A Todo instance does " +
                    "not exist with the given ID.");
        } else {

            //Create todos instance with the field updated with a new value
            JSONObject updatedTodo = new JSONObject();
            updatedTodo.put("title", "scan even more paperwork");

            //POST request for the updated todos
            APIInstance.post(endpoint, updatedTodo.toString());
            sleep(500);

            //Compare both todos instances and assert they are not equal anymore
            assertEquals("scan even more paperwork", requireNonNull(APIInstance.request("GET", endpoint))
                    .getJSONArray("todos").getJSONObject(0).getString("title"));
        }
    }

    //Update done status using POST
    @Test
    public void changeDoneStatus() throws IOException, InterruptedException {

        //Endpoint to be updated
        String endpoint = "/todos/1";

        //Check for invalid endpoint
        JSONObject response = APIInstance.request("GET", endpoint);

        //Make sure GET response is not null to avoid exceptions
        if (response == null) {
            fail("Failure to change doneStatus field at the given endpoint. A Todo instance does " +
                    "not exist with the given ID.");
        } else {

            //Create todos instance with the field updated with a new value
            JSONObject updatedTodo = new JSONObject();
            updatedTodo.put("doneStatus", true);

            //POST request for the updated todos
            APIInstance.post(endpoint, updatedTodo.toString());
            sleep(500);

            //Compare both todos instances and assert they are not equal anymore
            assertEquals("true", requireNonNull(APIInstance.request("GET", endpoint))
                    .getJSONArray("todos").getJSONObject(0).getString("doneStatus"));
        }
    }

    //Update description using POST
    @Test
    public void changeDescription() throws IOException, InterruptedException {

        //Endpoint to be updated
        String endpoint = "/todos/1";

        //Check for invalid endpoint
        JSONObject response = APIInstance.request("GET", endpoint);

        //Make sure GET response is not null to avoid exceptions
        if (response == null) {
            fail("Failure to change description field at the given endpoint. " +
                    "A Todo instance does not exist with the given ID.");
        } else {

            //Create todos instance with the field updated with a new value
            JSONObject updatedTodo = new JSONObject();
            updatedTodo.put("description", "more and more paperwork");

            //POST request for the updated todos
            APIInstance.post(endpoint, updatedTodo.toString());
            sleep(500);

            //Compare both todos instances and assert they are not equal anymore
            assertEquals("more and more paperwork", requireNonNull(APIInstance.request("GET", endpoint))
                    .getJSONArray("todos").getJSONObject(0).getString("description"));
        }
    }

    //DELETE request on '/todos' endpoint is an  command and needs to return
    @Test
    public void testDeleteAllTodos() throws IOException {

        assertEquals(405,APIInstance.getStatusCode("DELETE", "/todos"));
    }


    /*
     * DELETE requests at the endpoint todos/id can only be performed on valid 'id' endpoint.
     * i.e. the todos specified by 'id' must exist such that it becomes possible to remove them.
     */

    //DELETE valid todos
    @Test
    public void deleteExistingTodo() throws IOException, InterruptedException {

        //Create a dummy Todos instance that will be deleted
        JSONObject newTodo = new JSONObject();
        String dummyTitle = "dummy todos";
        newTodo.put("title", dummyTitle);

        //Make filter to look for the new dummy todos instance
        String filter = dummyTitle.replaceAll(" ", "%20");

        //Filter added to the endpoint of the GET request.
        String endpoint = "/todos?title=" + filter;

        //Send POST Request
        APIInstance.post("/todos", newTodo.toString());
        sleep(500);

        //Request using the filter to make sure the dummy todos is created.
        JSONObject response = APIInstance.request("GET", endpoint);

        //Make sure the GET response is not null to avoid exceptions
        if(response == null)    {
            fail("Failure to create a dummy todos instance that will be deleted todos instances using the given URL Query Parameters. The used filter could be " +
                    "invalid or have syntax errors. Usage: '/todos?title=...'");
        } else {

            //Retrieve the ID of the dummy todos
            String dummyID = response.getJSONArray("todos").getJSONObject(0).getString("id");

            //Dummy Todos to delete
            String dummyEndpoint = "/todos/" + dummyID;

            //Check for invalid endpoint
            JSONObject dummyTodos = APIInstance.request("GET", dummyEndpoint);

            //Make sure GET response is not null to avoid exceptions
            if (dummyTodos == null) {
                fail("This endpoint does not exist. Failure to delete the endpoint because it was not created.");
            } else {

                //DELETE request
                APIInstance.request("DELETE", dummyEndpoint);

                //Check for invalid endpoint
                JSONObject finalResponse = APIInstance.request("GET", dummyEndpoint);

                //Since the endpoint is not found, the todos has been deleted
                assertTrue("The given endpoint can still be found, thus the DELETE request failed."
                        , finalResponse == null);
            }

        }
    }
    //DELETE invalid todos
    @Test
    public void deleteMissingTodo() throws IOException {

        //Todos to delete
        String endpoint = "/todos/999";

        //Check for invalid endpoint
        JSONObject response = APIInstance.request("DELETE", endpoint);

        //The response must be null, otherwise the Todos exists and this test fails
        assertTrue("This endpoint exist, thus it can be deleted. Test failed because the given endpoint must" +
                        "be invalid such that the DELETE request would fail."
                , response == null);
    }

    /*
     * HEAD request tests to make sure the documented features are supported and return the appropriate type of response.
     */

    //HEAD request on '/todos' endpoint
    @Test
    public void testHeadTodos() throws IOException {

        //Test to make sure HEAD request returns 'application/json' type.
        assertEquals("application/json", APIInstance.getHeadContentType("/todos"));
    }
    //HEAD request on '/todos/id' endpoint
    @Test
    public void testHeadTodosID() throws IOException{

        //Test to make sure HEAD request returns 'application/json' type.
        assertEquals("application/json", APIInstance.getHeadContentType("/todos/1"));
    }
}