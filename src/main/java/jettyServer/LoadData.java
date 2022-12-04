package jettyServer;

import db.DatabaseHandler;
import hotelapp.Hotel;
import hotelapp.Review;
import hotelapp.ThreadSafeHotelHandler;
import hotelapp.ThreadSafeReviewHandler;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.StringJoiner;

public class LoadData extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // load all the data from reviews and hotels to the database
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);



    response.getWriter().println("Data loaded");


    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

}
