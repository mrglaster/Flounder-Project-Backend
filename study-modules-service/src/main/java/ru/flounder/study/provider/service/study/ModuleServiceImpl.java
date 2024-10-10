package ru.flounder.study.provider.service.study;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import module.ModuleGenerator;
import module.ModuleServiceGrpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.flounder.study.provider.domain.Language;
import ru.flounder.study.provider.domain.ModuleStatus;
import ru.flounder.study.provider.domain.StudyModule;
import ru.flounder.study.provider.dto.request.CreateStudyModuleRequestDTO;
import ru.flounder.study.provider.dto.response.StudyModuleInfoResponseDTO;
import ru.flounder.study.provider.repository.StudyModulesRepository;
import ru.flounder.study.provider.repository.LanguageRepository;
import ru.flounder.study.provider.service.userinfo.UserInfoService;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.flounder.study.provider.util.Util.intToModuleStatus;
import static ru.flounder.study.provider.util.Util.slugify;

@Service
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    private StudyModulesRepository studyModulesRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private UserInfoService userInfoService;

    @Value("${flounder.generator_service.host}")
    private String generatorServiceHost;

    @Value("${flounder.generator_service.port}")
    private int generatorServicePort;

    @Override
    public List<StudyModuleInfoResponseDTO> findAllPaginated(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<StudyModule> modules = studyModulesRepository.findAllByIsActiveTrueOrderByCreatedAtDesc(pageable).getContent();
        return modules.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<StudyModuleInfoResponseDTO> findByTopicContaining(String substring) {
        return studyModulesRepository.findByTopicContainingIgnoreCase(substring).stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<StudyModuleInfoResponseDTO> findByAuthorId(long authorId) {
        return studyModulesRepository.findByAuthorId(authorId).stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public void createNewModule(CreateStudyModuleRequestDTO requestDTO) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(
                generatorServiceHost, generatorServicePort).usePlaintext().build();
        ModuleServiceGrpc.ModuleServiceBlockingStub stub = ModuleServiceGrpc.newBlockingStub(channel);
        String authorName = userInfoService.getUsernameById(requestDTO.getAuthorId());
        String moduleName = generateModuleName(requestDTO.getLanguage(), authorName, requestDTO.getTopic());
        String iconName = generateCoverName(moduleName, requestDTO.getIcon().length() > 7);
        addModuleToDb(requestDTO, iconName, moduleName);
        ModuleGenerator.ModuleCreationRequest creationRequest = ModuleGenerator.
                ModuleCreationRequest.newBuilder()
                    .setModuleId((int)studyModulesRepository.findByTopic(requestDTO.getTopic()).getId())
                    .setAuthor(authorName)
                .setLanguage(requestDTO.getLanguage())
                .setFileName(moduleName)
                .setIcon(requestDTO.getIcon())
                .setTitle(requestDTO.getTopic())
                .setTranslationsLanguage(requestDTO.getTranslationsLanguage())
                .addAllTranslations(requestDTO.getTranslations())
                .addAllWordlist(requestDTO.getWords())
                .build();
        ModuleGenerator.ModuleCreationResponse response = stub.createModule(creationRequest);
    }

    private void addModuleToDb(CreateStudyModuleRequestDTO requestDTO, String iconName, String moduleName){
        StudyModule module = new StudyModule();
        Language lang = new Language();
        lang.setLanguage(requestDTO.getLanguage());
        module.setLanguage(lang);
        module.setCreatedAt(LocalDateTime.now());
        module.setStatus(intToModuleStatus(0));
        module.setAuthorId(requestDTO.getAuthorId());
        module.setDisplayWords(requestDTO.getWords().toString().
                replace("[", "").replace("]", ""));
        module.setActive(false);
        module.setIconPath(iconName);
        module.setFilePath(moduleName);
        module.setTopic(requestDTO.getTopic());
        studyModulesRepository.save(module);
    }

    @Override
    public boolean updateModuleCreationStatus(long moduleId, ModuleStatus status) {
        Optional<StudyModule> moduleOptional = studyModulesRepository.findById(moduleId);
        if (moduleOptional.isPresent()) {
            StudyModule module = moduleOptional.get();
            module.setStatus(status);
            module.setActive(true);
            studyModulesRepository.save(module);
            return true;
        }
        return false;
    }

    private StudyModuleInfoResponseDTO convertToDto(StudyModule module) {
        return new StudyModuleInfoResponseDTO(
                module.getTopic(),
                module.getLanguage().getLanguage(),
                module.getIconPath(),
                module.getFilePath(),
                module.getCreatedAt(),
                userInfoService.getUsernameById(module.getAuthorId()),
                module.getDisplayWords()
        );
    }

    private String generateModuleName(String wlang, String author, String title) {
        long timestamp = Instant.now().getEpochSecond();
        String slug = slugify(String.format("%s_%s_%s_%d", wlang, author, title, timestamp), false);
        return "data/modules/" + slug + ".gsmf";
    }

    private String generateCoverName(String moduleName, boolean isDefault){
        if (isDefault){
           return Paths.get("data", "images", Paths.get(moduleName).getFileName().toString() + "_cover.png").toString();
        }
        return "/data/images/default.png";
    }




}
