package ds;

import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

// Project4 Task1 Servlet - dmolenho Dan Molenhouse
// 04.10.2022
// Handles get requests and sends data to MongoDB
// Based on InterestingPicture model / servlet example

//Sets two acceptable url patterns
@WebServlet(name = "Servlet",
        urlPatterns = {"/getTvShows/*"})
public class Servlet extends HttpServlet {

    Model model = null;

    @Override
    public void init () {
        model = new Model();
    }


    //If /getTvShows then it performs the search
    @Override
    protected void doGet (HttpServletRequest request,
                          HttpServletResponse response) {

        //get the correct search term
        String searchItem = (request.getPathInfo()).substring(1);

        response.setStatus(200);

        //Performs the search here if the searchTerm exists
        if ((searchItem != null && !searchItem.equals(""))) {
            try {

                JsonObject output = model.search(searchItem);
                PrintWriter out = response.getWriter();
                out.println(output);
            } catch (Exception e) {
                System.out.println("Exception: " + e);
            }
        }

        response.setContentType("application/json;charset=utf-8");
    }
}
