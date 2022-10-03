package ds;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import com.google.gson.Gson;

// Project4 Task1 Model - dmolenho Dan Molenhouse
// 04.10.2022
// Handles all backend caluclations for Servlet
// Model / Servlet based on InterestingPicture lab 2 and other labs from DS, so some remnants of those labs still exist

public class Model {

    public JsonObject search (String searchString)
            throws UnsupportedEncodingException, FileNotFoundException {

        searchString = URLEncoder.encode(searchString, "UTF-8");

        //get URL for the itunes API
        String iTunesURL = "https://itunes.apple.com/search?media=tvShow&term=" + searchString;

        //Get Json String reply using fetch (borrowed from early DS labs)
        String results = fetch(iTunesURL);

        //Standard String -> JsonObject method that I've been using for the past few projects
        // Parse the Json string to get the Json object
        JsonObject output = new JsonObject();
        JsonParser jsonParser = new JsonParser();
        JsonObject object = (JsonObject) jsonParser.parse(results);

        // Main JsonArray of results is stored in the "results" key of JsonObject
        JsonArray jsonArray = object.getAsJsonArray("results");

        output.addProperty("resultCount", jsonArray.size());

        List<JsonObject> entries = new ArrayList<JsonObject>();
        Gson gson = new Gson();

        //Loops through array and pulls important information out, temporarily stores as an entry
        for (int i = 0; i < jsonArray.size(); i++) {

            JsonObject entry = new JsonObject();

            // get the JSON object for each json array
            JsonObject tempObj = (JsonObject) jsonArray.get(i);

            //Get artistName, trackName, Genre, and artwork
            if(tempObj.get("artistName")!=null){
                entry.addProperty("artistName", tempObj.get("artistName").getAsString());
            }
            if(tempObj.get("trackName")!=null){
                entry.addProperty("trackName", tempObj.get("trackName").getAsString());
            }
            if(tempObj.get("primaryGenreName")!=null){
                entry.addProperty("primaryGenreName", tempObj.get("primaryGenreName").getAsString());
            }
            if(tempObj.get("artworkUrl100")!=null){
                entry.addProperty("artworkUrl100", tempObj.get("artworkUrl100").getAsString());
            }

            // add item JSON as the value of results key within dataset JSON
            entries.add(entry);
        }

        //Final assembly of output object containing all important info
        output.add("results", gson.toJsonTree(entries));
        return output;
    }

    //Fetch method taken from previous servlet implementations in DS Labs
    private String fetch (String urlStr) {
        String response = "";

        try {

            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Read all the text returned by the server
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String string;

            // Read each line of "in" until done, adding each to "response"
            while ((string = in.readLine()) != null) {

                // str is one line of text readLine() strips newline characters
                response += string;
            }
            in.close();

        } catch (IOException e) {
            System.out.println("Eeek, an exception");
            // Do something reasonable.  This is left for students to do.
        }
        return response;
    }


}