package db;

import hotelapp.Hotel;
import hotelapp.Review;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;


public class DatabaseHandler {

    private static DatabaseHandler dbHandler = new DatabaseHandler(); // singleton pattern
    private String uri = null; // uri to connect to mysql using jdbc
    private Random random = new Random(); // used in password  generation

    private static final String host = "localhost";
    private static final String username = "user005";
    private static final String password = "password";
    private static final String port = "3307";


    /**
     * DataBaseHandler is a singleton, we want to prevent other classes
     * from creating objects of this class using the constructor
     */
    private DatabaseHandler(){
        this.uri = "jdbc:mysql://"+ host + ": " + port + "/" + username + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    }

    /**
     * Returns the instance of the database handler.
     * @return instance of the database handler
     */
    public static DatabaseHandler getInstance() {
        return dbHandler;
    }


    public void CreateTables() {
        Statement statement;
        try (Connection dbConnection = DriverManager.getConnection(uri, username, password)) {
            System.out.println("CREATING TABLES");

            statement = dbConnection.createStatement();
            statement.executeUpdate(Queries.CREATE_HOTEL_TABLE);

            statement = dbConnection.createStatement();
            statement.executeUpdate(Queries.CREATE_REVIEW_TABLE);

            statement = dbConnection.createStatement();
            statement.executeUpdate(Queries.CREATE_FAVOURITE_TABLE);

        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public void addHotel(String hotels) {
        Statement statement = null;

        try (Connection dbConnection = DriverManager.getConnection(uri, username, password)) {
            System.out.println("INSERTING HOTELS");

            statement = dbConnection.createStatement();
            statement.executeUpdate(Queries.INSERT_HOTEL_DATA + hotels + ";");

        }
        catch (SQLException ex) {
            System.out.println(statement.toString());
            System.out.println(ex);
        }
    }

    public void loadReviews(String reviews) {
        try (Connection dbConnection = DriverManager.getConnection(uri, username, password)) {
            Statement statement = dbConnection.createStatement();
            System.out.println("INSERTING REVIEWS");
            statement.executeUpdate(Queries.INSERT_REVIEW_DATA + reviews + ";");

        }
        catch (SQLException ex) {
            System.out.println(ex);
        }

    }

    public void addReview(String hotelId, String reviewId, String reviewTitle, String reviewText, String reviewRating, String reviewUsername, String reviewDate) {
        try (Connection dbConnection = DriverManager.getConnection(uri, username, password)) {
            PreparedStatement statement = dbConnection.prepareStatement(Queries.INSERT_REVIEW);
            statement.setString(1, hotelId);
            statement.setString(2, reviewId);
            statement.setString(3, reviewTitle);
            statement.setString(4, reviewText);
            statement.setString(5, reviewRating);
            statement.setString(6, reviewUsername);
            statement.setString(7, reviewDate);

            System.out.println(statement.toString());

            statement.executeUpdate();

        }
        catch (SQLException ex) {

            System.out.println(ex);
            throw new RuntimeException(ex);
        }

    }
    public boolean deleteReview(String reviewId) {
        try (Connection dbConnection = DriverManager.getConnection(uri, username, password)) {
            PreparedStatement statement = dbConnection.prepareStatement(Queries.DELETE_REVIEW);
            statement.setString(1, reviewId);
            statement.executeUpdate();
            return true;
        }
        catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }
    }

    public boolean updateReview(String reviewId, String reviewTitle, String reviewText, String rating) {
        try (Connection dbConnection = DriverManager.getConnection(uri, username, password)) {
            PreparedStatement statement = dbConnection.prepareStatement(Queries.UPDATE_REVIEW);
            statement.setString(1, reviewTitle);
            statement.setString(2, reviewText);
            statement.setString(3, rating);
            statement.setString(4, reviewId);
            statement.executeUpdate();
            return true;
        }
        catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }
    }

