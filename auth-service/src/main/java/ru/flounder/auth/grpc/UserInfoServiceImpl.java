package ru.flounder.auth.grpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import ru.flounder.auth.domain.User;
import ru.flounder.auth.repository.UserRepository;
import ru.flounder.study.service.UserInfoServiceGrpc;
import ru.flounder.study.service.UserRequest;
import ru.flounder.study.service.UserResponse;

import java.util.Optional;

@GrpcService
public class UserInfoServiceImpl extends UserInfoServiceGrpc.UserInfoServiceImplBase {
    private final CacheManager cacheManager;

    public UserInfoServiceImpl(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Autowired
    private UserRepository userRepository;

    @Override
    public void getUserName(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        Cache cache = cacheManager.getCache("userCache");
        assert cache != null;
        String cachedName = cache.get(request.getUserId(), String.class);
        if (cachedName == null) {
            Optional<User> user  = userRepository.findById(request.getUserId());
            if (user.isEmpty()) cachedName = "";
            else {
                cachedName = user.get().getUsername();
                cache.put(request.getUserId(), cachedName);
            }
        }
        UserResponse response = UserResponse.newBuilder().setUserName(cachedName).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }
}
