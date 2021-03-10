package ecse429.group9.restAPI;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import java.net.URL;

public class APIInstance {

    private static final String URL = "http://localhost:4567";

    public static Process process;

    public static void runApplication(){
        ProcessBuilder pb = new ProcessBuilder("java", "-jar", "./runTodoManagerRestAPI-1.5.5.jar");
        try {
            process = pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void killInstance(){
        process.destroy();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static JSONObject send(String type, String option) throws IOException {
        URL url = new URL(URL + option);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(type);
        connection.setRequestProperty("Accept", "application/json");
        System.out.println(connection.getContentType());
        System.out.println(connection.getResponseCode());
        if (connection.getResponseCode() != 200) {
            return null;
        }
        BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
        String response = br.readLine();
        if (response == null) {
            return null;
        }

        JSONObject json = new JSONObject(response);
        connection.disconnect();
        return json;
    }

    public static JSONObject request(String type, String option) throws IOException {
        URL url = new URL(URL + option);
        HttpURLConnection connec = (HttpURLConnection) url.openConnection();
        connec.setRequestMethod(type);
        connec.setRequestProperty("Accept", "application/json");

        if (connec.getResponseCode() != 200) {
            return null;
        }
        BufferedReader br = new BufferedReader(new InputStreamReader((connec.getInputStream())));
        String response = br.readLine();
        if (response == null) {
            return null;
        }

        JSONObject json = new JSONObject(response);
        connec.disconnect();
        return json;
    }

    public static boolean post(String option, String JSONInputString) throws IOException {
        URL url = new URL(URL + option);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
        byte[] input = JSONInputString.getBytes("utf-8");
        connection.setFixedLengthStreamingMode(input.length);
        connection.connect();
        try(OutputStream os = connection.getOutputStream()) {
            os.write(input,0,input.length);
        }

        return true;
    }

    public static int post2(String option, String JSONInputString) throws IOException {
        URL url = new URL(URL + option);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
        byte[] input = JSONInputString.getBytes("utf-8");
        connection.setFixedLengthStreamingMode(input.length);
        connection.connect();
        try(OutputStream os = connection.getOutputStream()) {
            os.write(input,0,input.length);
        }

        return connection.getResponseCode();
    }



    public static boolean put(String option, String JSONInputString) throws IOException {
        URL url = new URL(URL + option);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
        byte[] input = JSONInputString.getBytes("utf-8");
        connection.setFixedLengthStreamingMode(input.length);
        connection.connect();
        try(OutputStream os = connection.getOutputStream()) {
            os.write(input,0,input.length);
        }

        return true;
    }

    public static String getHeadContentType(String option) throws IOException {
        URL url = new URL(URL + option);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("HEAD");
        connection.setRequestProperty("Accept", "application/json");
        return (connection.getContentType());
    }

    public static int getStatusCode(String type, String option) throws IOException {
        URL url = new URL(URL + option);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(type);
        connection.setRequestProperty("Accept", "application/json");
        return connection.getResponseCode();
    }

    public static int getStatusCode(String option) throws IOException {
        URL url = new URL(URL + option);
        HttpURLConnection http;
        http = (HttpURLConnection)url.openConnection();
        return http.getResponseCode();
    }

}
