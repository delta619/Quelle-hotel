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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ReviewServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String loggedUser = Helper.validateSession(request, response);

        String reviewId = request.getParameter("reviewId");
        if(reviewId == null){
            response.sendRedirect("/home");
        }
        reviewId = StringEscapeUtils.escapeHtml4(reviewId);

        DatabaseHandler db = (DatabaseHandler) getServletContext().getAttribute("dbController");

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();


        Review review = db.getReviewUsingReviewId(reviewId);

        VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();

        Template template = ve.getTemplate(Helper.CONSTANTS.EDIT_REVIEW);

        context.put("review", review);
        context.put("hotel", db.getHotel(review.getHotelId()));
        context.put("hotelId", review.getHotelId());
        context.put("reviewId", reviewId);
        context.put("loggedUser", loggedUser);
        context.put("reviewTitle", review.getTitle());
        context.put("reviewText", review.getReviewText());


        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        out.println(writer.toString());

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String loggedUser = Helper.validateSession(request, response);

        String action = request.getParameter("action");
        String hotelId = request.getParameter("hotelId");

        DatabaseHandler db = (DatabaseHandler) getServletContext().getAttribute("dbController");

        PrintWriter out = response.getWriter();

        if (action != null || hotelId != null) {
            action = StringEscapeUtils.escapeHtml4(action);
        }
        action = StringEscapeUtils.escapeHtml4(action);
        hotelId = StringEscapeUtils.escapeHtml4(hotelId);

        try {
            if (action.equals("delete")) {
                String reviewId = request.getParameter("reviewId");
                reviewId = StringEscapeUtils.escapeHtml4(reviewId);

                Review review = db.getReviewUsingReviewId(reviewId);
                if (review.getUserNickname().equals(loggedUser)) {
                    db.deleteReview(reviewId);
                    out.println(Helper.userSuccessResponseGenerator(null));
                }
                db.deleteReview(reviewId);
            } else if (action.equals("add")) {
                String reviewTitle = request.getParameter("reviewTitle");
                String reviewText = request.getParameter("reviewText");
                String reviewRating = request.getParameter("reviewRating");

                reviewTitle = StringEscapeUtils.escapeHtml4(reviewTitle);
                reviewText = StringEscapeUtils.escapeHtml4(reviewText);
                reviewRating = StringEscapeUtils.escapeHtml4(reviewRating);

                String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 15);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                db.addReview(hotelId, uuid, reviewTitle, reviewText, reviewRating, loggedUser, sdf.format(new Date()));



            } else if (action.equals("update")) {
                String reviewId = request.getParameter("reviewId");
                String reviewTitle = request.getParameter("reviewTitle");
                String reviewText = request.getParameter("reviewText");
                String reviewRating = request.getParameter("reviewRating");

                reviewId = StringEscapeUtils.escapeHtml4(reviewId);
                reviewTitle = StringEscapeUtils.escapeHtml4(reviewTitle);
                reviewText = StringEscapeUtils.escapeHtml4(reviewText);
                reviewRating = StringEscapeUtils.escapeHtml4(reviewRating);

                db.updateReview(reviewId, reviewTitle, reviewText, reviewRating);

                out.println(Helper.userSuccessResponseGenerator(null));

            }
        } catch (Exception e) {
            out.println(Helper.failedResponseGenerator(e.getMessage()));
        } finally {
            response.sendRedirect("/hotelInfo?hotelId=" + hotelId);
        }

    }
}
