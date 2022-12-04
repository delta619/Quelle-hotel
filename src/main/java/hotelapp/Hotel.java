package hotelapp;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class Hotel implements Comparable<Hotel> {

    @SerializedName("f")
    private String name;
    private String id;
    @SerializedName("ad")
    private String address;
    @SerializedName("ci")
    private String city;

    @SerializedName("pr")
    private String state;

    private LL ll;
    class LL {
        public String lat;
        public String lng;
    }
    public Hotel(String name, String id, String address, Double lat, Double lng, String city, String state){
        this.name = name;
        this.id = id;
        this.address = address;
        this.city = city;
        this.state = state;

        this.ll = new LL();
        this.ll.lat = String.valueOf(lat);
        this.ll.lng = String.valueOf(lng);


    }

    public String getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }
    public String  getLatitude(){
        return this.ll.lat;
    }
    public String getLongitude(){
        return this.ll.lng;
    }
    public String getAddress(){
        return this.address;
    }
    public String getCity() {
        return city;
    }
    public String getState() {
        return state;
    }
    @Override
    public int compareTo(Hotel hotel) {
        return getId().compareTo(hotel.getId());
    }
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("hotelId", id);
        json.addProperty("name", name);
        json.addProperty("addr", address);
        json.addProperty("city", city);
        json.addProperty("state", state);
        json.addProperty("lat", ll.lat);
        json.addProperty("lng", ll.lng);
        return json;
    }

    @Override
    public String toString() {
        String result = System.lineSeparator() + "********************";
        result += System.lineSeparator() + getName() + ": " + getId();
        result += System.lineSeparator() + getAddress();
        result += System.lineSeparator() + getCity() + ", " + getState();
        return result;
//        ********************
//        Hilton San Francisco Union Square: 25622
//        333 O'Farrell St.
//        San Francisco, CA

    }
}
