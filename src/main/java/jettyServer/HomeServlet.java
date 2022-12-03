package jettyServer;

import db.DatabaseHandler;
import hotelapp.Hotel;
import hotelapp.ThreadSafeHotelHandler;
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

public class HomeServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {


        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        String loggedUser = (String) session.getAttribute("loggedUser");

//        if(loggedUser == null){
//            response.sendRedirect("/register");
//        }

        VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();
        context.put("loggedUser", "ash"); //TODO: change this to loggedUser
        ThreadSafeHotelHandler hotelData = (ThreadSafeHotelHandler) request.getServletContext().getAttribute("hotelController");
        ArrayList<Hotel> favHotels  = new ArrayList<>();
        ArrayList<String> favList = DatabaseHandler.getInstance().getFavHotels("ash"); //TODO: change this to loggedUser
        for(String fav : favList){
            favHotels.add(hotelData.findHotelId(fav));
        }
        context.put("favHotels", favHotels);
        Template template = ve.getTemplate(Helper.CONSTANTS.HOME);

        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        out.println(writer.toString());



    }
}
