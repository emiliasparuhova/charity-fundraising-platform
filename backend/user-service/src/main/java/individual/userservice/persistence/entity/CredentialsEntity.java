package individual.userservice.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CredentialsEntity {
    @Column(name = "hashed_password")
    private String hashedPassword;
}
