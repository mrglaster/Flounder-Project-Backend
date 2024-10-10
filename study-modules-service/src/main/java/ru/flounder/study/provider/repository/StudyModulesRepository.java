package ru.flounder.study.provider.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.flounder.study.provider.domain.StudyModule;

import java.util.List;

public interface StudyModulesRepository extends JpaRepository<StudyModule, Long> {
    Page<StudyModule> findAllByIsActiveTrueOrderByCreatedAtDesc(Pageable pageable);
    StudyModule findByTopic(String topic);

    @Query("SELECT m FROM StudyModule m WHERE LOWER(m.topic) LIKE LOWER(CONCAT('%', :substring, '%'))")
    List<StudyModule> findByTopicContainingIgnoreCase(@Param("substring") String substring);

    List<StudyModule> findByAuthorId(long authorId);


}
