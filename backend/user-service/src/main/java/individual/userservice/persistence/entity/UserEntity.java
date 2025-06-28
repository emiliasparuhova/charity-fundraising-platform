package individual.userservice.persistence.entity;

import individual.userservice.domain.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "app_user", schema = "public")
@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Embedded
    private CredentialsEntity credentials;

    @Column(name = "profile_picture", columnDefinition = "bytea")
    private byte[] profilePicture;

    @Column(name = "join_date")
    private LocalDate joinDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "deletion_date")
    private LocalDate deletionDate;
}
