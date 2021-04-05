package ecse429.group9.restAPI.PartC;

import ecse429.group9.restAPI.APIInstance;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestInterPerformance {

    public static long startTime;
    public static long transactionTime = 0;

    @After
    public void killInstance() {
        APIInstance.killInstance();
    }
    @Before
    public void startInstance()  throws IOException{
        APIInstance.runApplication();
    }

    public static void Create_Init(String title, String description) throws IOException, InterruptedException {
        JSONObject newTodo = new JSONObject();
        newTodo.put("title", title);
        newTodo.put("description", description);
        newTodo.put("doneStatus", false);

        APIInstance.post2("/todos", newTodo.toString());
    }

    public static void Delete(int id) throws IOException {
        String endpoint = "/projects/1/tasks/" + id;
        APIInstance.request("DELETE", endpoint);
    }

    public static void Create(int id) throws IOException {
        JSONObject json = new JSONObject();
        json.put("id", String.valueOf(id));
        APIInstance.post("/projects/1/tasks", json.toString());
    }

    @Test
    public void performanceTestCreate() throws IOException, InterruptedException {

        FileWriter csvWriter = new FileWriter("inter_performance_create.csv");
        csvWriter.write("Total number, Transaction Time, Current Time MS\n");

        for (int i = 1; i < 1001; i++){
            Create_Init("Test " + i + "","" + "Entry # " + i + "");
        }

        for (int i = 1; i < 1001; i++) {
            startTime = System.currentTimeMillis();
            Create(i);
            transactionTime = System.currentTimeMillis() - startTime;
            csvWriter.write(i + ", " + transactionTime + ", " + System.currentTimeMillis() + "\n");
        }

        System.out.println("Performance Test - Inter Create Completed");

        csvWriter.close();
    }


    @Test
    public void performanceTestDelete() throws IOException, InterruptedException {

        FileWriter csvWriter = new FileWriter("inter_performance_delete.csv");
        csvWriter.write("Total number, Transaction Time, Current Time MS\n");

        for (int i = 1; i < 1001; i++){
            Create_Init("Test " + i + "","Entry #" + i + "");
            Create(i);
        }

        Thread.sleep(500);

        for (int i = 1; i < 1001; i++){
            startTime = System.currentTimeMillis();
            Delete(i);
            transactionTime = System.currentTimeMillis() - startTime;
            csvWriter.write(i + ", " + transactionTime + ", " + System.currentTimeMillis() + "\n");
        }

        System.out.println("Performance Test - Inter Delete Completed");

        csvWriter.close();
    }

}