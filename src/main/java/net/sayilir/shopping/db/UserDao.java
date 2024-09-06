package net.sayilir.shopping.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author omersayilir
 * @Date 2024-09-06
 */
public class UserDao {
    private static final Logger logger = Logger.getLogger(UserDao.class.getName());

    public User getDetails(String username) {
        User user = new User();

        try {
            Connection connection = H2DatabaseConnection.getConnectionToDatabase();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select * from user where username = ?");
            preparedStatement.setString(1, username);
            ResultSet resultset = preparedStatement.executeQuery();
            while (resultset.next()) {
                user.setId(resultset.getInt("id"));
                user.setUsername(resultset.getString("username"));
                user.setName(resultset.getString("name"));
                user.setAge(resultset.getInt("age"));
                user.setGender(resultset.getString("gender"));
            }

        } catch (Exception exception) {
            logger.log(Level.SEVERE, "Could not set up connection", exception);
        }
        return user;
    }
}
