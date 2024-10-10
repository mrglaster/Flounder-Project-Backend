package ru.flounder.auth.grpc;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import ru.flounder.auth.grpc.TokenValidateResponse;
import ru.flounder.auth.grpc.TokenValidateRequest;
import ru.flounder.auth.grpc.TokenValidationServiceGrpc.TokenValidationServiceImplBase;
import io.grpc.stub.StreamObserver;
import ru.flounder.auth.security.jwt.JwtUtils;

@GrpcService
public class TokenValidationServiceImpl extends TokenValidationServiceImplBase{
    @Autowired
    private JwtUtils jwtUtils;
    @Override
    public void validateToken(TokenValidateRequest request, StreamObserver<TokenValidateResponse> responseObserver) {
        TokenValidateResponse response = TokenValidateResponse.newBuilder().setValid(jwtUtils.validateJwtToken(request.getToken())).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
