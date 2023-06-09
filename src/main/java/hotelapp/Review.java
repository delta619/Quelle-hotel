package hotelapp;

import com.google.gson.JsonObject;

public class Review implements Comparable<Review> {
    private String hotelId;
    private String reviewId;
    private double ratingOverall;
    private String title;
    private String reviewText;
    private String userNickname;
    private String reviewSubmissionDate;

//    private DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    public Review(String hotelId, String reviewId, double ratingOverall, String title, String reviewText, String userNickname, String reviewSubmissionDate){
        this.hotelId = hotelId;
        this.reviewId = reviewId;
        this.ratingOverall = ratingOverall;
        this.title = title;
        this.reviewText = reviewText;
        this.userNickname = userNickname;
        this.reviewSubmissionDate = reviewSubmissionDate;
    }

    public String getReviewId(){
        return this.reviewId;
    }

    public String getHotelId(){
        return this.hotelId;
    }

    public String[] getReviewTextWords(boolean... punctuationRemoved){

        if(punctuationRemoved.length == 1 && punctuationRemoved[0]){
            return this.reviewText.replaceAll("\\p{Punct}", " ").split(" ");
        }
        return this.reviewText.split(" ");
    }
    public String getUserNickname(){
        if(this.userNickname.isEmpty()){
            return "Anonymous";
        }
        return this.userNickname;
    }
    public String getReviewSubmissionDate(){
        return this.reviewSubmissionDate;
    }
    @Override
    public String toString() {
        String result = System.lineSeparator() + "--------------------"+System.lineSeparator();
        result += "Review by " + getUserNickname() + " on " + getReviewSubmissionDate()+System.lineSeparator();
        result += "Rating: " + getRatingOverall() + System.lineSeparator();
        result += "ReviewId: " + getReviewId() + System.lineSeparator();
        result += getTitle() + System.lineSeparator();
        result += getReviewText();

        return result;

    }

    public double getRatingOverall() {
        return ratingOverall;
    }

    public String getTitle() {
        if(this.title.isEmpty()){
            return "NA";
        }
        return title;
    }

    public String getReviewText() {
        return this.reviewText;
    }
    public void setReviewText(String reviewText){
        this.reviewText = reviewText;
    }
    public void setReviewTitle(String title){
        this.title = title;
    }

    public JsonObject toJson(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("reviewId", this.getReviewId());
        jsonObject.addProperty("title", this.getTitle());
        jsonObject.addProperty("hotelId", this.getHotelId());
        jsonObject.addProperty("user", this.getUserNickname());
        jsonObject.addProperty("ratingOverall", this.getRatingOverall());
        jsonObject.addProperty("reviewText", this.getReviewText());
        jsonObject.addProperty("date", this.getReviewSubmissionDate());
        return jsonObject;
    }
    @Override
    public int compareTo(Review o) {
        return getReviewId().compareTo(o.getReviewId());
    }
}
