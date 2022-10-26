import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class DiabloGamesVar2 {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "");

        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/diablo", properties);

        System.out.println("Enter username: ");
        String username = scanner.nextLine();

        PreparedStatement query = connection.prepareStatement(
                "SELECT user_name, first_name, last_name, COUNT(ug.game_id) AS games_count " +
                        "FROM users AS u " +
                        "JOIN users_games AS ug ON u.id = ug.user_id " +
                        "WHERE user_name = ?");
        query.setString(1, username);
        ResultSet result = query.executeQuery();

        while (result.next()) {
            //valid username
            String dbUsername = result.getString("user_name");

            if (dbUsername == null) {
                System.out.printf("No such exists");
                break;
            }

            String dbFirstName = result.getString("first_name");
            String dbLastName = result.getString("last_name");

            // ако е null връща нула
            int dbGamesCount = result.getInt("games_count");

            System.out.printf("User: %s%n%s %s has played %d games",
                    dbUsername, dbFirstName, dbLastName, dbGamesCount);
        }
    }
}