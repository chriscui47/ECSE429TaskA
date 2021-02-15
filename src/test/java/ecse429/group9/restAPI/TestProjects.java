package ecse429.group9.restAPI;

import static org.junit.Assert.assertEquals;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
public class TestProjects {


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

    @Test
    public void Get_Proj_Completed_Title() throws IOException {
        String project_url = "/projects";
        JSONObject response = APIInstance.request("GET", project_url);
        String result = response.getJSONArray("projects").getJSONObject(0).getString("title");
        assertEquals("Office Work", result);

        result = response.getJSONArray("projects").getJSONObject(0).getString("completed");
        assertEquals("false", result);

    }

    //GET projects/id
    @Test
    public void Get_Specific_Proj_WithID() throws IOException {
        JSONObject response = APIInstance.request("GET", "/projects/1");
        String expected = "1";
        String result = response.getJSONArray("projects").getJSONObject(0).getString("id");
        assertEquals(result, expected);
    }

    @Test
    public void Get_Invalid_ID() throws IOException {
        String invalid_request = "/projects/3";
        assertEquals(APIInstance.getStatusCode(invalid_request), StatusCodes.SC_NOT_FOUND);
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
    public void Create_With_Only_Title() throws IOException, InterruptedException {
        String validID = "/projects";
        String title = "Title";

        JSONObject json = new JSONObject();
        json.put("title", title);
        APIInstance.post(validID,json.toString());
        Thread.sleep(500);

        JSONObject response = APIInstance.request("GET", "/projects");
        System.out.println(response);
        assertEquals(2,response.getJSONArray("projects").length());
    }

    @Test
    public void Create_With_Existing_Title() throws IOException, InterruptedException {
        String validID = "/projects";
        String title = "letsgooo";

        JSONObject json = new JSONObject();
        json.put("title", title);
        APIInstance.post(validID,json.toString());
        Thread.sleep(500);

        JSONObject response = APIInstance.request("GET", "/projects");
        assertEquals(2,response.getJSONArray("projects").length());
    }

    @Test
    public void Create_Without_Title() throws IOException, InterruptedException {
        JSONObject json = new JSONObject();
        json.put("description", "DescriptionTest");
        APIInstance.post("/projects", json.toString());
        Thread.sleep(500);

        JSONObject response = APIInstance.request("GET", "/projects");
        assertEquals(2,response.getJSONArray("projects").length());
    }

    //POST projects/id

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

    //PUT projects/id


    @Test
    public void Override_Completed() throws IOException, InterruptedException {
        String validID = "/projects/1";
        String title = "NEWTITLE";
        boolean completed = true;

        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("completed", completed);
        APIInstance.put(validID,json.toString());
        Thread.sleep(500);

        JSONObject response = APIInstance.request("GET", "/projects/1");
        assertEquals(title,response.getJSONArray("projects").getJSONObject(0).get("title"));
        assertEquals("true",response.getJSONArray("projects").getJSONObject(0).get("completed"));
        boolean NotExist = false;
        try{
            response.getJSONArray("projects").getJSONObject(0).get("tasks");
        } catch(JSONException e) {
            NotExist = true;
        }
        assertEquals(true,NotExist);
    }

    //DELETE Projects/id
    @Test
    public void Delete_Valid_Projects() throws IOException {
        String valid_id = "/projects/1";
        assertEquals(APIInstance.getStatusCode(valid_id), StatusCodes.SC_SUCCESS);
        APIInstance.request("DELETE",valid_id);
        assertEquals(APIInstance.getStatusCode(valid_id), StatusCodes.SC_NOT_FOUND);

    }


    //HEAD Projects
    @Test
    public void Head_Projects() throws IOException {
        String proj = "/projects";
        String head = APIInstance.getHeadContentType(proj);
        assertEquals("application/json", head);
    }

    @Test
    public void Head_Projects_ID() throws IOException {
        String proj = "/projects/1";
        String head = APIInstance.getHeadContentType(proj);
        assertEquals("application/json", head);
    }

}
