package ecse429.group9.restAPI.PartC;

import ecse429.group9.restAPI.APIInstance;
import ecse429.group9.restAPI.StatusCodes;
import org.json.JSONException;
import org.json.JSONObject;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestProjectsPerformance {

    @After
    public void killInstance() {
        APIInstance.killInstance();
    }

    @Before
    public void startInstance() throws IOException {
        APIInstance.runApplication();
        assertEquals(StatusCodes.SC_SUCCESS, APIInstance.getStatusCode(""));
    }
    public static void addMultipleProjects(String title, String description) throws IOException, InterruptedException {
        JSONObject response = APIInstance.request("GET", "/categories");
        //int currentSize = response.getJSONArray("categories").length();

        String option = "/projects";
        int i=0;
        while (i<1000) {
            JSONObject json = new JSONObject();
            json.put("title", title);
            json.put("description", description);
            APIInstance.post(option, json.toString());
            i++;
        }

    }

    //POST Projects
    public static long Create_Valid_Projects(String title, String description) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        long endTime;


        String validID = "/projects";

        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("description", description);
        int code = APIInstance.post2(validID, json.toString());

        endTime = System.currentTimeMillis() - startTime;
        return endTime;
    }


    public long Update_Description(String description) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        long endTime;

        String validID = "/projects/1";
        JSONObject jsonNew = new JSONObject();
        jsonNew.put("description", description);
        int code = APIInstance.post2(validID, jsonNew.toString());

        endTime = System.currentTimeMillis() - startTime -500;

        return endTime;
    }

    //DELETE Projects/id
    public long Delete_Valid_Projects(int id) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        long endTime;

        String valid_id = "/projects/"+ id;
        APIInstance.request("DELETE", valid_id);

        endTime = System.currentTimeMillis() - startTime ;

        return endTime;
    }

    @Test
    public void performanceTestCreate() throws IOException, InterruptedException {

        int errorCount = 0;
        int counter = 0;
        int accumulator = 0;

        long zeroTime = System.currentTimeMillis();


        FileWriter csvWriter = new FileWriter("projects_performance_create.csv");
        csvWriter.write("Total number of projects,Transaction Time,Current Time MS\n");

        for (int i=1; i<10000; i++) {
            long startTime = System.currentTimeMillis();

            long code = Create_Valid_Projects("Test ","Tester");

            counter++;
            //Sample the transaction timestamp
            long transactionTime = System.currentTimeMillis() - startTime;

            accumulator += transactionTime;

            //Sample at each interval of 50 transactions
            if (counter == 50) {
                //Add the transaction time sample to the CSV file


                JSONObject response = APIInstance.request("GET", "/projects");
                csvWriter.write(response.getJSONArray("projects").length() + "," + (accumulator/50.0) + "," + (System.currentTimeMillis() - zeroTime) + "\n");
                counter = 0;
                accumulator = 0;
            }

        }
        System.out.println("Performance Test - Creating projects (Completed)");
        System.out.println("Error Count: "+errorCount+"\n");

        csvWriter.close();
    System.out.println(errorCount);
        assertTrue("Too many failed POST requests... Error count is too high.",errorCount < 10);
    }


@Test
    public void performanceTestDelete() throws IOException, InterruptedException {

        int errorCount = 0;
        int counter = 0;
        int accumulator = 0;

        long zeroTime = System.currentTimeMillis();

        FileWriter csvWriter = new FileWriter("projects _performance_delete.csv");
        csvWriter.write("Total number of projects,Transaction Time,Current Time MS\n");

        for (int i = 1; i < 10001; i++) {
            long response = Create_Valid_Projects("Test ", "tester");

            if (response != 201) {
                errorCount++;
            }
        }


        System.out.println("Dummy entries created. Failed Create Count: " + errorCount + ".\n" +
                "Starting the 'Performance Test - Deleting Todos'\n");

        for (int j = 1; j < 50; j++) {
            long startTime = System.currentTimeMillis();
            long response = Delete_Valid_Projects(j);
            JSONObject response2 = APIInstance.request("GET", "/projects");
            counter++;
            //Sample the transaction timestamp
            long transactionTime = System.currentTimeMillis() - startTime;

            accumulator += transactionTime;

            //Sample at each interval of 50 transactions
                //Add the transaction time sample to the CSV file
                csvWriter.write(response2.getJSONArray("projects").length()  + "," + (accumulator / 50.0) + "," + (System.currentTimeMillis() - zeroTime) + "\n");
                counter = 0;
                accumulator = 0;

        }
        csvWriter.close();

}



    @Test
    public void performanceTestChange() throws IOException, InterruptedException {

        int errorCount = 0;
        int counter = 0;
        int accumulator = 0;

        long zeroTime = System.currentTimeMillis();

        FileWriter csvWriter = new FileWriter("projects_performance_change.csv");
        csvWriter.write("Total number of todos,Transaction Time,Current Time MS\n");

        for (int i=1; i<10000; i++){
            long code = Create_Valid_Projects("Test", "tester");

                long startTime = System.currentTimeMillis();
                long success = Update_Description("yolo");

                counter++;
                //Sample the transaction timestamp
                long transactionTime = System.currentTimeMillis() - startTime;

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

        csvWriter.close();

    }



}
