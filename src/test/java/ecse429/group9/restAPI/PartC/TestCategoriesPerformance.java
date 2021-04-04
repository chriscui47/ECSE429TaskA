package ecse429.group9.restAPI.PartC;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;

import ecse429.group9.restAPI.APIInstance;
import ecse429.group9.restAPI.StatusCodes;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TestCategoriesPerformance {
    @After
    public void killInstance(){
        APIInstance.killInstance();
    }

    @Before
    public void startInstance() throws IOException {
        APIInstance.runApplication();
        assertEquals(StatusCodes.SC_SUCCESS, APIInstance.getStatusCode(""));
    }

    public static long addIndividualCategory(String title, String description) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();

        String option = "/categories";

        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("description", description);
        int responseCode = APIInstance.post2(option, json.toString());

        return System.currentTimeMillis() - startTime;
    }

    public static void addMultipleCategories(String title, String description, int targetSize) throws IOException, InterruptedException {
        JSONObject response = APIInstance.request("GET", "/categories");
        int currentSize =  response.getJSONArray("categories").length();

        String option = "/categories";

        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("description", description);

        int responseCode = APIInstance.post2(option, json.toString());
    }

    public static long changeCategory(String title, String description, int id) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();

        String option = "/categories/" + id;

        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("description", description);
        int responseCode = APIInstance.post2(option, json.toString());

        return System.currentTimeMillis() - startTime;
    }

    public static long deleteCategory(int id) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();

        String option = "/categories/" + id;

        int responseCode = APIInstance.getStatusCode("DELETE", option);

        return System.currentTimeMillis() - startTime;
    }

    @Test
    public void addTest() throws IOException, InterruptedException {
        FileWriter csvWriter = new FileWriter("categories_performance_add.csv");
        csvWriter.write("Total number of categories,Transaction Time,Current Time MS\n");

        long transactionTime = 0;

        for (int i = 2; i < 1000; i++) {
            transactionTime = addIndividualCategory("sample title", "sample description");
            csvWriter.write(i + "," + transactionTime + "," + System.currentTimeMillis() + "\n");
        }

        csvWriter.close();
    }

    @Test
    public void changeTest() throws IOException, InterruptedException {
        FileWriter csvWriter = new FileWriter("categories_performance_change.csv");
        csvWriter.write("Total number of categories,Transaction Time,Current Time MS\n");

        long transactionTime = 0;

        for (int i = 2; i < 1000; i++) {
            addIndividualCategory("sample title", "sample description");

            transactionTime = changeCategory("new title", "new description", 1);
            csvWriter.write(i + "," + transactionTime + "," + System.currentTimeMillis() + "\n");
        }

        csvWriter.close();
    }

    @Test
    public void deleteTest() throws IOException, InterruptedException {
        FileWriter csvWriter = new FileWriter("categories_performance_delete.csv");
        csvWriter.write("Total number of categories,Transaction Time,Current Time MS\n");

        long transactionTime = 0;

        for (int i = 2; i < 1000; i++) {
            addMultipleCategories("sample title", "sample description", i+1);
            transactionTime = deleteCategory(i-1);

            csvWriter.write(i + "," + transactionTime + "," + System.currentTimeMillis() + "\n");
        }

        csvWriter.close();
    }

}
