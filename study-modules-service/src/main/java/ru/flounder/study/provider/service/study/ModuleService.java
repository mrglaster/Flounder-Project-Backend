package ru.flounder.study.provider.service.study;
import java.util.List;

import ru.flounder.study.provider.domain.ModuleStatus;
import ru.flounder.study.provider.dto.request.CreateStudyModuleRequestDTO;
import ru.flounder.study.provider.dto.response.StudyModuleInfoResponseDTO;

public interface ModuleService {
    List<StudyModuleInfoResponseDTO> findAllPaginated(int pageNumber, int pageSize);

    List<StudyModuleInfoResponseDTO> findByTopicContaining(String substring);

    List<StudyModuleInfoResponseDTO> findByAuthorId(long authorId);
    void createNewModule(CreateStudyModuleRequestDTO requestDTO);

    boolean updateModuleCreationStatus(long moduleId, ModuleStatus status);
}
