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

        endTime = System.currentTimeMillis() - startTime ;

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
    public void startTest() throws IOException, InterruptedException {
        long[] timesforCreate = new long[1000];
        FileWriter addCSVWriter = new FileWriter("projectsCreate_performance.csv");
        addCSVWriter.write("Total number of projects,Transaction Time,Current Time MS\n");


        for (int i = 2; i < 10000; i++) {
            long currentTime = System.currentTimeMillis();
            long transaction =  Create_Valid_Projects("test", "yolo");
            addCSVWriter.write( i + "," + transaction + "," + currentTime + "\n");
        }


        addCSVWriter.close();
    }

    @Test
    public void deleteTest() throws IOException, InterruptedException {
        FileWriter addCSVWriter = new FileWriter("projectsDelete_performance.csv");
        addCSVWriter.write("Total number of projects,Transaction Time,Current Time MS\n");


        addMultipleProjects("test","test");
        for (int i = 1; i < 10000; i++) {
            long currentTime = System.currentTimeMillis();
            long transaction =  Delete_Valid_Projects(i);

            addCSVWriter.write((1000-i) + "," + transaction + "," + currentTime + "\n");
        }

        addCSVWriter.close();

    }




    @Test
    public void updateDescriptionTest() throws IOException, InterruptedException {
        FileWriter addCSVWriter = new FileWriter("projectsUpdate_performance.csv");
        addCSVWriter.write("Total number of projects,Transaction Time,Current Time MS\n");

        //create project w id 1

        Create_Valid_Projects("test", "yolo");

        for (int i = 0; i < 10000; i++) {
            long currentTime = System.currentTimeMillis();
            long transaction =  Update_Description("testt");
            addCSVWriter.write(i + "," + transaction + "," + currentTime + "\n");
        }
        addCSVWriter.close();

    }

}
