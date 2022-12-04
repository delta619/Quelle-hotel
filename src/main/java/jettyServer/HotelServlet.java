package jettyServer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import db.DatabaseHandler;
import hotelapp.Hotel;
import hotelapp.ThreadSafeHotelHandler;
import hotelapp.ThreadSafeReviewHandler;
import org.apache.commons.text.StringEscapeUtils;
import org.eclipse.jetty.util.StringUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class HotelServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String loggedUser = Helper.validateSession(request, response);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        DatabaseHandler db = (DatabaseHandler) getServletContext().getAttribute("dbController");

        try {

            String sub = request.getParameter("sub");
            if (sub == null) {
                sub = "";
            }
            sub = StringEscapeUtils.escapeHtml4(sub);

            ArrayList<Hotel> hotels = db.getHotelsUsingSubstring(sub.trim());
            if (hotels.size() == 0) {
                response.setStatus(HttpServletResponse.SC_OK);
                out.print(Helper.hotelResponseGenerator(false, null));
                return;
            }

            JsonObject res = new JsonObject();
            JsonArray hotelsArray = new JsonArray();
            for (Hotel hotel : hotels) {
                hotelsArray.add(hotel.toJson());
            }

            res.add("hotels", hotelsArray);
            out.print(Helper.hotelResponseGenerator(true, res));


            // send hotels in json format

        } catch (Exception e) {
            e.printStackTrace();
            out.println(Helper.failedResponseGenerator("Something went wrong."));
        }








    }
}
