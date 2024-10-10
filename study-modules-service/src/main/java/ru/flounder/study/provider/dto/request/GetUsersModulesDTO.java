package ru.flounder.study.provider.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetUsersModulesDTO {
    @JsonProperty("user_id")
    private long userId;
}
