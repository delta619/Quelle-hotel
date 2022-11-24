package db;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.*;
import java.util.Properties;
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


    public void createTable() {
        Statement statement;
        try (Connection dbConnection = DriverManager.getConnection(uri, username, password)) {
            System.out.println("dbConnection successful");
            statement = dbConnection.createStatement();
            statement.executeUpdate(Queries.CREATE_USER_TABLE);
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
        //check if password has length > 8 and has one special character
        if (!newpass.matches(".*[!@#$%^&*()_+].*") || newpass.length() < 8) {
            throw new Exception("Password must be 8 or more characters long with a special character");
        }
        // Generate salt
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

                // insert
                statement = connection.prepareStatement(Queries.REGISTER_SQL);
                statement.setString(1, newuser);
                statement.setString(2, passhash);
                statement.setString(3, usersalt);
                statement.executeUpdate();
                statement.close();
            }
            catch(SQLException e) {
                System.out.println(e);
            }
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }

    }
    public void loginUser(String user_input, String pass_input) throws Exception {
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);

        String usersalt ;
//                = encodeHex(saltBytes, 32); // salt
        String passhash ;
//                = getHash(password, usersalt); // hashed password

        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, username, password)) {
            System.out.println("dbConnection successful");
            try {
                //check if user exists
                statement = connection.prepareStatement(Queries.CHECK_EXISTING_USER_SQL);
                statement.setString(1, user_input);
                ResultSet rs = statement.executeQuery();
                if (!rs.next()) {
                    System.out.println("User does not exist");
                    throw new Exception("User does not exist");
                }   else{
                    // print out the user's password hash and salt
                    System.out.println("User: " + rs.getString("username"));

                    passhash = rs.getString("password");
                    System.out.println("Password Hash: " + passhash);


                    usersalt = rs.getString("usersalt");
                    System.out.println("Salt: " + usersalt);
                }

                //check if password is correct by creating verifyPassHash
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

