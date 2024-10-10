package ru.flounder.study.provider.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.checkerframework.common.aliasing.qual.Unique;
import org.hibernate.proxy.HibernateProxy;

@Entity
@Table(name="languages")
@Getter
@Setter
@ToString
@RequiredArgsConstructor

public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="language")
    @NotBlank
    @Unique
    private String language;


    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

}
