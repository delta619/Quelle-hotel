package db;

public class Queries {
    public static final String INSERT_REVIEW = "INSERT INTO reviews (hotelId, reviewId, reviewTitle, reviewText, reviewRating, userNickname, reviewDate) VALUES (?, ?, ?, ?, ?, ?, ?);";
    public static final String UPDATE_REVIEW = "UPDATE Reviews SET reviewText = ?, rating = ? WHERE reviewId = ?;";
    public static final String DELETE_REVIEW = "DELETE FROM Reviews WHERE reviewId = ?;";
    public static final String GET_HOTEL_BY_ID = "SELECT * FROM hotels WHERE id = ?;";
    public static final String GET_REVIEWS_BY_HOTEL_ID  = "SELECT * FROM reviews WHERE hotelId = ?;";

    public static final String GET_REVIEWS_BY_REVIEW_ID = "SELECT * FROM reviews WHERE reviewId = ?;";
    public static final String CREATE_USER_TABLE =
            "CREATE TABLE users (" +
                    "userid INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(32) NOT NULL UNIQUE, " +
                    "password CHAR(64) NOT NULL, " +
                    "usersalt CHAR(32) NOT NULL);";

    public static final String CREATE_HOTEL_TABLE =
            "CREATE TABLE hotels (" +
                    "id VARCHAR(128) PRIMARY KEY, " +
                    "name TEXT NOT NULL , " +
                    "address TEXT , " +
                    "city TEXT , " +
                    "state TEXT , " +
                    "lat TEXT , " +
                    "lng TEXT );";

    public static final String CREATE_FAVOURITE_TABLE =
            "CREATE TABLE favourites (" +
                    "hotelId VARCHAR(128) NOT NULL, " +
                    "PRIMARY KEY (hotelId, userNickname), " +
                    "userNickname VARCHAR(128) ); ";

    public static final String ADD_TO_FAVOURITES =
            "INSERT INTO favourites (hotelId, userNickname)" +
                    " VALUES (?, ?);";

    public static final String REMOVE_FROM_FAVOURITES =
            "DELETE FROM favourites WHERE hotelId = ? AND userNickname = ?;";
    public static final String REMOVE_ALL_FAVOURITES =
            "DELETE FROM favourites WHERE userNickname = ?;";
    public static final String IS_FAVOURITE =
            "SELECT * FROM favourites WHERE hotelId = ? AND userNickname = ?;";

    public static final String INSERT_HOTEL_DATA =
            "INSERT INTO hotels (id, name, address, city, state, lat, lng) " +
                    "VALUES ";
    public static final String INSERT_REVIEW_DATA =
            "INSERT INTO reviews (hotelId, reviewId, reviewTitle, reviewText, reviewRating, userNickname, reviewDate) " +
                    "VALUES ";


    public static final String CREATE_REVIEW_TABLE =
            "CREATE TABLE reviews (" +
                    "hotelId VARCHAR(128) NOT NULL, " +
                    "reviewId VARCHAR(128) PRIMARY KEY , " +
                    "reviewTitle TEXT NOT NULL , " +
                    "reviewText TEXT NOT NULL , " +
                    "reviewRating TEXT NOT NULL , " +
                    "userNickname TEXT NOT NULL , " +
                    "reviewDate TEXT NOT NULL );";

    public static final String DROP_HOTEL_TABLE =
            "DROP TABLE hotels;";
    public static final String DROP_REVIEW_TABLE =
            "DROP TABLE reviews;";
    public static final String DROP_FAVOURITE_TABLE =
            "DROP TABLE favourites;";
    public static final String GET_FAVOURITES =
            "SELECT * FROM favourites WHERE userNickname = ?;";



    /** Used to insert a new user into the database. */
    public static final String REGISTER_SQL =
            "INSERT INTO users (username, password, usersalt) " +
                    "VALUES (?, ?, ?);";

    public static final String CHECK_EXISTING_USER_SQL =
            "SELECT username, password, usersalt FROM users WHERE username = ?";

    public static final String LOGIN_SQL =
            "SELECT username FROM users WHERE username = ? AND password = ?";

}
