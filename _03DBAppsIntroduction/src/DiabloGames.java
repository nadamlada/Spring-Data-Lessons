import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

/*
Part 2: Writing your own data retrieval application
Now it’s time for you to write a similar program. Follow the steps to write a java application that retrieves information about the users, their games and duration. We are going to use the “diablo” database from the previous course.
Example
Input	Output
nakov	User: nakov
Svetlin Nakov has played 6 games
destroyer	No such user exists

1.	 Establish a connection to the database
Get the user’s username and password and change the connection string according to the name of the database.

2.	 Username and statement
Ask the user for a username, which you will use to retrieve the desired info. Write a proper SQL statement to get the result.

3.	 Output
Consider that the input may be invalid – user with given username might not exist and you will receive an empty ResultSet. If so, print “No such user exists”, otherwise print the user_name, first and last names and the total count of games a user has played.
 */

public class DiabloGames {
    public static void main(String[] args) {
        // https://youtu.be/gWIl72jnYIU?t=10537
        // 1. read props from external property file
        Properties props = new Properties();
        String path = DiabloGames.class.getClassLoader()
                .getResource("jdbc.properties")
                .getPath();

        System.out.printf("Resource path:  %s%n", path);

        try {
            props.load(new FileInputStream(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //TODO: add meaningful defaults
        System.out.println(props);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter username (<Enter> for 'Alex'): ");
        String username = scanner.nextLine().trim();
        username = username.length() > 0 ? username : "Alex";

        // 2. try with resources - connection, prepared statement
        try (Connection con = DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password"));

             PreparedStatement ps = con.prepareStatement(
                     "SELECT u.id, first_name, last_name, COUNT(ug.game_id) count " +
                             "FROM users AS u " +
                             "JOIN users_games AS ug ON u.id = ug.user_id " +
                             "WHERE user_name = ?"
             )) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            // 3. print results
            while (rs.next()) {

                if(rs.getInt("id") == 0) {
                    System.out.printf("No such exists");
                    break;
                }

                System.out.printf("| %10d | %-15.15s | %-15.15s | %10d |%n",
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getInt("count")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}