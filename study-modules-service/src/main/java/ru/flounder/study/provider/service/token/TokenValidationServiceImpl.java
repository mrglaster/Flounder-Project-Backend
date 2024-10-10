package ru.flounder.study.provider.service.token;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.stereotype.Service;
import ru.flounder.auth.grpc.TokenValidateRequest;
import ru.flounder.auth.grpc.TokenValidateResponse;
import ru.flounder.auth.grpc.TokenValidationServiceGrpc;

@Service
public class TokenValidationServiceImpl implements TokenValidationService{
    @Override
    public boolean validateJwtToken(@NotEmpty String token){
        ManagedChannel channel = ManagedChannelBuilder.forAddress(
                "127.0.0.1", 9090).usePlaintext().build();
        TokenValidationServiceGrpc.TokenValidationServiceBlockingStub stub =
                TokenValidationServiceGrpc.newBlockingStub(channel);
        TokenValidateRequest request = TokenValidateRequest.newBuilder()
                .setToken(token)
                .build();
        boolean valid = true;
        try {
            TokenValidateResponse response = stub.validateToken(request);
        } catch (Exception e) {
            valid = false;
        } finally {
            channel.shutdown();
        }
        return valid;
    }
}
