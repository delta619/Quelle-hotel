package jettyServer;

import db.DatabaseHandler;
import hotelapp.ThreadSafeHotelHandler;
import hotelapp.ThreadSafeReviewHandler;
import org.apache.velocity.app.VelocityEngine;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

/** This class uses Jetty & servlets to implement server serving hotel and review info */
public class JettyServer {
    // FILL IN CODE
    private static final int PORT = 8080;

    /**
     * Function that starts the server
     * @throws Exception throws exception if access failed
     */

    ThreadSafeReviewHandler tsReviewHandler;
    ThreadSafeHotelHandler tsHotelHandler;
    public JettyServer(ThreadSafeHotelHandler tsHotelHandler, ThreadSafeReviewHandler tsReviewHandler){

        this.tsReviewHandler = tsReviewHandler;
        this.tsHotelHandler = tsHotelHandler;


    }
    public  void start() throws Exception {
        Server server = new Server(PORT); // jetty server

        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);


        // FILL IN CODE:
        // Map end points to servlets
        VelocityEngine velocity = new VelocityEngine();
        velocity.init();
        handler.setAttribute("templateEngine", velocity);
        handler.setAttribute("reviewController", tsReviewHandler );
        handler.setAttribute("hotelController", tsHotelHandler );
        handler.setAttribute("dbController", DatabaseHandler.getInstance());


        Helper.setUpDB( tsHotelHandler,  tsReviewHandler);


        handler.addServlet(HomeServlet.class, "/home");
        handler.addServlet(RegisterServlet.class, "/register");
        handler.addServlet(LoginServlet.class, "/login");
        handler.addServlet(LogoutServlet.class, "/logout");
        handler.addServlet(HotelServlet.class, "/hotels");
        handler.addServlet(HotelDetailsServlet.class, "/hotelInfo");
        handler.addServlet(ReviewServlet.class, "/review");
        handler.addServlet(LoadData.class, "/loadData");
        handler.addServlet(UserActionsServlet.class, "/userActions");





        server.setHandler(handler);
        server.start();
        server.join();
    }

}
