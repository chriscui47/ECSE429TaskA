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

import static org.junit.Assert.assertEquals;

public class TestProjectsPerformance {

@Before
    public void killInstance() {
        APIInstance.killInstance();
    }
@After
    public void startInstance()  throws IOException{
        APIInstance.runApplication();
        assertEquals(StatusCodes.SC_SUCCESS, APIInstance.getStatusCode(""));
    }

    //POST Projects
    public static void Create_Valid_Projects(String title, String description) throws IOException, InterruptedException {
        String validID = "/projects";

        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("description", description);
        APIInstance.post(validID,json.toString());
        Thread.sleep(500);

        JSONObject response = APIInstance.request("GET", "/projects");
        System.out.println(response);
    }


    public void Update_Description(String description, JSONObject jsonOriginal) throws IOException, InterruptedException {
        String validID = "/projects/1";

        JSONObject jsonNew = new JSONObject();
        jsonNew.put("description", description);
        APIInstance.post(validID,jsonNew.toString());
        Thread.sleep(500);

        JSONObject response = APIInstance.request("GET", "/projects/1");
        assertEquals("testdescription",response.getJSONArray("projects").getJSONObject(0).get("description"));
    }

    //DELETE Projects/id
    public void Delete_Valid_Projects() throws IOException {
        String valid_id = "/projects/1";
        assertEquals(APIInstance.getStatusCode(valid_id), StatusCodes.SC_SUCCESS);
        APIInstance.request("DELETE",valid_id);
        assertEquals(APIInstance.getStatusCode(valid_id), StatusCodes.SC_NOT_FOUND);
    }

@Test
    public void startTest() throws IOException, InterruptedException {

    for (int i=0; i<100; i++){
        Create_Valid_Projects("test","yolo");
    }

    }
}
