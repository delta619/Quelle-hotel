package jettyServer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class HotelDetailsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

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

        JsonObject res = new JsonObject();


        JsonObject hotelDetails = hotelData.getHotelInfoJson(hotelId);
        if(hotelDetails == null){
            out.println(Helper.hotelResponseGenerator(false, null));
            return;
        }

        JsonArray reviews =  reviewData.findReviewsByHotelIdJson(hotelId, 1000000);

        double sumRating = 0.0;
        double avgRating = 0.0;

        for(int i = 0; i < reviews.size(); i++){
            sumRating += reviews.get(i).getAsJsonObject().get("ratingOverall").getAsDouble();
        }
        avgRating = sumRating / Math.max(1,reviews.size());

        hotelDetails.addProperty("avgRating", avgRating);
        hotelDetails.addProperty("link", "https://www.expedia.com/" + hotelDetails.get("city").getAsString() + "-Hotels-" + hotelDetails.get("name").getAsString() + ".h" + hotelId + ".Hotel-Information");

//        res.add("hotelDetails", hotelDetails);
//        res.add("reviews",reviews);
//
//        out.println(Helper.hotelResponseGenerator(true, res));
//     return;
        //////////////////////////////
//
        VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();

        context.put("hotelData", hotelDetails);
        context.put("reviews", reviews);

        Template template = ve.getTemplate(Helper.CONSTANTS.HOTEL_DETAILS);

        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        out.println(writer.toString());








    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ThreadSafeHotelHandler hotelData = (ThreadSafeHotelHandler) getServletContext().getAttribute("hotelController");
        ThreadSafeReviewHandler reviewData = (ThreadSafeReviewHandler) getServletContext().getAttribute("reviewController");

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        Boolean isHotelId = false;
        String hotelId = request.getParameter("hotelId");
        hotelId = StringEscapeUtils.escapeHtml4(hotelId);
        if(hotelId != null){
            isHotelId = true;
        }
        if(!isHotelId){
            out.println(Helper.hotelResponseGenerator(false, null));
            return;
        }
    }

}
