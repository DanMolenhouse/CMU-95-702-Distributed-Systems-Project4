package ds;

import com.google.gson.JsonObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.Document;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

// Project4 Task2 Servlet - dmolenho Dan Molenhouse
// 04.10.2022
// Handles get requests and sends data to MongoDB

//Sets two acceptable url patterns
@WebServlet(name = "Servlet",
        urlPatterns = {"/getTvShows/*","/getDashboard"})

public class Servlet extends HttpServlet {

    Model model = null;

    @Override
    public void init () {
        model = new Model();
    }

    //goGet will change depending on URL received from input
    //If /getTvShows then it performs the search from Task1
    //If /getDashboard, then dashboard is displayed
    @Override
    protected void doGet (HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        //Standard search result
        if (request.getServletPath().equals("/getTvShows")) {

            String searchItem = (request.getPathInfo()).substring(1);

            response.setStatus(200);

            // User agent methodology taken from: https://stackoverflow.com/questions/1995439/get-android-phone-model-programmatically-how-to-get-device-name-and-model-prog
            String agent_temp = request.getHeader("User-Agent");
            String device;
            String browser;

            //Tests for different types of devices
            if(agent_temp.contains("Mac")){
                device = "Apple Computer";
            }else if(agent_temp.contains("Windows")){
                device = "Windows PC";
            }else if(agent_temp.contains("Android")){
                device = "Android Phone";
            }else{
                device = "Apple Phone";
            }

            //Tests for different web browsers
            if(agent_temp.contains("Safari")){
                browser = "Safari";
            }else if(agent_temp.contains("Firefox")){
                browser = "Firefox";
            }else if(agent_temp.contains("Explorer")){
                browser = "Internet Explorer";
            }else if(agent_temp.contains("Chrome")) {
                browser = "Chrome";
            }else{
                browser = "No Browser (Servlet Access)";
            }

            //API request
            String url = "https://itunes.apple.com/search?media=tvShow&term=" + searchItem;

            //
            if ((searchItem != null && !searchItem.equals(""))) {
                try {
                   //perform search
                    JsonObject output = model.search(searchItem);

                    //get number of results
                    int resultCount = output.get("resultCount").getAsInt();

                    //Date formatter taken from:
                    //https://www.javatpoint.com/java-get-current-date
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date date = new Date();

                    //use doPost method to send data to mongoDb
                    doPost(device, browser, searchItem, resultCount,formatter.format(date),output);

                    //Send result to client
                    PrintWriter out = response.getWriter();
                    out.println(output);
                } catch (Exception e) {
                    System.out.println("Exception: " + e);
                }
            }
        }

        //If dashboard is requested
        if (request.getServletPath().equals("/getDashboard")) {

                //MongoDB Fetch accesses the database and gets a log of all info in the form of an array list
                ArrayList<String> dbLogs = model.doMongoDBFetch();

                //Access specific information for the dashboard
                request.setAttribute("date", dbLogs.get(0));
                request.setAttribute("recentSearch", dbLogs.get(1));
                request.setAttribute("search", dbLogs.get(2));
                request.setAttribute("logs", dbLogs.get(3));

               //view the dashboard.jsp file
                RequestDispatcher view = request.getRequestDispatcher("dashboard.jsp");
                view.forward(request, response);
            }
        }

        //HTTP Post request
    protected void doPost (String device, String browser, String search, int resultCount, String date, Object jsonData) {

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

        //Just creates a JsonObject that will have all the required information in it for the dashboard to access
        JsonObject dashboardInfo = new JsonObject();
        dashboardInfo.addProperty("device", device);
        dashboardInfo.addProperty("browser", browser);
        dashboardInfo.addProperty("search", search);
        dashboardInfo.addProperty("count", resultCount);
        dashboardInfo.addProperty("date", date);
        dashboardInfo.addProperty("json", String.valueOf(jsonData)); //Dashboard doesnt view this infromation, but I wanted to include it for debugging and it is a good thing to store in the database

        //HTTP Post to MongoDB Database
        database.getCollection("tvShows").insertOne(new Document(Document.parse(dashboardInfo.toString())));

    }


}
