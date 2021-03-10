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

public class SearchTodoDefinition {

    public static int prevCount;
    public static String filterEndpoint;
    public static String invalidFilterEndpoint;

    //Story test conditions
    @Given("Todos {string} and {string} with the completion statuses {string} and {string} exist")
    public void createTodosToFilter(String title1, String title2, String doneStatus1, String doneStatus2)
            throws IOException, InterruptedException {
        //Create first Todos
        JSONObject json1 = new JSONObject();
        json1.put("title", title1);
        json1.put("doneStatus", doneStatus1.equals("true"));

        //Create second Todos
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

    @Given("a Todo with title {string} exists")
    public void createATodoToFilter(String title) throws IOException, InterruptedException {
        CommonDefinition.json.put("title", title);

        //GET all todos before the POST request and count them
        prevCount = requireNonNull(APIInstance.request("GET", "/todos"))
                .getJSONArray("todos").length();

        //Send POST Requests
        APIInstance.post("/todos", CommonDefinition.json.toString());
        sleep(500);

        //GET all todos after the POST request and count them
        int currCount = requireNonNull(APIInstance.send("GET", "/todos"))
                .getJSONArray("todos").length();

        assertEquals("Error: Failed to create the Todo instance with title: "+ title +"",
                prevCount + 1, currCount);
    }




    @When("the user wants to filter for completion status {string}")
    public void makeStatusFilterEndpoint(String filter) throws IOException {
        //Filter added to the endpoint of the GET request.
        filterEndpoint = "/todos?doneStatus=" + filter;
    }

    @When("the user wants to filter for {string} todos")
    public void makeTitleFilterEndpoint(String title) throws IOException, InterruptedException {

        //The title will have the white spaces replaced with %20 to become a URL Query Parameter to filter
        String filter = title.replaceAll(" ", "%20");
        //Filter added at the endpoint of the URL Query parameters.
        filterEndpoint = "/todos?title=" + filter;
    }

    @When("the user wants to filter for {string} todos using an invalid URL Query")
    public void makeValidAndInvalidTitleFilters(String title) throws IOException {

        //The title will have the white spaces replaced with %20 to become a URL Query Parameter to filter
        String filter = title.replaceAll(" ", "%20");

        //Create valid filter
        filterEndpoint = "/todos?title=" + filter;
        //Create invalid filter
        invalidFilterEndpoint = "/todos? ti tle=" + filter;
    }




    //Normal flow operations
    @Then("Todos will be filtered by completion status {string}")
    public void filterDoneStatus(String filter) throws IOException {

        //Request using the filter
        JSONObject response = APIInstance.request("GET", filterEndpoint);

        //Make sure the GET response is not null to avoid exceptions
        if(response == null)    {
            fail("Failed to GET todos instances using the given URL Query Parameters.");
        } else if(response.getJSONArray("todos").length() == 0) {
            //The request succeeded, but returned an empty array because the searched Todos do not exist.
            fail("Error: All Todo instances were filtered by the request.");
        } else {
            //Loops for each filtered Todos instance
            for (int i = 0; i < response.getJSONArray("todos").length(); i++) {
                //Extract the 'doneStatus' of the Todos
                String status = response.getJSONArray("todos").getJSONObject(i).getString("doneStatus");

                //Validate that the completion status of each filtered todos matches the expected filter
                assertEquals("Error: The Todo list was not properly filtered using the filter: " +
                        "doneStatus = \""+ filter +"\"", filter, status);
            }
        }
    }

    //Alternate flow operations
    @Then("Todos will be filtered by title {string}")
    public void filterTitle(String title) throws IOException {

        //GET Request using the filter
        JSONObject response = APIInstance.request("GET", filterEndpoint);

        //Make sure the GET response is not null to avoid exceptions
        if(response == null)    {
            fail("Failed to GET todos instances using the given URL Query Parameters.");
        } else if(response.getJSONArray("todos").length() == 0) {
            //The request succeeded, but returned an empty array because the searched Todos do not exist.
            fail("Error: All Todo instances were filtered by the request.");
        } else {
            //Loops for each filtered Todos instance
            for (int i = 0; i < response.getJSONArray("todos").length(); i++) {
                //Extract the 'doneStatus' of the Todos
                String filteredTitle = response.getJSONArray("todos").getJSONObject(i).getString("title");

                //Validate that the completion status of each filtered todos matches the expected filter
                assertEquals("Error: The Todo list was not properly filtered using the filter: " +
                        "title = \""+ title +"\"", title, filteredTitle);
            }
        }
    }

    //Error flow operations
    @Then("the Todo instances will not be filtered")
    public void failedTitleFilter() throws IOException {

        //GET Request and count all Todos that should are returned by a valid filter
        int validFilterCount = requireNonNull(APIInstance.request("GET", filterEndpoint)).
                getJSONArray("todos").length();

        //GET Request and count all Todos without a filter
        int totalCount = requireNonNull(APIInstance.request("GET", "/todos")).
                getJSONArray("todos").length();

        //Validate that there exists Todos and that no errors occur because of bad filter values
        if(totalCount == 0 || validFilterCount != 1) {
            fail("Error: No Todos or Bad filter value.");
        } else {
            assertNull("Error: The filter Request using an invalid URL Query did not fail.",
                    APIInstance.request("GET", invalidFilterEndpoint)
            );
        }
    }
}
