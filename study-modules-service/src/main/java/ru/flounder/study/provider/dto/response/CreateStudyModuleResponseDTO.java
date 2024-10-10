package ru.flounder.study.provider.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateStudyModuleResponseDTO {
    private String status;
    private String description;
}
