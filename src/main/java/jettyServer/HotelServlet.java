package jettyServer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
        ThreadSafeHotelHandler hotelData = (ThreadSafeHotelHandler) getServletContext().getAttribute("hotelController");

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        Boolean isSubString = false;
        Boolean isHotelId = false;

        String sub = request.getParameter("sub");
        sub = StringEscapeUtils.escapeHtml4(sub);

        String hotelId = request.getParameter("hotelId");
        hotelId = StringEscapeUtils.escapeHtml4(hotelId);


        if(sub != null){
            isSubString = true;
        }
        if(hotelId != null){
            isHotelId = true;
        }

        if(!isSubString && !isHotelId){
            // show all hotels from the database
            JsonObject res = new JsonObject();
            JsonArray jsonArray = hotelData.findHotelsUsingSubstring("");
            res.add("hotels", jsonArray);
            out.println(Helper.hotelResponseGenerator(true, res));
            return;
        }



        JsonArray hotels;

        if(isSubString){
            hotels = hotelData.findHotelsUsingSubstring(sub);
            if(hotels.size() == 0){
                out.println(Helper.hotelResponseGenerator(false, null));
                return;
            }

            JsonObject res = new JsonObject();
            res.addProperty("success", true);
            res.add("hotels", hotels);
            out.println(Helper.hotelResponseGenerator(true, res));
            return;

        }else {
            JsonObject hotel = hotelData.getHotelInfoJson(hotelId);
            if(hotel == null){
                out.println(Helper.hotelResponseGenerator(false, null));
                return;
            }

            JsonObject res = new JsonObject();
            res.add("hotel", hotel);
            out.println(Helper.hotelResponseGenerator(true, res));
            return;
        }


    }
}
