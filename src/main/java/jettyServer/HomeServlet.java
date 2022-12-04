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
        String loggedUser = Helper.validateSession(request, response);


        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();

        DatabaseHandler db = (DatabaseHandler) getServletContext().getAttribute("dbController");

        VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();
        context.put("loggedUser", loggedUser);
        context.put("history", db.getUserHistory(loggedUser));
        context.put("lastLogin", Helper.getReadableTime((String) request.getSession().getAttribute("lastLogin")));

        ThreadSafeHotelHandler hotelData = (ThreadSafeHotelHandler) request.getServletContext().getAttribute("hotelController");
        ArrayList<Hotel> favHotels  = new ArrayList<>();
        ArrayList<String> favList = db.getFavHotels(loggedUser);
        for(String favID : favList){
            favHotels.add(db.getHotel(favID));
        }
        context.put("favHotels", favHotels);

        Template template = ve.getTemplate(Helper.CONSTANTS.HOME);
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        out.println(writer.toString());



    }
}
