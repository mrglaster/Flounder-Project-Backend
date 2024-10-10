package ru.flounder.study.provider.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.checkerframework.common.aliasing.qual.Unique;
import org.hibernate.proxy.HibernateProxy;
import java.time.LocalDateTime;

@Entity
@Table(name="study_module")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class StudyModule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="topic")
    @Size(max=30)
    @Unique
    @NotBlank
    private String topic;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "language_id")
    @NotNull
    private Language language;

    @Column(name="icon")
    @Size(max=40)
    @NotBlank
    private String iconPath;

    @Column(name="file")
    @NotBlank
    private String filePath;

    @Column(name = "createdAt")
    @NotNull
    private LocalDateTime createdAt;

    @Column(name = "active")
    private boolean  isActive = true;

    @Column(name = "author_id")
    @NotNull
    private long authorId;

    @Column(name="display_words")
    @NotBlank
    private String displayWords;


    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ModuleStatus status;

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
