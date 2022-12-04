package db;

public class Queries {

    public static final String INSERT_HISTORY = "INSERT INTO history (userNickname, hotelId, hotelName, timestamp) VALUES (?, ?, ?, ?);";
    public static final String GET_HISTORY_OF_USER = "SELECT * FROM history WHERE userNickname = ? ORDER BY timestamp DESC;";


    public static final String GET_REVIEWS_COUNT_USING_HOTEL_ID = "SELECT COUNT(*) as totalReviewsCount FROM reviews WHERE hotelId = ?";
    public static final String GET_AVG_RATING = "SELECT avg(reviewRating) as avgRating from reviews where hotelId = ?;";
    public static final String INSERT_REVIEW = "INSERT INTO reviews (hotelId, reviewId, reviewTitle, reviewText, reviewRating, userNickname, reviewDate) VALUES (?, ?, ?, ?, ?, ?, ?);";
    public static final String UPDATE_REVIEW = "UPDATE reviews SET reviewTitle = ?, reviewText = ?, reviewRating = ? WHERE reviewId = ?;";
    public static final String DELETE_REVIEW = "DELETE FROM reviews WHERE reviewId = ?;";




    public static final String GET_HOTEL_BY_ID = "SELECT * FROM hotels WHERE id = ?;";
    public static final String GET_REVIEWS_BY_HOTEL_ID = "SELECT * FROM reviews WHERE hotelId = ? ORDER BY reviewDate desc  LIMIT ? OFFSET ?;";




    public static final String GET_REVIEW_USING_REVIEW_ID = "SELECT * FROM reviews WHERE reviewId = ?;";
    public static final String CREATE_USER_TABLE =
            "CREATE TABLE IF NOT EXISTS users (" +
                    "userid INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(32) NOT NULL UNIQUE, " +
                    "password CHAR(64) NOT NULL, " +
                    "usersalt CHAR(32) NOT NULL);";

    public static final String CREATE_HOTEL_TABLE =
            "CREATE TABLE IF NOT EXISTS hotels (" +
                    "id VARCHAR(128) PRIMARY KEY, " +
                    "name TEXT NOT NULL , " +
                    "address TEXT , " +
                    "city TEXT , " +
                    "state TEXT , " +
                    "lat TEXT , " +
                    "lng TEXT );";
    public static final String CREATE_REVIEW_TABLE =
            "CREATE TABLE IF NOT EXISTS reviews (" +
                    "hotelId VARCHAR(128) NOT NULL, " +
                    "reviewId VARCHAR(128) PRIMARY KEY , " +
                    "reviewTitle TEXT NOT NULL , " +
                    "reviewText TEXT NOT NULL , " +
                    "reviewRating int NOT NULL , " +
                    "userNickname TEXT NOT NULL , " +
                    "reviewDate TEXT NOT NULL );";


    public static final String CREATE_FAVOURITE_TABLE =
            "CREATE TABLE IF NOT EXISTS favourites (" +
                    "hotelId VARCHAR(128) NOT NULL, " +
                    "PRIMARY KEY (hotelId, userNickname), " +
                    "userNickname VARCHAR(128) ); ";
    public static final String CREATE_HISTORY_TABLE = "CREATE TABLE IF NOT EXISTS history ("
            + "userNickname VARCHAR(256),"
            + "hotelId TEXT,"
            + "hotelName TEXT,"
            + "timestamp VARCHAR(256)"
            + ");";


    public static final String ADD_TO_FAVOURITES =
            "INSERT INTO favourites (hotelId, userNickname)" +
                    " VALUES (?, ?);";
    public static final String REMOVE_FROM_FAVOURITES =
            "DELETE FROM favourites WHERE hotelId = ? AND userNickname = ?;";
    public static final String REMOVE_ALL_FAVOURITES =
            "DELETE FROM favourites WHERE userNickname = ?;";
    public static final String IS_FAVOURITE =
            "SELECT * FROM favourites WHERE hotelId = ? AND userNickname = ?;";
    public static final String GET_FAVOURITES =
            "SELECT * FROM favourites WHERE userNickname = ?;";






    public static final String INSERT_HOTEL_DATA =
            "INSERT INTO hotels (id, name, address, city, state, lat, lng) " +
                    "VALUES ";
    public static final String INSERT_REVIEW_DATA =
            "INSERT INTO reviews (hotelId, reviewId, reviewTitle, reviewText, reviewRating, userNickname, reviewDate) " +
                    "VALUES ";




    public static final String DROP_HOTEL_TABLE =
            "DROP TABLE hotels;";
    public static final String DROP_REVIEW_TABLE =
            "DROP TABLE reviews;";
    public static final String DROP_FAVOURITE_TABLE =
            "DROP TABLE favourites;";
    public static final String DROP_HISTORY_TABLE =
            "DROP TABLE history;";
    public static final String DROP_USER_TABLE =
            "DROP TABLE users;";

    /** Used to insert a new user into the database. */
    public static final String REGISTER_SQL =
            "INSERT INTO users (username, password, usersalt) " +
                    "VALUES (?, ?, ?);";

    public static final String CHECK_EXISTING_USER_SQL =
            "SELECT username, password, usersalt FROM users WHERE username = ?";

    public static final String LOGIN_SQL =
            "SELECT username FROM users WHERE username = ? AND password = ?";

}
