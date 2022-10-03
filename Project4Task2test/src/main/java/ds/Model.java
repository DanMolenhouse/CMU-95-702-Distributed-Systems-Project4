package ds;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import com.google.gson.*;
import com.mongodb.*;
import com.mongodb.client.*;
import org.bson.Document;

// Project4 Task2 Model - dmolenho Dan Molenhouse
// 04.10.2022
// Handles all backend caluclations for Servlet
// Model / Servlet based on InterestingPicture lab 2 and other labs from DS, so some remnants of those labs still exist

public class Model {

    //GSON weirdness
    Gson gson = new Gson();

    //search reused from Task1
    public JsonObject search (String searchString)
            throws UnsupportedEncodingException, FileNotFoundException {

        searchString = URLEncoder.encode(searchString, "UTF-8");

        //get URL for the itunes API
        String url = "https://itunes.apple.com/search?media=tvShow&term=" + searchString;

        //Get Json String reply using fetch (borrowed from early DS labs)
        String results = fetch(url);

        //Standard String -> JsonObject method that I've been using for the past few projects
        // Parse the Json string to get the Json object
        JsonObject output = new JsonObject();
        JsonParser jsonParser = new JsonParser();
        JsonObject object = (JsonObject) jsonParser.parse(results);

        // Main JsonArray of results is stored in the "results" key of JsonObject
        JsonArray jsonArray = object.getAsJsonArray("results");

        output.addProperty("resultCount", jsonArray.size()); //Length of array is the number of results

        List<JsonObject> entries = new ArrayList<JsonObject>();

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

                response += string;
            }
            in.close();

        } catch (IOException e) {
            System.out.println("Exception: " + e);
        }
        return response;
    }

    //MongoDB Fetch added for task 2, it simply grabs the information from our MongoDB and gives it back to the servlet
    public ArrayList doMongoDBFetch () {
        //Initialize all lists / variables for storing mongoDb database information
        ArrayList<String> log = new ArrayList();
        ArrayList<String> searchList = new ArrayList();
        String logValues = "";
        String recentSearch = null;
        String recentAccess = null;
        List<String> toAdd = new ArrayList<>();

        //Sourced from MongoDB instruction in Project4Task0
        ConnectionString connectionString = new ConnectionString("mongodb+srv://DanMolenhouse:danmolenhouse@cluster0.vridi.mongodb.net/myFirstDatabase?retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .serverApi(ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build())
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase("tvShows");
        MongoCollection collection = database.getCollection("tvShows");
        MongoCursor<Document> cursor = collection.find().iterator();

        //Cursor essentially just parses through MongoDB database reply
        try {
            while (cursor.hasNext()) {

                //get information as a Json String
                String report = cursor.next().toJson();

                //JsonParser to go from string to Json Object (same as before)
                JsonParser jsonParser = new JsonParser();
                JsonElement element = jsonParser.parse(report);
                JsonObject object = gson.fromJson(element, JsonObject.class);

                // append logValues string with next result
                logValues += "<tr><td>"+ object.get("device")+"</td><td>"+object.get("browser")+"</td><td>"+object.get("search")+"</td><td>"+object.get("date")+"</td><td>"+object.get("count")+"</td>";

                //Attempt to add a single reseult of each search string to an array
                String search = object.get("search").getAsString();
                if(searchList.size()==0){
                    searchList.add(search);
                }
                // Avoiding concurrent modification exemption https://stackoverflow.com/questions/18448671/how-to-avoid-concurrentmodificationexception-while-removing-elements-from-arr
                for(String item : searchList){
                    if(!item.contains(search)){
                        toAdd.add(search);
                    }
                }

                //Get the most recent search term and date of access
                recentSearch = object.get("search").getAsString();
                recentAccess = object.get("date").getAsString();
            }
            searchList.addAll(toAdd);

        } finally {
            // close the cursor
            cursor.close();
        }

        //log is all of the results
        log.add(recentAccess);
        log.add(recentSearch);
        log.add(searchList.toString());
        log.add(logValues);

        return log;
    }


}