package jettyServer;

import db.DatabaseHandler;
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
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class EditReview extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String reviewId = request.getParameter("reviewId");
        String hotelId = request.getParameter("hotelId");

        if(reviewId == null || hotelId == null){
            response.sendRedirect("/home");
        }
        String loggedUser = (String) request.getSession().getAttribute("loggedUser");

        if(loggedUser == null){
            response.sendRedirect("/register");
        }

        reviewId = StringEscapeUtils.escapeHtml4(reviewId);
        hotelId = StringEscapeUtils.escapeHtml4(hotelId);


        System.out.println("ReviewId: " + reviewId);
        System.out.println("HotelId: " + hotelId);


        ThreadSafeHotelHandler hotelData = (ThreadSafeHotelHandler) getServletContext().getAttribute("hotelController");
        ThreadSafeReviewHandler reviewData = (ThreadSafeReviewHandler) getServletContext().getAttribute("reviewController");

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();


        Hotel hotel = hotelData.findHotelId(hotelId);
        Review review = reviewData.findReviewUsingHotelIdAndReviewId(hotelId, reviewId);

        if(review == null){
            response.sendRedirect("/home");
        }

        if(!review.getUserNickname().equals(loggedUser)){
            response.sendRedirect("/home");
        }

        VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();

        Template template = ve.getTemplate(Helper.CONSTANTS.EDIT_REVIEW);

        context.put("review", review);
        context.put("hotelId", hotelId);
        context.put("reviewId", reviewId);
        context.put("loggedUser", loggedUser);
        context.put("address", hotel.getAddress());
        context.put("name", hotel.getName());
        context.put("reviewTitle", review.getTitle());
        context.put("reviewText", review.getReviewText());

        StringWriter writer = new StringWriter();

        template.merge(context, writer);

        out.println(writer.toString());



    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String reviewId = request.getParameter("reviewId");
        String hotelId = request.getParameter("hotelId");
        String reviewTitle = request.getParameter("reviewTitle");
        String reviewText = request.getParameter("reviewText");
        ThreadSafeReviewHandler reviewData = (ThreadSafeReviewHandler) getServletContext().getAttribute("reviewController");

        reviewId = StringEscapeUtils.escapeHtml4(reviewId);
        hotelId = StringEscapeUtils.escapeHtml4(hotelId);
        reviewTitle = StringEscapeUtils.escapeHtml4(reviewTitle);
        reviewText = StringEscapeUtils.escapeHtml4(reviewText);

        String loggedUser = (String) request.getSession().getAttribute("loggedUser");

        if(loggedUser == null){
            response.sendRedirect("/register");
        }
        if(!reviewData.findReviewUsingHotelIdAndReviewId(hotelId, reviewId).getUserNickname().equals(loggedUser)){
            response.sendRedirect("/home");
        }

        if (reviewId == null || hotelId == null) {
            response.sendRedirect("/home");
        }

        if (request.getParameter("delete") != null) {
            // delete the review reviewId from hotelId
            boolean result = reviewData.deleteReview(hotelId, reviewId);
            if(result){
                response.sendRedirect("/hotelInfo?hotelId=" + hotelId);
            } else {
                response.sendRedirect("/home");
            }
        }
        if(request.getParameter("save") != null){
            // update the reviewText and reviewTitle of reviewId from hotelId
            boolean result = reviewData.updateReview(hotelId, reviewId, reviewTitle, reviewText);
            if(result){
                response.sendRedirect("/hotelInfo?hotelId=" + hotelId);
            } else {
                response.sendRedirect("/home");
            }
        }



        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
    }

}
