package ecse429.group9.restAPI.JUnitTests;

import ecse429.group9.restAPI.APIInstance;
import ecse429.group9.restAPI.StatusCodes;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TestCategories {
    @After
    public void killInstance(){
        APIInstance.killInstance();
    }

    @Before
    public void startInstance() throws IOException {
        APIInstance.runApplication();
        assertEquals(StatusCodes.SC_SUCCESS, APIInstance.getStatusCode(""));
    }


    // /categories endpoint

    // GET
    @Test
    public void testGetCategoriesLength() throws IOException {
        JSONObject response = APIInstance.request("GET", "/categories");
        assertEquals(2, response.getJSONArray("categories").length());
    }

    // HEAD
    @Test
    public void testHeadCategories() throws IOException {
        String option = "/categories";
        String head = APIInstance.getHeadContentType(option);
        assertEquals("application/json", head);
    }

    // POST
    // observation:
    // post actually increments the new category's id by 1, however, even after you delete the id the next object u call post on with /categories endpoint will have id incremented by 1 from before
    // for example one calls post to have an category with id 3, then calls delete with id 3, then calls post, the new object will have an id of 4
    @Test
    public void testPostValidCategories() throws IOException, InterruptedException {
        String option = "/categories";
        String title = "School";
        String description = "School related description";

        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("description", description);
        APIInstance.post(option, json.toString());
        Thread.sleep(500);

        JSONObject response = APIInstance.request("GET", "/categories");
        assertEquals(3,response.getJSONArray("categories").length());

        // delete the added category (delete 3 and 4 because we do not know the index of the added category, we add another category in a different unit test)
        APIInstance.request("DELETE", "/categories/3");
        APIInstance.request("DELETE", "/categories/4");

        Thread.sleep(500);

    }

    // case where id is also in json, which it should not be
    @Test
    public void testPostInvalidCategories() throws IOException, InterruptedException {
        String option = "/categories";
        String id = "3";
        String title = "School";
        String description = "School related description";

        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("title", title);
        json.put("description", description);

        APIInstance.post(option, json.toString());
        Thread.sleep(500);

        JSONObject response = APIInstance.request("GET", "/categories");
        assertEquals(2,response.getJSONArray("categories").length());
    }

    // /categories/:id endpoint

    //GET
    @Test
    public void testGetCategoryWithID() throws IOException {
        JSONObject response = APIInstance.request("GET", "/categories/1");
        String expectedID = "1";
        String resultID = response.getJSONArray("categories").getJSONObject(0).getString("id");
        assertEquals(resultID, expectedID);
    }

    @Test
    public void testGetCategoriesWithInvalidID() throws IOException {
        String invalid_request = "/categories/5";
        assertEquals(APIInstance.getStatusCode(invalid_request), StatusCodes.SC_NOT_FOUND);
    }

    //HEAD
    @Test
    public void testHeadCategoriesWithID() throws IOException {
        String option = "/categories/1";
        String head = APIInstance.getHeadContentType(option);
        assertEquals("application/json", head);
    }

    //POST
    // discovery: cannot post to an id not existing already
    // change title
    @Test
    public void testPostCategoriesWithIDTitle() throws IOException, InterruptedException {
        String option = "/categories/2";
        String title = "School";

        JSONObject json = new JSONObject();
        json.put("title", title);

        APIInstance.post(option, json.toString());
        Thread.sleep(500);

        JSONObject response = APIInstance.request("GET", "/categories/2");
        assertEquals(title, response.getJSONArray("categories").getJSONObject(0).get("title"));

        // reset the title of category at 2 back to the original
        title = "Home";

        json = new JSONObject();
        json.put("title", title);

        APIInstance.post(option, json.toString());
    }

    // change description
    @Test
    public void testPostCategoriesWithIDDescription() throws IOException, InterruptedException {
        String option = "/categories/2";
        String description = "School related description";

        JSONObject json = new JSONObject();
        json.put("description", description);

        APIInstance.post(option, json.toString());
        Thread.sleep(500);

        JSONObject response = APIInstance.request("GET", "/categories/2");
        assertEquals(description, response.getJSONArray("categories").getJSONObject(0).get("description"));

        // reset the description category at 2 back to the original
        description = "";

        json = new JSONObject();
        json.put("description", description);

        APIInstance.post(option, json.toString());
    }

    // try to post to null id
    @Test
    public void testPostCategoriesWithNullID() throws IOException, InterruptedException {
        String option = "/categories/5";
        String title = "School";
        String description = "School related description";

        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("description", description);

        APIInstance.post(option, json.toString());
        Thread.sleep(500);

        JSONObject response = APIInstance.request("GET", "/categories");
        assertEquals(2,response.getJSONArray("categories").length());
    }

    //PUT
    // change title
    @Test
    public void testPutCategoriesWithIDTitle() throws IOException, InterruptedException {
        String option = "/categories/2";
        String title = "School";

        JSONObject json = new JSONObject();
        json.put("title", title);

        APIInstance.put(option, json.toString());
        Thread.sleep(500);

        JSONObject response = APIInstance.request("GET", "/categories/2");
        assertEquals(title, response.getJSONArray("categories").getJSONObject(0).get("title"));

        // reset the title of category at 2 back to the original
        title = "Home";

        json = new JSONObject();
        json.put("title", title);

        APIInstance.put(option, json.toString());
    }

    // change description
    // finding: for the PUT method type, you must have a title in the json body
    @Test
    public void testPutCategoriesWithIDDescription() throws IOException, InterruptedException {
        String option = "/categories/2";
        String title = "Home";
        String description = "School related description";

        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("description", description);

        APIInstance.put(option, json.toString());
        Thread.sleep(500);

        JSONObject response = APIInstance.request("GET", "/categories/2");
        assertEquals(description, response.getJSONArray("categories").getJSONObject(0).get("description"));

        // reset the description category at 2 back to the original
        description = "";

        json = new JSONObject();
        json.put("title", title);
        json.put("description", description);

        APIInstance.put(option, json.toString());
    }

    // DELETE
    // add a new category then delete it
    @Test
    public void testDeleteCategoriesValid() throws IOException, InterruptedException {
        String option = "/categories";
        String title = "School";
        String description = "School related description";

        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("description", description);
        APIInstance.post(option, json.toString());
        Thread.sleep(500);

        JSONObject response = APIInstance.request("GET", "/categories");
        assertEquals(3, response.getJSONArray("categories").length());

        // delete the added category (delete 3 and 4 because we do not know the index of the added category, we add another category in a different unit test)
        APIInstance.request("DELETE", "/categories/3");
        APIInstance.request("DELETE", "/categories/4");

        Thread.sleep(500);
        response = APIInstance.request("GET", "/categories");

        assertEquals(2, response.getJSONArray("categories").length());
    }

    @Test
    public void testDeleteCategoriesInvalid() throws IOException {
        String option = "/categories/5";

        JSONObject result = APIInstance.request("DELETE", option);
        assertEquals(null, result);
    }
}
