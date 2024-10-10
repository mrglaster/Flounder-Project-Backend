package ru.flounder.study.provider.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FindModulesBySubstringDTO {
    @JsonProperty("substring")
    private String substring;

}
