package edu.cmu.ds.project4android;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

// Project4 Android App - "Getters" - this handles all searches / information processing
// Dan Molenhouse - dmolenho
// 04.10.2022
// Based on the Interesting Picture android lab code & online resources as cited

public class itunesGet {

    //Initialize variables
    int resultCount;
    itunesMain tvMain = null;
    ArrayList<tvShow> tvShowList;

    //does the itunes search
    public void search(String searchTerm, itunesMain main) {
        this.tvMain = main;
        new iTunesSearch().execute(searchTerm);
    }

    private class iTunesSearch extends AsyncTask<String, Void, ArrayList> {

        // To be honest I dont really understand what this is doing,
        // I am implementing what I found on https://stackoverflow.com/questions/28813890/android-how-can-i-return-5-string-arrays-in-doinbackground
        protected ArrayList doInBackground(String... strings) {
            return search(strings[0]);
        }

        // Just sets up views for when a search term is submitted
        protected void onPostExecute(ArrayList tvShowList) {
            tvMain.arrangeViews(resultCount, tvShowList);
        }

        private ArrayList search(String searchTerm) {

            String jsonString = "", line;
            tvShowList = new ArrayList<>();

            try {

                // Changing these will allow you to access the appropriate model/servlet for task 1 or task 2

                //Task 1:
                //String urlBase = "https://pure-shore-63134.herokuapp.com/"; //URL for Task 1 Model / Servlet

                //Task 2:
                String urlBase = "https://cryptic-brushlands-72356.herokuapp.com/"; //URL for Task 2 Model / Servlet

                //Get URL
                URL url = new URL(urlBase + "getTvShows/" + searchTerm);

                //Basic URL formatting and setup taken from Blockchain project
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");

                //Setup JsonData if the response is good
                if (connection.getResponseCode() == 200) {
                    System.out.println("HTTP Reply Code: 200");
                    BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
                    while ((line = br.readLine()) != null) {
                        jsonString += line; //Assemble Json String
                    }
                }
                else{
                    System.out.println("HTTP Reply Code: " + connection.getResponseCode());
                }


                tvShowList = getShowList(jsonString);

            } catch (Exception error) {
                System.out.print("Error:" + error);
                return null;
            }

            //return List view of TV Shows
            return tvShowList;
        }

        private ArrayList getShowList(String jsonString) {
            try {

                //Create JsonObject from string
                JSONObject json = new JSONObject(jsonString);

                //Number of results (50 maximum)
                resultCount = json.getInt("resultCount");

                //get results array as done previously
                JSONArray resultArray = json.getJSONArray("results");

                //Populate
                for (int i = 0, size = resultArray.length(); i < size; i++) {

                    JSONObject jo = (JSONObject) resultArray.get(i);

                    //Bitmap image taken from URL (see method for citations)
                    Bitmap imageBitmap = getImage(new URL(jo.getString("artworkUrl100")));

                    // add the new tune using the above info to the tune list
                    tvShowList.add(new tvShow(jo.getString("artistName"),jo.getString("trackName"),jo.getString("primaryGenreName"),imageBitmap));
                }

            } catch (Exception e) {
                System.out.print("Error: " + e);
                return null;
            }
            return tvShowList;
        }
    }

    //Basic URL to Bitmap image fetcher taken from https://stackoverflow.com/questions/11831188/how-to-get-bitmap-from-a-url-in-android
    private Bitmap getImage(URL url) {
        try {
            URLConnection connection = url.openConnection();
            connection.connect();
            BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
            Bitmap image = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

