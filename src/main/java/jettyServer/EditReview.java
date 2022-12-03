package jettyServer;

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


        DatabaseHandler db = (DatabaseHandler) getServletContext().getAttribute("dbController");


        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();


        Hotel hotel = db.getHotel(hotelId);
        Review review = db.getReviewUsingReviewId(reviewId);

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
        DatabaseHandler db = (DatabaseHandler) getServletContext().getAttribute("dbController");
        PrintWriter out = response.getWriter();

        reviewId = StringEscapeUtils.escapeHtml4(reviewId);
        hotelId = StringEscapeUtils.escapeHtml4(hotelId);
        reviewTitle = StringEscapeUtils.escapeHtml4(reviewTitle);
        reviewText = StringEscapeUtils.escapeHtml4(reviewText);

        String loggedUser = (String) request.getSession().getAttribute("loggedUser");

        if(loggedUser == null){
            response.sendRedirect("/register");
        }
        if(!db.getReviewUsingReviewId(reviewId).getUserNickname().equals(loggedUser)){
            response.sendRedirect("/home");
        }

        if (reviewId == null || hotelId == null) {
            response.sendRedirect("/home");
        }

        if (request.getParameter("delete") != null) {
            // delete the review reviewId from hotelId
            boolean result = db.deleteReview(reviewId);
            if(result){
                response.sendRedirect("/hotelInfo?hotelId=" + hotelId);
            } else {
                out.println(Helper.failedResponseGenerator("Failed to delete review"));
                return;
            }
        }
        if(request.getParameter("save") != null){
            // update the reviewText and reviewTitle of reviewId from hotelId
            boolean result = db.updateReview(hotelId, reviewId, reviewTitle, reviewText);
            if(result){
                response.sendRedirect("/hotelInfo?hotelId=" + hotelId);
            } else {
                response.sendRedirect("/home");
            }
        }



        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
