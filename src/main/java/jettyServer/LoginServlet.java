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

public class LoginServlet extends HttpServlet {



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();

        VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();

        Template template = ve.getTemplate(Helper.CONSTANTS.LOGIN);

        StringWriter writer = new StringWriter();

        template.merge(context, writer);

        out.println(writer.toString());


    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        DatabaseHandler dhandler = DatabaseHandler.getInstance();
        try {
            dhandler.loginUser(username, password);
        } catch (Exception e) {
            VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
            VelocityContext context = new VelocityContext();
            context.put("error", e.getMessage());

            Template template = ve.getTemplate(Helper.CONSTANTS.LOGIN_FAILED);

            StringWriter writer = new StringWriter();

            template.merge(context, writer);

            out.println(writer.toString());
            return;
        }
        // redirect to homepage after a successful login post
        HttpSession session = request.getSession();
        session.setAttribute("loggedUser", username);

        response.sendRedirect("/home");


    }
}
