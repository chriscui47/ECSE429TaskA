package ecse429.group9.restAPI;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TestInteroperability {
    @After
    public void killInstance(){
        APIInstance.killInstance();
    }

    @Before
    public void startInstance() throws IOException {
        APIInstance.runApplication();
        assertEquals(StatusCodes.SC_SUCCESS, APIInstance.getStatusCode(""));
    }

    // 1. GETS

    // 1.1 to do

    @Test
    public void testGetToDoTasksOf() throws IOException {
        String url = "/todos/1/tasksof";
        assertEquals(APIInstance.getStatusCode(url),StatusCodes.SC_SUCCESS);
        JSONObject value = APIInstance.request("GET", url);
        assertEquals(1, value.getJSONArray("projects").length());
        assertEquals("1", value.getJSONArray("projects").getJSONObject(0).getString("id"));
        assertEquals("Office Work", value.getJSONArray("projects").getJSONObject(0).getString("title"));
    }

    @Test
    public void testGetToDoTasksOfInvalid() throws IOException {
        String url = "/todos/1/tasksof/3";
        int value = APIInstance.getStatusCode(url);
        assertEquals(StatusCodes.SC_NOT_FOUND, value);
    }

    @Test
    public void testGetToDoCategory() throws IOException {
        String url = "/todos/1/categories";
        assertEquals(APIInstance.getStatusCode(url),StatusCodes.SC_SUCCESS);
        JSONObject value = APIInstance.request("GET",url);
        assertEquals(1, value.getJSONArray("categories").length());
        assertEquals("1", value.getJSONArray("categories").getJSONObject(0).getString("id"));
        assertEquals("Office", value.getJSONArray("categories").getJSONObject(0).getString("title"));

    }

    @Test
    public void testGetTodoCategoryInvalid() throws IOException {
        String url = "/todos/1/categories/3";
        int value = APIInstance.getStatusCode(url);
        assertEquals(StatusCodes.SC_NOT_FOUND, value);
    }

    // 1.2 project

    @Test
    public void testGetProjectTask() throws IOException {
        String url = "/projects/1/tasks";
        assertEquals(APIInstance.getStatusCode(url), StatusCodes.SC_SUCCESS);
        JSONObject value = APIInstance.request("GET", url);
        assertEquals(2, value.getJSONArray("todos").length());

        assertEquals("file paperwork", value.getJSONArray("todos").getJSONObject(0).getString("title"));
        assertEquals("scan paperwork", value.getJSONArray("todos").getJSONObject(1).getString("title"));

    }

    @Test
    public void testGetProjectTaskInvalid() throws IOException {
        String url = "/projects/1/tasks/3";
        int value = APIInstance.getStatusCode(url);
        assertEquals(StatusCodes.SC_NOT_FOUND, value);
    }

    @Test
    public void testGetProjectCategory() throws IOException {
        String url = "/projects/1/categories";
        JSONObject value = APIInstance.request("GET", url);
        assertEquals(0, value.getJSONArray("categories").length());
    }

    @Test
    public void testGetProjectCategoryInvalid() throws IOException {
        String url = "/todos/1/categories/3";
        int value = APIInstance.getStatusCode(url);
        assertEquals(StatusCodes.SC_NOT_FOUND, value);
    }

    // 1.3 category

    @Test
    public void testGetCategoryToDo() throws IOException {
        String url = "/categories/1/todos";
        assertEquals(APIInstance.getStatusCode(url), StatusCodes.SC_SUCCESS);
        JSONObject value = APIInstance.request("GET", url);
        assertEquals(0, value.getJSONArray("todos").length());
    }

    @Test
    public void testGetCategoryToDoInvalid() throws IOException {
        String url = "/categories/1/todos/1";
        assertEquals(APIInstance.getStatusCode(url), StatusCodes.SC_NOT_FOUND);
    }

    @Test
    public void testGetCategoryProject() throws IOException {
        String url = "/categories/1/projects";
        assertEquals(APIInstance.getStatusCode(url), StatusCodes.SC_SUCCESS);
        JSONObject value = APIInstance.request("GET", url);
        assertEquals(0, value.getJSONArray("projects").length());
    }

    @Test
    public void testGetCategoryProjectInvalid() throws IOException {
        String url = "/categories/1/projects/1";
        assertEquals(APIInstance.getStatusCode(url), StatusCodes.SC_NOT_FOUND);
    }

    // 2. POSTS & DELETE

    // 2.1 to do

    @Test
    public void testPostDeleteToDoTasksOf() throws IOException, InterruptedException {
        JSONObject json = new JSONObject();
        json.put("id", "1");
        APIInstance.post("/todos/1/tasksof", json.toString());

        Thread.sleep(500);

        JSONObject value = APIInstance.request("GET", "/todos/1/tasksof");
        assertEquals(1,value.getJSONArray("projects").length());
        assertEquals("1", value.getJSONArray("projects").getJSONObject(0).getString("id"));
        assertEquals("Office Work", value.getJSONArray("projects").getJSONObject(0).getString("title"));

        APIInstance.request("DELETE", "/todos/1/tasksof/1");

        Thread.sleep(500);

        JSONObject value2 = APIInstance.request("GET", "/todos/1/tasksof");
        assertEquals(0,value2.getJSONArray("projects").length());

        APIInstance.post("/todos/1/tasksof", json.toString());
        // add the deleted endpoint back
    }

    @Test
    public void testPostDeleteToDoCategory() throws IOException, InterruptedException {
        JSONObject json = new JSONObject();
        json.put("id", "2");
        APIInstance.post("/todos/1/categories", json.toString());

        Thread.sleep(500);

        JSONObject value = APIInstance.request("GET", "/todos/1/categories");

        assertEquals(2,value.getJSONArray("categories").length());
        assertEquals("Home", value.getJSONArray("categories").getJSONObject(0).getString("title"));
        assertEquals("Office", value.getJSONArray("categories").getJSONObject(1).getString("title"));


        APIInstance.request("DELETE", "/todos/1/categories/2");

        Thread.sleep(500);

        JSONObject value2 = APIInstance.request("GET", "/todos/1/categories");
        assertEquals(1,value2.getJSONArray("categories").length());
        assertEquals("1", value2.getJSONArray("categories").getJSONObject(0).getString("id"));
        assertEquals("Office", value2.getJSONArray("categories").getJSONObject(0).getString("title"));
    }

    // 2.2 project

    @Test
    public void testPostDeleteProjectTask() throws IOException, InterruptedException {
        JSONObject json = new JSONObject();
        json.put("id", "1");
        APIInstance.post("/projects/1/tasks", json.toString());
        Thread.sleep(500);

        JSONObject value = APIInstance.request("GET", "/projects/1/tasks");
        assertEquals(2, value.getJSONArray("todos").length());

        assertEquals("file paperwork", value.getJSONArray("todos").getJSONObject(0).getString("title"));
        assertEquals("scan paperwork", value.getJSONArray("todos").getJSONObject(1).getString("title"));


        APIInstance.request("DELETE", "/projects/1/tasks/1");
        Thread.sleep(500);

        JSONObject value2 = APIInstance.request("GET", "/projects/1/tasks");
        assertEquals(1, value2.getJSONArray("todos").length());
        assertEquals("2", value2.getJSONArray("todos").getJSONObject(0).getString("id"));
        assertEquals("file paperwork", value2.getJSONArray("todos").getJSONObject(0).getString("title"));

        APIInstance.post("/projects/1/tasks", json.toString());
        // add back
    }

    @Test
    public void testPostDeleteProjectCategory() throws IOException, InterruptedException {
        JSONObject json = new JSONObject();
        json.put("id", "1");
        APIInstance.post("/projects/1/categories", json.toString());
        Thread.sleep(500);

        JSONObject value = APIInstance.request("GET", "/projects/1/categories");
        assertEquals(1, value.getJSONArray("categories").length());
        assertEquals("1", value.getJSONArray("categories").getJSONObject(0).getString("id"));
        assertEquals("Office", value.getJSONArray("categories").getJSONObject(0).getString("title"));

        APIInstance.request("DELETE", "/projects/1/categories/1");
        Thread.sleep(500);

        JSONObject value2 = APIInstance.request("GET", "/projects/1/categories");
        assertEquals(0,value2.getJSONArray("categories").length());
    }

    // 2.3 category

    @Test
    public void testPostDeleteCategoryToDo() throws IOException, InterruptedException {
        JSONObject json = new JSONObject();
        json.put("id", "1");
        APIInstance.post("/categories/1/todos", json.toString());
        Thread.sleep(500);

        JSONObject value = APIInstance.request("GET", "/categories/1/todos");
        assertEquals(1, value.getJSONArray("todos").length());
        assertEquals("1", value.getJSONArray("todos").getJSONObject(0).getString("id"));
        assertEquals("scan paperwork", value.getJSONArray("todos").getJSONObject(0).getString("title"));

        APIInstance.request("DELETE", "/categories/1/todos/1");

        Thread.sleep(500);

        JSONObject value2 = APIInstance.request("GET", "/categories/1/todos");
        assertEquals(0, value2.getJSONArray("todos").length());
    }

    @Test
    public void testPostDeleteCategoryProject() throws IOException, InterruptedException {
        JSONObject json = new JSONObject();
        json.put("id", "1");
        APIInstance.post("/categories/1/projects", json.toString());
        Thread.sleep(500);

        JSONObject value = APIInstance.request("GET", "/categories/1/projects");
        assertEquals(1, value.getJSONArray("projects").length());
        assertEquals("1", value.getJSONArray("projects").getJSONObject(0).getString("id"));
        assertEquals("Office Work", value.getJSONArray("projects").getJSONObject(0).getString("title"));

        APIInstance.request("DELETE", "/categories/1/projects/1");

        Thread.sleep(500);

        JSONObject value2 = APIInstance.request("GET", "/categories/1/projects");
        assertEquals(0,value2.getJSONArray("projects").length());
    }

    // 3. DELETE Invalid

    // 3.1 to do

    @Test
    public void testDeleteToDoTasksOfInvalid() throws IOException {
        String url = "/todos/1/tasksof/3";
        int value = APIInstance.getStatusCode("DELETE", url);
        assertEquals(StatusCodes.SC_NOT_FOUND, value);
    }

    @Test
    public void testDeleteToDoCategoryInvalid() throws IOException {
        String url = "/todos/1/categories/3";
        int value = APIInstance.getStatusCode("DELETE",url);
        assertEquals(StatusCodes.SC_NOT_FOUND, value);
    }

    // 3.2 project

    @Test
    public void testDeleteProjectTaskInvalid() throws IOException {
        String url = "/projects/1/tasks/3";
        int value = APIInstance.getStatusCode("DELETE",url);
        assertEquals(StatusCodes.SC_NOT_FOUND, value);
    }

    @Test
    public void testDeleteProjectCategoryInvalid() throws IOException {
        String url = "/projects/1/categories/1";
        int value = APIInstance.getStatusCode("DELETE",url);
        assertEquals(StatusCodes.SC_NOT_FOUND, value);
    }

    // 3.3 category

    @Test
    public void testDeleteCategoryToDoInvalid() throws IOException {
        String url = "/categories/1/todos/3";
        int value = APIInstance.getStatusCode("DELETE", url);
        assertEquals(StatusCodes.SC_NOT_FOUND, value);
    }

    @Test
    public void testDeleteCategoryProjectInvalid() throws IOException {
        String url = "/categories/1/projects/1";
        int value = APIInstance.getStatusCode("DELETE", url);
        assertEquals(StatusCodes.SC_NOT_FOUND, value);
    }

    // 4. HEAD

    // 4.1 to do

    @Test
    public void testHeadTodoTasksOf() throws IOException{
        String url = "/todos/1/tasksof";
        assertEquals("application/json", APIInstance.getHeadContentType(url));
    }

    @Test
    public void testHeadTodoCategory() throws IOException{
        String url = "/todos/1/categories";
        assertEquals("application/json", APIInstance.getHeadContentType(url));
    }

    // 4.2 project

    @Test
    public void testHeadProjectTask() throws IOException {
        String url = "/projects/1/tasks";
        assertEquals("application/json", APIInstance.getHeadContentType(url));
    }

    @Test
    public void testHeadProjectCategory() throws IOException {
        String url = "/projects/1/categories";
        assertEquals("application/json", APIInstance.getHeadContentType(url));
    }

    // 4.3 category

    @Test
    public void testHeadCategoryToDo() throws IOException {
        String url = "/categories/1/todos";
        assertEquals("application/json", APIInstance.getHeadContentType(url));
    }

    @Test
    public void testHeadCategoryProject() throws IOException {
        String url = "/categories/1/projects";
        assertEquals("application/json", APIInstance.getHeadContentType(url));
    }
}
