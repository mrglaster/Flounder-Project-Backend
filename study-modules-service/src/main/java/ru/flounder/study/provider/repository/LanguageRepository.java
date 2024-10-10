package ru.flounder.study.provider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.flounder.study.provider.domain.Language;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    Language findById(long id);
}
