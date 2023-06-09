package hotelapp;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class ReviewWithFreq implements Comparable<ReviewWithFreq> {

    private String word;
    private int frequency;
    private Review review;

    private Map<String, Integer> wordFrequency = new HashMap<String, Integer>();

    public ReviewWithFreq(Review review, String word){
        this.review = review;
        this.word = word;
        this.frequency = Helper.countWords(review.getReviewText(), word);
    }

    public int getFrequency() {
        return frequency;
    }

    public String getTitle() {
        return review.getTitle();
    }

    public String getNickname() {
        return review.getUserNickname();
    }

    public int getWordFrequency(String word){
        return this.wordFrequency.get(word);
    }

    public String getReviewText() {
        return review.getReviewText();
    }

    public String getWord() {
        return word;
    }

    public String getReviewId(){
        return this.review.getReviewId();
    }
    public String getReviewSubmissionDate(){
        return this.review.getReviewSubmissionDate();
    }
    public String toString() {
        String result = System.lineSeparator() + "--------------------" + System.lineSeparator();
        result += "Review by " + this.review.getUserNickname() + " on " + getReviewSubmissionDate() + System.lineSeparator();
        result += "Rating: " + this.review.getRatingOverall() + System.lineSeparator();
        result += "Hotel: " + this.review.getHotelId() + System.lineSeparator();
        result += "ReviewId: " + getReviewId() + System.lineSeparator();
        result += "Title: " + this.review.getTitle() + System.lineSeparator();
        result += "Review Text: " + getReviewText() + System.lineSeparator();
        result += "Word count of " + word + " - " + getFrequency() + System.lineSeparator();


        return result;
    }

    @Override
    public int compareTo(ReviewWithFreq o) {
        return getReviewText().compareTo(o.getReviewText());
    }

    public JsonObject toJson(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("reviewId", getReviewId());
        jsonObject.addProperty("title", getTitle());
        jsonObject.addProperty("user", getNickname());
        jsonObject.addProperty("reviewText", getReviewText());
        jsonObject.addProperty("date", getReviewSubmissionDate());
        return jsonObject;
    }
}
