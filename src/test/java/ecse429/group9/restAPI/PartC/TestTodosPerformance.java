package ecse429.group9.restAPI.PartC;

import ecse429.group9.restAPI.APIInstance;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;

//import static java.lang.Thread.sleep;
import static org.junit.Assert.*;

public class TestTodosPerformance {

    public static int errorCount;
    public static int counter;

    public static long startTime;
    public static long transactionTime;
    public static long zeroTime;
    public static long accumulator;


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

        //DELETE request
        return APIInstance.getStatusCode("DELETE", endpoint);
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
        counter = 0;
        accumulator = 0;

        zeroTime = System.currentTimeMillis();


        FileWriter csvWriter = new FileWriter("todos_performance_create.csv");
        csvWriter.write("Total number of todos,Transaction Time,Current Time MS\n");

        for (int i=1; i<10001; i++) {
            startTime = System.currentTimeMillis();

            int code = Create_Todos("Test "+i+"","Dummy entry #"+i+"");

            counter++;
            //Sample the transaction timestamp
            transactionTime = System.currentTimeMillis() - startTime;

            accumulator += transactionTime;

            //Sample at each interval of 50 transactions
            if (counter == 50) {
                //Add the transaction time sample to the CSV file
                csvWriter.write(i + "," + (accumulator/50.0) + "," + (System.currentTimeMillis() - zeroTime) + "\n");
                counter = 0;
                accumulator = 0;
            }

            if (code != 201) {
                errorCount++;
            }
        }
        System.out.println("Performance Test - Creating Todos (Completed)");
        System.out.println("Error Count: "+errorCount+"\n");

        csvWriter.close();

        assertTrue("Too many failed POST requests... Error count is too high.",errorCount < 10);
    }


    @Test
    public void performanceTestDelete() throws IOException, InterruptedException {

        errorCount = 0;
        counter = 0;
        accumulator = 0;

        zeroTime = System.currentTimeMillis();

        FileWriter csvWriter = new FileWriter("todos_performance_delete.csv");
        csvWriter.write("Total number of todos,Transaction Time,Current Time MS\n");

        for (int i=1; i<10001; i++){
            int response = Create_Todos("Test "+i+"","Dummy entry #"+i+"");

            if (i == 9995) {
                System.out.println("almost done adding... start perfmon");
            }

            if (response != 201) {
                errorCount++;
            }
        }

        //Check for invalid endpoint
        JSONObject object = APIInstance.request("GET", "/todos/500");
        boolean beforeValidate = (object == null);

        System.out.println("Dummy entries created. Failed Create Count: "+errorCount+".\n" +
                "Starting the 'Performance Test - Deleting Todos'\n");

        for (int j=1; j<10001; j++){
            startTime = System.currentTimeMillis();
            int response = Delete_Todos(j);

            counter++;



            if (response != 200) {
                errorCount++;
                //Add a duplicate of the previous valid transaction time
                accumulator += transactionTime;
            } else {

                //Sample the transaction timestamp
                transactionTime = System.currentTimeMillis() - startTime;

                accumulator += transactionTime;

                //Sample at each interval of 50 transactions
                if (counter == 50) {
                    //Add the transaction time sample to the CSV file
                    csvWriter.write(10001 - j + "," + (accumulator / 50.0) + "," + (System.currentTimeMillis() - zeroTime) + "\n");
                    counter = 0;
                    accumulator = 0;
                }
            }
        }

        //Check for invalid endpoint
        object = APIInstance.request("GET", "/todos/500");
        boolean afterValidate = (object == null);

        System.out.println("Performance Test - Deleting Todos (Completed)");
        System.out.println("Error Count: "+errorCount+"\n");

        csvWriter.close();

        assertTrue("Too many failed POST or DELETE requests... Error count is too high.",
                errorCount < 10 && beforeValidate != afterValidate);
    }


    @Test
    public void performanceTestChange() throws IOException, InterruptedException {

        errorCount = 0;
        counter = 0;
        accumulator = 0;

        zeroTime = System.currentTimeMillis();

        FileWriter csvWriter = new FileWriter("todos_performance_change.csv");
        csvWriter.write("Total number of todos,Transaction Time,Current Time MS\n");

        for (int i=1; i<10000; i++){
            int code = Create_Todos("Test "+i,"Dummy entry #"+i);

            if (code != 201) {
                errorCount++;
            } else {
                startTime = System.currentTimeMillis();
                int success = Change_Todos(i, "New, Test "+i,"New Dummy entry #"+i);

                counter++;
                //Sample the transaction timestamp
                transactionTime = System.currentTimeMillis() - startTime;

                accumulator += transactionTime;

                //Sample at each interval of 50 transactions
                if (counter == 50) {
                    //Add the transaction time sample to the CSV file
                    csvWriter.write(i + "," + (accumulator/50.0) + "," + (System.currentTimeMillis() - zeroTime) + "\n");
                    counter = 0;
                    accumulator = 0;
                }

                if (success != 200) {
                    errorCount++;
                }
            }
        }
        System.out.println("Performance Test - Changing Todos (Completed)");
        System.out.println("Error Count: "+errorCount+"\n");

        csvWriter.close();

        assertTrue("Too many failed POST requests... Error count is too high.",errorCount < 10);
    }



}
