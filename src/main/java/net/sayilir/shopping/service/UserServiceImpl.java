package net.sayilir.shopping.service;

import io.grpc.stub.StreamObserver;
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
    @Override
    public void getUserDetails(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        UserDao userDao = new UserDao();
        User user = userDao.getDetails(request.getUsername());

        UserResponse.Builder userResponseBuilder = UserResponse.newBuilder()
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setName(user.getName())
                .setAge(user.getAge())
                .setGender(Gender.valueOf(user.getGender()));

        UserResponse userResponse = userResponseBuilder.build();

        responseObserver.onNext(userResponse);
        responseObserver.onCompleted();

    }
}
