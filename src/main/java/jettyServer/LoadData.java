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
import java.util.ArrayList;
import java.util.StringJoiner;

public class LoadData extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // load all the data from reviews and hotels to the database
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        clear();
        DatabaseHandler db = DatabaseHandler.getInstance();

        ThreadSafeHotelHandler hotelData = (ThreadSafeHotelHandler) getServletContext().getAttribute("hotelController");
        ThreadSafeReviewHandler reviewData = (ThreadSafeReviewHandler) getServletContext().getAttribute("reviewController");

        StringJoiner sj = new StringJoiner(",");
        for(Hotel hotel: hotelData.getAllHotels()){
            StringJoiner joiner = new StringJoiner("\",\"", "(\"", "\")");
            joiner.add(hotel.getId());
            joiner.add(hotel.getName());
            joiner.add(hotel.getAddress());
            joiner.add(hotel.getCity());
            joiner.add(hotel.getState());
            joiner.add(hotel.getLatitude());
            joiner.add(hotel.getLongitude());
            String query = joiner.toString() ;
            sj.add(query);
        }
        db.addHotel(sj.toString());
        sj = new StringJoiner(",");
        for(Review review: reviewData.getAllReviews()){
            StringJoiner joiner = new StringJoiner("\",\"", "(\"", "\")");
            joiner.add(review.getHotelId());
            joiner.add(review.getReviewId());
            joiner.add(review.getTitle().replaceAll("\"", ""));
            joiner.add(review.getReviewText().replaceAll("\"", "'"));
            joiner.add(String.valueOf(review.getRatingOverall()));
            joiner.add(review.getUserNickname().replaceAll("\"", ""));
            joiner.add(review.getReviewSubmissionDate());
            String query = joiner.toString() ;
//            System.out.println(query);
            sj.add(query);
        }
        db.addReview(sj.toString());

    response.getWriter().println("Data loaded");


    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    public void clear(){
        DatabaseHandler db = DatabaseHandler.getInstance();
        db.removeAllTables();
        db.CreateTables();
    }
}
