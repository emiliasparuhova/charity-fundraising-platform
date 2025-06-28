package individual.userservice.controller.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
public class CreateUserRequest {
    @NotBlank
    @Length(min = 2)
    private String name;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
}