    public boolean isFavourite(String hotelId, String userNickname) {
        try (Connection dbConnection = DriverManager.getConnection(uri, username, password)) {
            PreparedStatement statement = dbConnection.prepareStatement(Queries.IS_FAVOURITE);
            statement.setString(1, hotelId);
            statement.setString(2, userNickname);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return true;
            }
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
        return false;
    }
    public void addFavourite(String hotelId, String userNickname) {
        try (Connection dbConnection = DriverManager.getConnection(uri, username, password)) {
            PreparedStatement statement = dbConnection.prepareStatement(Queries.ADD_TO_FAVOURITES);

            statement.setString(1, hotelId);
            statement.setString(2, userNickname);
            statement.executeUpdate();
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    public void removeFavourite(String hotelId, String userNickname) {
        try (Connection dbConnection = DriverManager.getConnection(uri, username, password)) {
            PreparedStatement statement = dbConnection.prepareStatement(Queries.REMOVE_FROM_FAVOURITES);
            statement.setString(1, hotelId);
            statement.setString(2, userNickname);
            statement.executeUpdate();
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    public void removeAllFavourites(String userNickname) {
        try (Connection dbConnection = DriverManager.getConnection(uri, username, password)) {
            PreparedStatement statement = dbConnection.prepareStatement(Queries.REMOVE_ALL_FAVOURITES);
            statement.setString(1, userNickname);
            statement.executeUpdate();
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    public ArrayList<String> getFavHotels(String userNickname) {
        PreparedStatement statement = null;

        try (Connection dbConnection = DriverManager.getConnection(uri, username, password)) {
            statement = dbConnection.prepareStatement(Queries.GET_FAVOURITES);
            statement.setString(1, userNickname);
            ResultSet rs = statement.executeQuery();

            ArrayList<String> favHotels = new ArrayList<>();

            while (rs.next()) {
                favHotels.add(rs.getString("hotelId"));
            }

            return favHotels;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }
    public Hotel getHotel(String hotelId) {
        PreparedStatement statement = null;

        try (Connection dbConnection = DriverManager.getConnection(uri, username, password)) {
            statement = dbConnection.prepareStatement(Queries.GET_HOTEL_BY_ID);
            statement.setString(1, hotelId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                String hotelName = rs.getString("name");
                String address = rs.getString("address");
                String city = rs.getString("city");
                String state = rs.getString("state");
                Double latitude = rs.getDouble("lat");
                Double longitude = rs.getDouble("lng");

                return new Hotel(hotelName, hotelId, address, latitude, longitude, city, state);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public int getReviewsCountUsingHotelId(String hotelId) {
        PreparedStatement statement = null;

        try (Connection dbConnection = DriverManager.getConnection(uri, username, password)) {
            statement = dbConnection.prepareStatement(Queries.GET_REVIEWS_COUNT_USING_HOTEL_ID);
            statement.setString(1, hotelId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getInt("totalReviewsCount");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
    public ArrayList<Review> getReviewsUsingHotelId(String hotelId, int offset, int limit) {
        PreparedStatement statement = null;

        try (Connection dbConnection = DriverManager.getConnection(uri, username, password)) {
            statement = dbConnection.prepareStatement(Queries.GET_REVIEWS_BY_HOTEL_ID);

            statement.setString(1, hotelId);
            statement.setInt(2, limit);
            statement.setInt(3, offset);
            ResultSet rs = statement.executeQuery();
            ArrayList<Review> reviews = new ArrayList<>();

            while (rs.next()) {
                String reviewId = rs.getString("reviewId");
                String userNickname = rs.getString("userNickname");
                String reviewTitle = rs.getString("reviewTitle");
                String reviewText = rs.getString("reviewText");
                String reviewDate = rs.getString("reviewDate");
                int reviewOverall = rs.getInt("reviewRating");

                reviews.add(new Review(hotelId, reviewId, reviewOverall, reviewTitle, reviewText, userNickname, reviewDate));
            }
            return reviews;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public double getAvgRating(String hotelId) {
        PreparedStatement statement = null;

        try (Connection dbConnection = DriverManager.getConnection(uri, username, password)) {
            statement = dbConnection.prepareStatement(Queries.GET_AVG_RATING);
            statement.setString(1, hotelId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getDouble("avgRating");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
    public Review getReviewUsingReviewId(String reviewId) {
        PreparedStatement statement = null;

        try (Connection dbConnection = DriverManager.getConnection(uri, username, password)) {
            statement = dbConnection.prepareStatement(Queries.GET_REVIEW_USING_REVIEW_ID);

            statement.setString(1, reviewId);
            ResultSet rs = statement.executeQuery();
            Review review = null;
            while (rs.next()) {
                String hotelId = rs.getString("hotelId");
                String userNickname = rs.getString("userNickname");
                String reviewTitle = rs.getString("reviewTitle");
                String reviewText = rs.getString("reviewText");
                String reviewDate = rs.getString("reviewDate");
                int reviewRating = rs.getInt("reviewRating");

                review =  new Review(hotelId, reviewId, reviewRating, reviewTitle, reviewText, userNickname, reviewDate);
                System.out.println(review);
                return review;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);

        }
        return null;
    }

    public void removeAllTables(){
        Statement statement;
        try (Connection dbConnection = DriverManager.getConnection(uri, username, password)) {
            System.out.println("initial dbConnection successful");

            statement = dbConnection.createStatement();
            statement.executeUpdate(Queries.DROP_HOTEL_TABLE);

            statement = dbConnection.createStatement();
            statement.executeUpdate(Queries.DROP_REVIEW_TABLE);

            statement = dbConnection.createStatement();
            statement.executeUpdate(Queries.DROP_FAVOURITE_TABLE);

        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    /**
     * Returns the hex encoding of a byte array.
     *
     * @param bytes - byte array to encode
     * @param length - desired length of encoding
     * @return hex encoded byte array
     */
    public static String encodeHex(byte[] bytes, int length) {
        BigInteger bigint = new BigInteger(1, bytes);
        String hex = String.format("%0" + length + "X", bigint);

        assert hex.length() == length;
        return hex;
    }
    /**
     * Calculates the hash of a password and salt using SHA-256.
     *
     * @param password - password to hash
     * @param salt - salt associated with user
     * @return hashed password
     */
    public static String getHash(String password, String salt) {
        String salted = salt + password;
        String hashed = salted;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salted.getBytes());
            hashed = encodeHex(md.digest(), 64);
        }
        catch (Exception ex) {
            System.out.println(ex);
        }

        return hashed;
    }

    /**
     * Registers a new user, placing the username, password hash, and
     * salt into the database.
     *
     * @param newuser - username of new user
     * @param newpass - password of new user
     */
    public void registerUser(String newuser, String newpass) throws Exception{
        if (!newpass.matches("^(?=.*[@$!%#()*?&])[A-Za-z\\d@$!()%*?#&]{8,}$")) {
            throw new Exception("Password must be 8 or more characters long with a special character");
        }
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);

        String usersalt = encodeHex(saltBytes, 32); // salt
        String passhash = getHash(newpass, usersalt); // hashed password
        System.out.println(usersalt);

        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, username, password)) {
            System.out.println("dbConnection successful");
            try {
                statement = connection.prepareStatement(Queries.CHECK_EXISTING_USER_SQL);
                statement.setString(1, newuser);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    System.out.println("User already exists");
                    throw new Exception("User already exists");
                }

                statement = connection.prepareStatement(Queries.REGISTER_SQL);
                statement.setString(1, newuser);
                statement.setString(2, passhash);
                statement.setString(3, usersalt);
                statement.executeUpdate();
                statement.close();
            }
            catch(SQLException e) {
                System.out.println(e);
                throw new Exception("Something went wrong");

            }
        }
        catch (SQLException ex) {
            System.out.println(ex);
            throw new Exception("Error connecting to server");

        }

    }
    public void loginUser(String user_input, String pass_input) throws Exception {
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);

        String usersalt ;
        String passhash ;

        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, username, password)) {
            System.out.println("dbConnection successful");
            try {
                statement = connection.prepareStatement(Queries.CHECK_EXISTING_USER_SQL);
                statement.setString(1, user_input);
                ResultSet rs = statement.executeQuery();
                if (!rs.next()) {
                    System.out.println("User does not exist");
                    throw new Exception("User does not exist");
                }   else{
                    System.out.println("User: " + rs.getString("username"));

                    passhash = rs.getString("password");
                    System.out.println("Password Hash: " + passhash);


                    usersalt = rs.getString("usersalt");
                    System.out.println("Salt: " + usersalt);
                }

                String verifyPassHash = getHash(pass_input, usersalt);

                statement = connection.prepareStatement(Queries.LOGIN_SQL);
                statement.setString(1, user_input);
                statement.setString(2, verifyPassHash);
                ResultSet rs2 = statement.executeQuery();
                if (rs2.next()) {
                    System.out.println(username + " : Login successful");
                }
                else {
                    System.out.println(username + " : Incorrect password");
                    throw new Exception("Incorrect password");
                }
            }
            catch(SQLException e) {
                System.out.println(e);
                throw new Exception(e.getMessage());
            }

        }
    }
//    public static void main(String[] args) {
//        DatabaseHandler dhandler = DatabaseHandler.getInstance();
//        dhandler.createTable();
//        System.out.println("created a user table ");
//        dhandler.registerUser("luke", "lukeS1k23w");
//        dhandler.registerUser("alex", "cat123");
//        System.out.println("Registered alex.");
//    }




}

