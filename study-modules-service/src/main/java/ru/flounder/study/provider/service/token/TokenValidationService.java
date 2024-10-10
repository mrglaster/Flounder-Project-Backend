package ru.flounder.study.provider.service.token;

public interface TokenValidationService {
    boolean validateJwtToken(String token);
}
