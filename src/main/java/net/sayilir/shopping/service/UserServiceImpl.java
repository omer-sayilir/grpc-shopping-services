package net.sayilir.shopping.service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import net.sayilir.shopping.client.OrderClient;
import net.sayilir.shopping.db.User;
import net.sayilir.shopping.db.UserDao;
import net.sayilir.stubs.user.Gender;
import net.sayilir.stubs.user.UserRequest;
import net.sayilir.stubs.user.UserResponse;
import net.sayilir.stubs.user.UserServiceGrpc;
import net.sayilir.stubs.order.Order;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author omersayilir
 * @Date 2024-09-06
 */
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {
    private Logger logger = Logger.getLogger(UserServiceImpl.class.getName());
    private final UserDao userDao = new UserDao();

    @Override
    public void getUserDetails(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        User user = userDao.getDetails(request.getUsername());

        UserResponse.Builder userResponseBuilder = UserResponse.newBuilder()
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setName(user.getName())
                .setAge(user.getAge())
                .setGender(Gender.valueOf(user.getGender()));

        List <Order> orders = getOrders(userResponseBuilder);

        UserResponse userResponse = userResponseBuilder.build();
        userResponseBuilder.setNoOfOrders(orders.size());
        responseObserver.onNext(userResponse);
        responseObserver.onCompleted();

    }

    private List<Order> getOrders(UserResponse.Builder userResponseBuilder) {
        // get orders by invoking the Order client
        ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:5002")
                .usePlaintext().build();
        OrderClient orderClient = new OrderClient(channel);
        List<Order> orders= orderClient.getOrders(userResponseBuilder.getId());

        try {
            channel.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Could not stop server", e);
        }
        return orders;
    }
}
