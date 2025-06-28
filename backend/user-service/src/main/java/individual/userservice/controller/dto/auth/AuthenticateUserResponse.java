package individual.userservice.controller.dto.auth;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthenticateUserResponse {
    private String accessToken;
}
