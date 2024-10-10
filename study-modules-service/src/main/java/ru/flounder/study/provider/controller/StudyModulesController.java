package ru.flounder.study.provider.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.flounder.study.provider.dto.request.CreateStudyModuleRequestDTO;
import ru.flounder.study.provider.dto.request.FindModulesBySubstringDTO;
import ru.flounder.study.provider.dto.request.GetUsersModulesDTO;
import ru.flounder.study.provider.dto.response.CreateStudyModuleResponseDTO;
import ru.flounder.study.provider.dto.response.StudyModuleInfoResponseDTO;
import ru.flounder.study.provider.dto.request.UpdateModuleCreationStatusRequestDTO;
import ru.flounder.study.provider.service.study.ModuleService;
import ru.flounder.study.provider.service.token.TokenValidationService;
import ru.flounder.study.provider.util.Util;
import java.util.List;

@RestController
public class StudyModulesController {

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private TokenValidationService tokenValidationService;

    @GetMapping("/api/study/modules/latest")
    public ResponseEntity<List<StudyModuleInfoResponseDTO>> getAllBooks(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestHeader(value = "Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (tokenValidationService.validateJwtToken(token)){
                List<StudyModuleInfoResponseDTO> studyModulesDto = moduleService.findAllPaginated(page, size);
                return new ResponseEntity<>(studyModulesDto, HttpStatus.OK);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @PostMapping("/api/study/modules/new")
    public ResponseEntity<CreateStudyModuleResponseDTO> createStudyModule(@RequestBody CreateStudyModuleRequestDTO requestDTO, @RequestHeader(value = "Authorization") String authorizationHeader){
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (tokenValidationService.validateJwtToken(token)){
               moduleService.createNewModule(requestDTO);
               return ResponseEntity.status(HttpStatus.OK).body(null);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }


    @PostMapping("/api/study/modules/update/status")
    public ResponseEntity<Boolean> updateModuleCreationStatus(@RequestBody UpdateModuleCreationStatusRequestDTO requestDTO){
        boolean isUpdated = moduleService.updateModuleCreationStatus(requestDTO.getModuleId(), Util.intToModuleStatus(requestDTO.getStatus()));
        return ResponseEntity.ok(isUpdated);
    }

    @PostMapping("/api/study/modules/search")
    public ResponseEntity<List<StudyModuleInfoResponseDTO>> findStudyModules(@RequestBody FindModulesBySubstringDTO requestDTO, @RequestHeader(value = "Authorization") String authorizationHeader){
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (tokenValidationService.validateJwtToken(token)){
                if (requestDTO == null || requestDTO.getSubstring().isEmpty()){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }
                List<StudyModuleInfoResponseDTO> studyModulesDto = moduleService.findByTopicContaining(requestDTO.getSubstring());
                return new ResponseEntity<>(studyModulesDto, HttpStatus.OK);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @PostMapping("/api/study/modules/user")
    public ResponseEntity<List<StudyModuleInfoResponseDTO>> getUserStudyModules(@RequestBody GetUsersModulesDTO requestDTO, @RequestHeader(value = "Authorization") String authorizationHeader){
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (tokenValidationService.validateJwtToken(token)){
                List<StudyModuleInfoResponseDTO> studyModulesDto = moduleService.findByAuthorId(requestDTO.getUserId());
                return new ResponseEntity<>(studyModulesDto, HttpStatus.OK);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

}
