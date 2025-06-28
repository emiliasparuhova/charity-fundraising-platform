package individual.userservice.domain;

import individual.userservice.domain.enums.UserRole;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class User {
    private Long id;
    private String name;
    private String email;
    private byte[] profilePicture;
    private LocalDate joinDate;
    private UserRole role;
    private Boolean isDeleted;
    private LocalDate deletionDate;
}
