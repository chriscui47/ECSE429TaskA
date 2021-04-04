package ecse429.group9.restAPI.PartC;

import ecse429.group9.restAPI.APIInstance;
import ecse429.group9.restAPI.StatusCodes;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static java.lang.Thread.sleep;
import static java.util.Objects.requireNonNull;
import static org.junit.Assert.*;

public class TestTodosPerformance {

    public static int errorCount;

@After
    public void killInstance() {
        APIInstance.killInstance();
    }
@Before
    public void startInstance()  throws IOException{
        APIInstance.runApplication();
        assertEquals(200, APIInstance.getStatusCode("/todos"));
    }

    /*
     * Helper methods for the Performance Test Programs
     */

    //POST Todos. Returns the API response code of the Request
    public static int Create_Todos(String title, String description) throws IOException, InterruptedException {

        //Create a new todos instance
        JSONObject newTodo = new JSONObject();
        newTodo.put("title", title);
        newTodo.put("description", description);
        newTodo.put("doneStatus", false);

        //Send POST Request
        int response = APIInstance.post2("/todos", newTodo.toString());

        return response;
    }

    //DELETE Todos. Returns the API response code of the Request
    public static int Delete_Todos(int id) throws IOException {

        //Todos to delete
        String endpoint = "/todos/" + id;

        //Check for invalid endpoint
        JSONObject object = APIInstance.request("GET", endpoint);

        //Make sure GET response is not null to avoid exceptions
        if (object != null) {

            //DELETE request
            JSONObject response = APIInstance.request("DELETE", endpoint);
            if (response != null) {
                return 200;
            } else {
                return 400;
            }
        } else {
            return 400;
        }
    }

    //CHANGE Todos. Returns the API response code of the Request.
    public int Change_Todos(int id, String title, String description) throws IOException, InterruptedException {

        //Todos to Change
        String endpoint = "/todos/" + id;

        //Check for invalid endpoint
        JSONObject object = APIInstance.request("GET", endpoint);

        if (object != null) {

            //Create todos instance with all fields updated with new values
            JSONObject updatedTodo = new JSONObject();
            updatedTodo.put("title", title);
            updatedTodo.put("doneStatus", true);
            updatedTodo.put("description", description);

            //POST request for the updated todos
            return APIInstance.post2(endpoint, updatedTodo.toString());
        } else {
            return 400;
        }
    }







    /*
     * Performance Tests
     */
    @Test
    public void performanceTestCreate() throws IOException, InterruptedException {

        errorCount = 0;

        for (int i=1; i<1001; i++){
            int code = Create_Todos("Test "+i+"","Dummy entry #"+i+"");

            if (code != 201) {
                errorCount++;
            } else {

                //TODO add print time stamp and other things

            }
        }
        System.out.println("Performance Test - Creating Todos (Completed)");
        System.out.println("Error Count: "+errorCount+"\n");

        assertTrue("Too many failed POST requests... Error count is too high.",errorCount < 10);
    }


    @Test
    public void performanceTestDelete() throws IOException, InterruptedException {

        errorCount = 0;

        for (int i=1; i<1001; i++){
            int response = Create_Todos("Test "+i+"","Dummy entry #"+i+"");

            if (response != 201) {
                errorCount++;
            }
        }
        System.out.println("Dummy entries created. Failed Create Count: "+errorCount+".\n" +
                "Starting the 'Performance Test - Deleting Todos'\n");

        for (int i=1; i>1001; i++){
            int response = Delete_Todos(i);

            if (response != 200) {
                errorCount++;
            } else {

                //TODO add print time stamp and other things

            }
        }
        System.out.println("Performance Test - Deleting Todos (Completed)");
        System.out.println("Error Count: "+errorCount+"\n");

        assertTrue("Too many failed POST or DELETE requests... Error count is too high.",
                errorCount < 20);
    }

    @Test
    public void performanceTestChange() throws IOException, InterruptedException {

        errorCount = 0;

        for (int i=1; i<1001; i++){
            int code = Create_Todos("Test "+i,"Dummy entry #"+i);

            if (code != 201) {
                errorCount++;
            } else {
                int success = Change_Todos(i, "New, Test "+i,"New Dummy entry #"+i);

                if (success != 200) {
                    errorCount++;
                }

                //TODO print timestamp

            }
        }
        System.out.println("Performance Test - Changing Todos (Completed)");
        System.out.println("Error Count: "+errorCount+"\n");

        assertTrue("Too many failed POST requests... Error count is too high.",errorCount < 20);
    }
}
