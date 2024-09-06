package net.sayilir.shopping.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author omersayilir
 * @Date 2024-09-06
 */
public class OrderDao {
    private static final Logger logger = Logger.getLogger(OrderDao.class.getName());

    public List<Order> getOrderDetails(int userId) {
        Connection connection = null;
        List<Order> orderList = new ArrayList<>();
        try {
            connection = H2DatabaseConnection.getConnectionToDatabase();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select * from order where user_id = ?");
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Order order = new Order();
                order.setOrderId(resultSet.getInt("order_id"));
                order.setUserId(resultSet.getInt("user_id"));
                order.setNoOfItems(resultSet.getInt("no_of_items"));
                order.setTotalAmount(resultSet.getDouble("total_amount"));
                order.setOrderDate(resultSet.getDate("order_at"));
                orderList.add(order);
            }

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Could not execute query", ex);
        }


        return orderList;
    }
}

