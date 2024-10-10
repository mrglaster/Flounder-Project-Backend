package ru.flounder.pronunciation.service.pronunciation;
import ru.flounder.pronunciation.dto.request.EnglishPronunciationRequest;

public interface PronunciationService {
    int processEnglishAssessment(EnglishPronunciationRequest request);
}
