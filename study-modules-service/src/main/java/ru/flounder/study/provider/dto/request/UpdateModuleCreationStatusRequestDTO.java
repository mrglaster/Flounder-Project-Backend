package ru.flounder.study.provider.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateModuleCreationStatusRequestDTO {

    @JsonProperty("status")
    private int status;

    @JsonProperty("module_id")
    private long moduleId;
}
