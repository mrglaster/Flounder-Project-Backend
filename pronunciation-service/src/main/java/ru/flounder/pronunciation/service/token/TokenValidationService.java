package ru.flounder.pronunciation.service.token;

public interface TokenValidationService {
    boolean validateJwtToken(String token);
}
