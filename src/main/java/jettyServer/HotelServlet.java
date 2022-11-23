package jettyServer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class HotelServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        String hotelId = request.getParameter("hotelId");
        if(hotelId == null){
            out.println(Helper.hotelResponseGenerator(false, null));
            return;
        }
//        JsonObject jsonResponse = Helper.getHotelInfo(hotelId);
//        out.println(Helper.hotelResponseGenerator(true, jsonResponse));
    }
}
