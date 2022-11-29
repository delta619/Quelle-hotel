package jettyServer;
import java.util.UUID;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import hotelapp.Hotel;
import hotelapp.Review;
import hotelapp.ThreadSafeHotelHandler;
import hotelapp.ThreadSafeReviewHandler;
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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.TreeSet;

public class HotelDetailsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession();
        String loggedUser = (String) session.getAttribute("loggedUser");
        if(loggedUser == null){
            response.sendRedirect("/register");
        }


        ThreadSafeHotelHandler hotelData = (ThreadSafeHotelHandler) getServletContext().getAttribute("hotelController");
        ThreadSafeReviewHandler reviewData = (ThreadSafeReviewHandler) getServletContext().getAttribute("reviewController");

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = response.getWriter();
        String hotelId = request.getParameter("hotelId");

        hotelId = StringEscapeUtils.escapeHtml4(hotelId);

        if(hotelId == null){
            out.println(Helper.hotelResponseGenerator(false, null));
            return;
        }

        Hotel hotelDetails = hotelData.findHotelId(hotelId);
        if(hotelDetails == null){
            out.println(Helper.hotelResponseGenerator(false, null));
            return;
        }

        TreeSet<Review> reviews =  reviewData.findReviewsByHotelId(hotelId, true);

        double sumRating = 0.0;
        double avgRating = 0.0;

        for(Review review : reviews){
            sumRating += review.getRatingOverall();
        }
        avgRating = sumRating / Math.max(1, reviews.size());


        VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();
        context.put("address", hotelDetails.getAddress());
        context.put("name", hotelDetails.getName());
        context.put("hotelId", hotelDetails.getId());
        context.put("loggedUser", loggedUser);

        context.put("avgRating", avgRating);
        context.put("link", "https://www.expedia.com/" + hotelDetails.getCity() + "-Hotels-" + hotelDetails.getName()+ ".h" + hotelId + ".Hotel-Information");
        context.put("city", hotelDetails.getCity());


        context.put("reviews", reviews);

        Template template = ve.getTemplate(Helper.CONSTANTS.HOTEL_DETAILS);

        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        out.println(writer.toString());

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession();
        String loggedUser = (String) session.getAttribute("loggedUser");
        if(loggedUser == null){
            response.sendRedirect("/register");
        }

        ThreadSafeHotelHandler hotelData = (ThreadSafeHotelHandler) getServletContext().getAttribute("hotelController");
        ThreadSafeReviewHandler reviewData = (ThreadSafeReviewHandler) getServletContext().getAttribute("reviewController");

        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();

        String hotelId = request.getParameter("hotelId");
        String reviewText = request.getParameter("reviewText");
        String reviewTitle = request.getParameter("reviewTitle");


        hotelId = StringEscapeUtils.escapeHtml4(hotelId);
        reviewText = StringEscapeUtils.escapeHtml4(reviewText);
        reviewTitle = StringEscapeUtils.escapeHtml4(reviewTitle);

        if(hotelId == null){
            out.println(Helper.hotelResponseGenerator(false, null));
            return;
        }
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        reviewData.insertReview(reviewTitle, reviewText, hotelId, uuid, loggedUser, new Date().toString());

        response.sendRedirect("/hotelInfo?hotelId=" + hotelId);

    }

}
