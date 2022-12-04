package jettyServer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import db.DatabaseHandler;
import hotelapp.Hotel;
import hotelapp.Review;
import hotelapp.ThreadSafeHotelHandler;
import hotelapp.ThreadSafeReviewHandler;

import javax.servlet.http.HttpSession;
import java.util.StringJoiner;

public class Helper {

    /**
     * This method generates the response for the review servlet
     * @param success boolean that indicates if the request was successful
     * @param jsonArr JsonObject that contains the review info
     * @return JsonObject that contains the response
     */
    public static Object reviewResponseGenerator (boolean success, JsonArray jsonArr){
        JsonObject jsonObject = new JsonObject();
        if(!success){
            jsonObject.addProperty("success", false);
            jsonObject.addProperty("hotelId", "invalid");
            return jsonObject;
        }
        jsonObject.addProperty("success", success);
        jsonObject.addProperty("hotelId", jsonArr.get(0).getAsJsonObject().get("hotelId").getAsString());

        JsonArray jsonArray = new JsonArray();
        jsonObject.add("reviews", jsonArr);
        return jsonObject;
    }

    /**
     * This method generates the response for the hotel servlet
     * @param success boolean that indicates if the request was successful
     * @param jsonResponse JsonObject that contains the hotel info
     * @return JsonObject that contains the response
     */
    public static Object hotelResponseGenerator (boolean success, JsonObject jsonResponse){
        if(!success){
            JsonObject jsonObj = new JsonObject();
            jsonObj.addProperty("success", false);
            jsonObj.addProperty("hotelId", "invalid");
            return jsonObj;
        }
        jsonResponse.addProperty("success", true);
        return jsonResponse;
    }


    /**
     * This method generates the response for the index servlet
     * @param success boolean that indicates if the request was successful
     * @param jsonArr JsonObject that contains the word reviews info
     * @return JsonObject that contains the response
     */
    public static Object wordResponseGenerator (boolean success, String word, JsonArray jsonArr){

        JsonObject jsonObject = new JsonObject();
        if(!success){
            jsonObject.addProperty("success", false);
            jsonObject.addProperty("word", "invalid");
            return jsonObject;
        }
        jsonObject.addProperty("success", success);
        jsonObject.addProperty("word", word);

        jsonObject.add("reviews", jsonArr);
        return jsonObject;

    }

    /**
     * This method generates the response for the weather servlet
     * @param success boolean that indicates if the request was successful
     * JsonObject that contains the hotel info
     * @return JsonObject that contains the response
     */
    public static Object weatherResponseGenerator (boolean success, JsonObject hotelObj, String temperature, String windspeed){
        JsonObject jsonObject = new JsonObject();
        if(!success){
            jsonObject.addProperty("success", false);
            jsonObject.addProperty("hotelId", "invalid");
            return jsonObject;
        }
        jsonObject.addProperty("success", success);
        jsonObject.addProperty("hotelId", hotelObj.get("hotelId").getAsString());
        jsonObject.addProperty("name", hotelObj.get("name").getAsString());
        jsonObject.addProperty("temperature", temperature);
        jsonObject.addProperty("windspeed", windspeed);

        return jsonObject;
    }

    public static Object userSuccessResponseGenerator(JsonObject userObj){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success", true);
        jsonObject.add("data", userObj);

        return jsonObject;
    }
    public static Object failedResponseGenerator(String message){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success", false);
        jsonObject.addProperty("message", message);
        return jsonObject;
    }

    public static String getLoggedUser(HttpSession session){
        if(session.getAttribute("loggedUser") == null){
            return "anonymous";
        }
        return session.getAttribute("loggedUser").toString();
    }

    public static String getCurrentDate(){
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
    }

    public static String getReadableTime(String timestamp){
        if(timestamp == null || timestamp.startsWith("First")){
            return "First time login.";
        }

        String[] time = timestamp.split(" ");
        String[] date = time[0].split("-");
        String[] hour = time[1].split(":");
        StringJoiner sj = new StringJoiner(" ");
        sj.add(date[1] + "/" + date[2] + "/" + date[0]);
        sj.add(hour[0] + ":" + hour[1] + ":" + hour[2]);
        return sj.toString();
    }

    //CONSTANTS
    static class CONSTANTS{
        public static final String HOME = "./src/main/java/templates/Home.html";
        public static final String HOTEL_DETAILS = "./src/main/java/templates/HotelDetails.html";

        public static final String EDIT_REVIEW = "./src/main/java/templates/EditReview.html";

        public static final String LOGIN = "./src/main/java/templates/Login.html";

        public static final String REGISTER = "./src/main/java/templates/Register.html";


    }

    public static void setUpDB(ThreadSafeHotelHandler hotelData, ThreadSafeReviewHandler reviewData){
        DatabaseHandler db = DatabaseHandler.getInstance();

        db.removeAllTables();
        db.CreateTables();

        StringJoiner sj = new StringJoiner(",");
        for(Hotel hotel: hotelData.getAllHotels()){
            StringJoiner joiner = new StringJoiner("\",\"", "(\"", "\")");
            joiner.add(hotel.getId());
            joiner.add(hotel.getName());
            joiner.add(hotel.getAddress());
            joiner.add(hotel.getCity());
            joiner.add(hotel.getState());
            joiner.add(hotel.getLatitude());
            joiner.add(hotel.getLongitude());
            String query = joiner.toString() ;
            sj.add(query);
        }
        db.addHotel(sj.toString());
        sj = new StringJoiner(",");
        for(Review review: reviewData.getAllReviews()){
            StringJoiner joiner = new StringJoiner("\",\"", "(\"", "\")");
            joiner.add(review.getHotelId());
            joiner.add(review.getReviewId());
            joiner.add(review.getTitle().replaceAll("\"", ""));
            joiner.add(review.getReviewText().replaceAll("\"", "'"));
            joiner.add(String.valueOf(review.getRatingOverall()));
            joiner.add(review.getUserNickname().replaceAll("\"", ""));
            joiner.add(review.getReviewSubmissionDate());
            String query = joiner.toString() ;
            sj.add(query);
        }
        db.loadReviews(sj.toString());

    }

}
