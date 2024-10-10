package ru.flounder.pronunciation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientPronunciationReportDTO {
    @JsonProperty("score")
    private int score;
}
