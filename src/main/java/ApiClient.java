import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;

public class ApiClient {
    private String apiAdress;
    HttpURLConnection connection;

    public ApiClient(String apiAdress) {
        this.apiAdress = apiAdress;
    }

    public ArrayList<String> getStringArray(String target) {
        JsonObject countryObj = new JsonObject();

        ArrayList<String> myArrayOfStrings = new ArrayList<>();

        return myArrayOfStrings;
    }
    public boolean deleteBlogPost(int id){
        String target = "/delete/" + id;

        boolean success = false;

        try{
            URL url = new URL(apiAdress + target);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");

            int status = connection.getResponseCode();

            if(status >= 300){
                success = false;
            } else {
                success = true;
            }
        } catch (Exception e ){
            System.out.println("Exception: " + e);
        } finally {
            connection.disconnect();
        }
        return success;
    }
    public BlogPost[] listBlogPosts() {
        BlogPost[] blogPosts = {};

        String target = "/list";

        BufferedReader reader;
        String line;
        StringBuilder responseContent = new StringBuilder();

        try {
            URL url = new URL(apiAdress + target);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("accept", "application/json");

            int status = connection.getResponseCode();

            if (status >= 300) {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            }
            String jsonStr = responseContent.toString();

            ObjectMapper mapper = new ObjectMapper();
            blogPosts = mapper.readValue(jsonStr, BlogPost[].class);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            connection.disconnect();
        }
        return blogPosts;
    }
    public boolean createBlogPost(BlogPost newBlogPost) {
        String target = "/create";

        boolean success = false;

        try {
            URL url = new URL(apiAdress + target);

            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");

            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()){
                byte[] input = newBlogPost.toJson().getBytes(StandardCharsets.UTF_8);

                os.write(input, 0, input.length);
            }

            int status = connection.getResponseCode();

            if(status < 300) {
                success = true;
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            connection.disconnect();
        }
        return success;
    }
    public boolean updateBlogPost(BlogPost updatedBlogPost, int id){
        String target = "/update/" + id;

        boolean success = false;

        try {
            URL url = new URL(apiAdress + target);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setDoOutput(true);


            try (OutputStream os = connection.getOutputStream()){
                byte[] input = updatedBlogPost.toJson().getBytes(StandardCharsets.UTF_8);

                os.write(input, 0, input.length);
            }

            int status = connection.getResponseCode();

            if(status < 300) {
                success = true;
            }
        } catch (Exception e){
            System.out.println("Exception: " + e);
        } finally {
            connection.disconnect();
        }
        return success;
    }
}
