package ru.flounder.study.provider.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor

public class CreateStudyModuleRequestDTO {
    @NotNull
    @JsonProperty("author_id")
    private long authorId;

    @NotBlank
    @JsonProperty("topic")
    private String topic;

    @NotBlank
    @JsonProperty("language")
    private String language;

    @NotNull
    @JsonProperty("words")
    private List<String> words;

    @JsonProperty("translations")
    private List<String> translations;

    @JsonProperty("translations_language")
    private String translationsLanguage;

    @JsonProperty("icon")
    private String icon;
}
