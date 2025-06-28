package individual.userservice.controller.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class AuthenticateUserRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String plainTextPassword;
}
