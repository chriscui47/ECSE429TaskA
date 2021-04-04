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

    //POST Projects
    public static long Create_Valid_Projects(String title, String description) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        long endTime;


        String validID = "/projects";

        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("description", description);
        APIInstance.post(validID, json.toString());
        Thread.sleep(500);

        JSONObject response = APIInstance.request("GET", "/projects");
        System.out.println(response);

        endTime = System.currentTimeMillis() - startTime - 500;
        return endTime;
    }


    public long Update_Description(String description) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        long endTime;

        String validID = "/projects/1";
        JSONObject jsonNew = new JSONObject();
        jsonNew.put("description", description);
        APIInstance.post(validID, jsonNew.toString());
        Thread.sleep(500);

        endTime = System.currentTimeMillis() - startTime - 500;

        return endTime;
    }

    //DELETE Projects/id
    public long Delete_Valid_Projects(int id) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        long endTime;

        String valid_id = "/projects/"+ id;
        APIInstance.request("DELETE", valid_id);
        Thread.sleep(500);

        endTime = System.currentTimeMillis() - startTime - 500;

        return endTime;
    }

    @Test
    public void startTest() throws IOException, InterruptedException {
        long[] timesforCreate = new long[1000];
        int[] ids = new int[1000];
        for (int i = 0; i < 10; i++) {
            timesforCreate[i] = Create_Valid_Projects("test", "yolo");
        }


        System.out.println(Arrays.toString(timesforCreate));

        long[] timesForDelete = new long[1000];
        for (int i = 10; i >= 0; i--) {
            timesforCreate[i] = Delete_Valid_Projects(i);
        }

    }

    @Test
    public void startUpdateDescription() throws IOException, InterruptedException {
        long[] timesforUpdate = new long[1000];
        //create project w id 1
        Create_Valid_Projects("test", "yolo");
        for (int i = 0; i < 300; i++) {
            timesforUpdate[i] = Update_Description("testtttttttt");
        }
        System.out.println(Arrays.toString(timesforUpdate));

    }

}
