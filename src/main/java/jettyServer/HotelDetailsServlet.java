package jettyServer;
import java.util.UUID;

import com.google.gson.JsonArray;
import db.DatabaseHandler;
import hotelapp.Hotel;
import hotelapp.Review;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;

public class HotelDetailsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String loggedUser = Helper.getLoggedUser(request.getSession());

        DatabaseHandler db = (DatabaseHandler) getServletContext().getAttribute("dbController");

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = response.getWriter();
        String hotelId = request.getParameter("hotelId");
        String pageNo = request.getParameter("page");


        hotelId = StringEscapeUtils.escapeHtml4(hotelId);
        pageNo = StringEscapeUtils.escapeHtml4(pageNo);

        if(hotelId == null){
            out.println(Helper.hotelResponseGenerator(false, null));
            return;
        }

        // log the hotel user history



        Hotel hotelDetails = db.getHotel(hotelId);

        if(hotelDetails == null){
            out.println(Helper.hotelResponseGenerator(false, null));
            return;
        }

        db.insertHistory( loggedUser, hotelDetails.getId(), hotelDetails.getName(), Helper.getCurrentDate());


        double avgRating = db.getAvgRating(hotelId);
        avgRating = Math.round(avgRating * 100.0) / 100.0;


        VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();
        context.put("address", hotelDetails.getAddress());
        context.put("hotel", hotelDetails);
        context.put("name", hotelDetails.getName());
        context.put("hotelId", hotelDetails.getId());
        context.put("loggedUser", loggedUser);
        context.put("avgRating", avgRating);
        context.put("link", "https://www.expedia.com/" + hotelDetails.getCity() + "-Hotels-" + hotelDetails.getName()+ ".h" + hotelId + ".Hotel-Information");
        context.put("city", hotelDetails.getCity());

        if(pageNo == null){
            pageNo = "1";
        }

        // segregate reviews into pages
        int page = Integer.parseInt(pageNo);
        int pageSize = 5;
        int totalReviews = db.getReviewsCountUsingHotelId(hotelId);
        int totalPages = (int) Math.ceil((double) totalReviews / pageSize);
        int offset = (page - 1) * pageSize;
        int limit = pageSize;
        ArrayList<Review> reviewsPage = db.getReviewsUsingHotelId(hotelId, offset, limit);


        context.put("reviews", reviewsPage);
        context.put("totalPages", totalPages);
        context.put("latitude", db.getHotel(hotelId).getLatitude());
        context.put("longitude", db.getHotel(hotelId).getLongitude());

        Template template = ve.getTemplate(Helper.CONSTANTS.HOTEL_DETAILS);
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        out.println(writer.toString());

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {


        String loggedUser = Helper.getLoggedUser(request.getSession());

        String hotelId = request.getParameter("hotelId");
        String action = request.getParameter("action");
        DatabaseHandler db = (DatabaseHandler) getServletContext().getAttribute("dbController");

        if(action == null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        action = StringEscapeUtils.escapeHtml4(action);


        if(action.equals("fetchReviews")){
            int page = Integer.parseInt(request.getParameter("page"));

            int pageSize = 5;
            int offset = (page - 1) * pageSize;

            ArrayList<Review> reviewsPage = db.getReviewsUsingHotelId(hotelId, offset, pageSize);

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = response.getWriter();
            JsonArray reviews = new JsonArray();
            for(Review review : reviewsPage){
                reviews.add(review.toJson());
            }
            out.println(Helper.reviewResponseGenerator(true, reviews));


        }



    }

}
