package ecse429.group9.restAPI.PartC;

import ecse429.group9.restAPI.APIInstance;
import ecse429.group9.restAPI.StatusCodes;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TestProjectsPerformance {


    @After
    public void killInstance() {
        APIInstance.killInstance();
    }

    @Before
    public void startInstance()  throws IOException{
        APIInstance.runApplication();
        assertEquals(StatusCodes.SC_SUCCESS, APIInstance.getStatusCode(""));
    }


    //GET Projects
    @Test
    public void Get_Proj_Status() throws IOException {
        String project_url = "/projects";
        assertEquals(StatusCodes.SC_SUCCESS, APIInstance.getStatusCode(project_url));
    }

    //POST Projects
    @Test
    public void Create_Valid_Projects() throws IOException, InterruptedException {
        String validID = "/projects";
        String title = "TitleTest";
        String description = "DescriptionTest";

        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("description", description);
        APIInstance.post(validID,json.toString());
        Thread.sleep(500);

        JSONObject response = APIInstance.request("GET", "/projects");
        System.out.println(response);
        assertEquals(2,response.getJSONArray("projects").length());
    }


    @Test
    public void Update_Description() throws IOException, InterruptedException {
        String validID = "/projects/1";
        String description = "testdescription";

        JSONObject json = new JSONObject();
        json.put("description", description);
        APIInstance.post(validID,json.toString());
        Thread.sleep(500);

        JSONObject response = APIInstance.request("GET", "/projects/1");
        assertEquals("testdescription",response.getJSONArray("projects").getJSONObject(0).get("description"));
    }

    @Test
    public void Update_Completion() throws IOException, InterruptedException {
        String validID = "/projects/1";
        boolean completed = true;

        JSONObject json = new JSONObject();
        json.put("completed", completed);
        APIInstance.post(validID,json.toString());
        Thread.sleep(500);

        JSONObject response = APIInstance.request("GET", "/projects/1");
        assertEquals("true",response.getJSONArray("projects").getJSONObject(0).get("completed"));
    }

    //DELETE Projects/id
    @Test
    public void Delete_Valid_Projects() throws IOException {
        String valid_id = "/projects/1";
        assertEquals(APIInstance.getStatusCode(valid_id), StatusCodes.SC_SUCCESS);
        APIInstance.request("DELETE",valid_id);
        assertEquals(APIInstance.getStatusCode(valid_id), StatusCodes.SC_NOT_FOUND);

    }

}
