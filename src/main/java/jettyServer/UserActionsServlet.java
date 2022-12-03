package jettyServer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import db.DatabaseHandler;
import org.apache.commons.text.StringEscapeUtils;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URL;

public class UserActionsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();

        String loggedUser = "ash";

        DatabaseHandler db = (DatabaseHandler) getServletContext().getAttribute("dbController");

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();


        try {

            String action = request.getParameter("action");

            if (action == null) {
                out.println(Helper.failedResponseGenerator("Action not specified"));
                return;
            }else{
                action = StringEscapeUtils.escapeHtml4(action);
            }

            if ("addToFavourite".equals(action)) {

                String hotelId = request.getParameter("hotelId");
                hotelId = StringEscapeUtils.escapeHtml4(hotelId);

                db.addFavourite(hotelId, loggedUser);
                out.println(Helper.userSuccessResponseGenerator(null));
            } else if ("removeFromFavourite".equals(action)) {
                String hotelId = request.getParameter("hotelId");
                hotelId = StringEscapeUtils.escapeHtml4(hotelId);

                db.removeFavourite(hotelId, loggedUser);
                out.println(Helper.userSuccessResponseGenerator(null));

            } else if ("isHotelFavourite".equals(action)) {
                String hotelId = request.getParameter("hotelId");
                hotelId = StringEscapeUtils.escapeHtml4(hotelId);
                boolean isFavourite = db.isFavourite(hotelId, loggedUser);

                if(isFavourite){
                    out.println(Helper.userSuccessResponseGenerator(null));
                }else {
                    out.println(Helper.failedResponseGenerator(null));
                }
            } else if ("fetchWeather".equals(action)) {
                String hotelId = request.getParameter("hotelId");
                hotelId = StringEscapeUtils.escapeHtml4(hotelId);

                String[] weatherInfo = getWeatherInfo(db.getHotel(hotelId).getLatitude(), db.getHotel(hotelId).getLongitude());
                JsonObject weatherJson = new JsonObject();
                weatherJson.addProperty("temperature", weatherInfo[0]);
                weatherJson.addProperty("windspeed", weatherInfo[1]);
                out.println(Helper.userSuccessResponseGenerator(weatherJson));
            }
        }
        catch (Exception e){
            out.println(Helper.failedResponseGenerator("Something went wrong"));
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    public String[] getWeatherInfo(String latitude, String longitude) {
        PrintWriter out = null;
        BufferedReader in = null;
        SSLSocket socket = null;

        try {
            String urlString = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&" + "longitude=" + longitude + "&current_weather=true";
            URL url = new URL(urlString);

            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();

            // HTTPS uses port 443
            socket = (SSLSocket) factory.createSocket(url.getHost(), 443);

            // output stream for the secure socket
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            String request = getRequest(url.getHost(), url.getPath() + "?" + url.getQuery());

            out.println(request); // send a request to the server
            out.flush();

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // use input stream to read server's response
            String line;
            StringBuffer sb = new StringBuffer();
            String body = "";
            while ((line = in.readLine()) != null) {
                sb.append(line);

                // curly braces are used to indicate the beginning and end of a JSON object
                if (checkBrack(line)) {
                    body += line;
                }
            }
            JsonParser parser = new JsonParser();
            JsonObject jo = (JsonObject) parser.parse(body);
            String temperature = jo.get("current_weather").getAsJsonObject().get("temperature").getAsString();
            String windspeed = jo.get("current_weather").getAsJsonObject().get("windspeed").getAsString();

            return new String[]{temperature, windspeed};

        } catch (Exception e){
            System.out.println("Exception lat long");
            e.printStackTrace();
        }
        finally {
            try {
                // close the streams and the socket
                out.close();
                in.close();
                socket.close();
            } catch (IOException e) {
                System.out.println("An exception occured while trying to close the streams or the socket: " + e);
            }

        }

        return new String[]{"NA", "NA"}; // 0 -> temperature 1 -> windspeed
    }
    public boolean checkBrack(String s){
        // if string contains curly braces, return true

        if(s.contains("{") || s.contains("}")){
            return true;
        }
        return false;
    };
    private static String getRequest(String host, String pathResourceQuery) {
        String request = "GET " + pathResourceQuery + " HTTP/1.1" + System.lineSeparator() // GET
                // request
                + "Host: " + host + System.lineSeparator() // Host header required for HTTP/1.1
                + "Connection: close" + System.lineSeparator() // make sure the server closes the
                // connection after we fetch one page
                + System.lineSeparator();
        return request;
    }

}
