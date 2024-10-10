package ru.flounder.study.provider.dto.request;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class StartModuleCreationRequestDTO {
    @JsonProperty("file_name")
    private String fileName;

    @JsonProperty("author")
    private String author;

    @JsonProperty("title")
    private String title;

    @JsonProperty("language")
    private String language;

    @JsonProperty("wordlist")
    private List<String> wordlist;

    @JsonProperty("translations")
    private List<String> translations;

    @JsonProperty("translations_language")
    private String translationsLanguage;

    @JsonProperty("icon")
    private String icon;

    @JsonProperty("module_id")
    private long moduleId;

}
