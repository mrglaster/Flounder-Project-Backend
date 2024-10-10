package ru.flounder.pronunciation.service.pronunciation;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.flounder.pronunciation.dto.request.EnglishPronunciationRequest;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@Service
public class PronunciationServiceImpl implements PronunciationService {
    private static final OkHttpClient client = new OkHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${flounder.pronunciation.en.pronunciation_assessment.host}")
    private String englishAssessmentHost;

    @Value("${flounder.pronunciation.en.pronunciation_assessment.key}")
    private String englishAssessmentKey;

    @Override
    public int processEnglishAssessment(EnglishPronunciationRequest request) {
        try {
            String audioBase64 = Base64.getEncoder().encodeToString(request.getAudioFile().getBytes());
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("audio_base64", audioBase64)
                    .addFormDataPart("audio_format", request.getAudioFormat())
                    .addFormDataPart("text", request.getWord())
                    .build();
            Request httpRequest = new Request.Builder()
                    .url(englishAssessmentHost)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("X-RapidAPI-Key", englishAssessmentKey)
                    .build();
            Response response = client.newCall(httpRequest).execute();
            if (response.isSuccessful()) {
                assert response.body() != null;
                String responseBody = response.body().string();
                Map<String, Object> scoreEstimates  = (Map<String, Object>)objectMapper.readValue(responseBody, Map.class).get("score_estimates");
                return (int) scoreEstimates.get("ielts");
            } else {
                return -1;
            }
        } catch (IOException e) {
            return -1;
        }
    }
}
