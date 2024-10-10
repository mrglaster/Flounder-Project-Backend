package ru.flounder.study.provider.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
public class StudyModuleInfoResponseDTO {
    private String topic;
    private String language;
    private String iconPath;
    private String filePath;
    private LocalDateTime createdAt;
    private String authorName;
    private String displayWords;
}
