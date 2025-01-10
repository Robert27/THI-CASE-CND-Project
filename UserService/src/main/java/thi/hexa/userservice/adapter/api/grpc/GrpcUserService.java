package thi.hexa.userservice.adapter.api.grpc;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import org.springframework.beans.factory.annotation.Autowired;
import thi.hexa.userservice.domain.UserService;
import thi.hexa.userservice.usergrpc.*;


@GrpcService
public class GrpcUserService extends GrpcUserServiceGrpc.GrpcUserServiceImplBase {

    @Autowired
    UserService userService;

    @Override
    public void getUserIds(UserIdsRequest request, StreamObserver<UserIdsResponse> responseObserver){
        UserIdsResponse.Builder builder = UserIdsResponse.newBuilder();
        userService.getAllUsers().forEach(user -> {builder.addUserIds(user.getUser_id());});
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        HelloResponse response = HelloResponse.newBuilder()
                .setMessage("Hello, " + request.getName())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
