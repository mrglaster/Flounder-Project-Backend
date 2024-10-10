package ru.flounder.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {
    @NotBlank
    @JsonProperty("username")
    private String username;

    @NotBlank
    @JsonProperty("password")
    private String password;
}
