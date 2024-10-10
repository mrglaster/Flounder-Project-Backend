package ru.flounder.study.provider.service.userinfo;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.flounder.study.service.UserInfoServiceGrpc;
import ru.flounder.study.service.UserRequest;
import ru.flounder.study.service.UserResponse;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Value("${flounder.user_service.host}")
    private String userServiceHost;

    @Value("${flounder.user_service.port}")
    private int userServicePort;

    @Override
    public String getUsernameById(long id) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(
                userServiceHost, userServicePort).usePlaintext().build();
        UserInfoServiceGrpc.UserInfoServiceBlockingStub stub = UserInfoServiceGrpc.newBlockingStub(channel);
        UserRequest request = UserRequest.newBuilder().setUserId(id).build();
        UserResponse response = stub.getUserName(request);
        return response.getUserName();
    }
}
