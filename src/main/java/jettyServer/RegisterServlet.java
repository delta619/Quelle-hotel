package jettyServer;

import db.DatabaseHandler;
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

public class RegisterServlet extends HttpServlet {



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();

        VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();

        Template template = ve.getTemplate(Helper.CONSTANTS.REGISTER);

        StringWriter writer = new StringWriter();

        template.merge(context, writer);

        out.println(writer.toString());






    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = response.getWriter();

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        DatabaseHandler db = (DatabaseHandler) request.getServletContext().getAttribute("dbController");

        try {
            db.registerUser(username, password);
            HttpSession session = request.getSession();
            session.setAttribute("loggedUser", username);
            out.println(Helper.userSuccessResponseGenerator(null));
        } catch (Exception e) {
            out.println(Helper.failedResponseGenerator(e.getMessage()));
        }

    }
}
