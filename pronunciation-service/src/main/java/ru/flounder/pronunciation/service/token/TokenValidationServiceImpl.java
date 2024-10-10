package ru.flounder.pronunciation.service.token;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.stereotype.Service;

@Service
public class TokenValidationServiceImpl implements TokenValidationService {
    @Override
    public boolean validateJwtToken(@NotEmpty String token){
        ManagedChannel channel = ManagedChannelBuilder.forAddress(
                "127.0.0.1", 9090).usePlaintext().build();
        ru.flounder.auth.grpc.TokenValidationServiceGrpc.TokenValidationServiceBlockingStub stub =
                ru.flounder.auth.grpc.TokenValidationServiceGrpc.newBlockingStub(channel);
        ru.flounder.auth.grpc.TokenValidateRequest request = ru.flounder.auth.grpc.TokenValidateRequest.newBuilder()
                .setToken(token)
                .build();
        boolean valid = true;
        try {
            ru.flounder.auth.grpc.TokenValidateResponse response = stub.validateToken(request);
        } catch (Exception e) {
            valid = false;
        } finally {
            channel.shutdown();
        }
        return valid;
    }
}
