package ru.flounder.pronunciation.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class EnglishPronunciationRequest {
    @JsonProperty("word")
    @NotNull
    @NotBlank
    private String word;

    @NotNull
    @NotBlank
    @JsonProperty("audio_format")
    private String audioFormat;

    @NotNull
    private MultipartFile audioFile;
}
