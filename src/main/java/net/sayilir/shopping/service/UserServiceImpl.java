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

/**
 * @author omersayilir
 * @Date 2024-09-06
 */
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {
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

        // get order by invoking the Order client
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext().build();
        OrderClient orrderClient = new OrderClient(channel);

        orrderClient.getOrders(user.getId());


        UserResponse userResponse = userResponseBuilder.build();

        responseObserver.onNext(userResponse);
        responseObserver.onCompleted();

    }
}
