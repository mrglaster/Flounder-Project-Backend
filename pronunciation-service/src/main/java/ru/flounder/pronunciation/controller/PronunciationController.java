package ru.flounder.pronunciation.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.flounder.pronunciation.dto.request.EnglishPronunciationRequest;
import ru.flounder.pronunciation.dto.response.ClientPronunciationReportDTO;
import ru.flounder.pronunciation.service.pronunciation.PronunciationService;
import ru.flounder.pronunciation.service.token.TokenValidationService;

import java.util.Objects;

@RestController
public class PronunciationController {
    @Autowired
    private TokenValidationService tokenValidationService;

    @Autowired
    private PronunciationService pronunciationService;

    @PostMapping("/api/study/pronunciation/en/eval")
    public ResponseEntity<ClientPronunciationReportDTO> handlePronunciationEn(@RequestBody EnglishPronunciationRequest request, @RequestHeader(value = "Authorization") String authorizationHeader){

        if (request.getAudioFile().isEmpty() || !Objects.equals(request.getAudioFile().getContentType(), "audio/wav")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            int englishScore = pronunciationService.processEnglishAssessment(request);
            if (tokenValidationService.validateJwtToken(token)){
                return ResponseEntity.ok(new ClientPronunciationReportDTO(englishScore));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
}
