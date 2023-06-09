package db;

public class Queries {

    public static final String CREATE_USER_TABLE =
            "CREATE TABLE users (" +
                    "userid INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(32) NOT NULL UNIQUE, " +
                    "password CHAR(64) NOT NULL, " +
                    "usersalt CHAR(32) NOT NULL);";

    /** Used to insert a new user into the database. */
    public static final String REGISTER_SQL =
            "INSERT INTO users (username, password, usersalt) " +
                    "VALUES (?, ?, ?);";

    public static final String CHECK_EXISTING_USER_SQL =
            "SELECT username, password, usersalt FROM users WHERE username = ?";

    public static final String LOGIN_SQL =
            "SELECT username FROM users WHERE username = ? AND password = ?";

}
