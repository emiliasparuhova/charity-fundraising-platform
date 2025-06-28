package individual.userservice.configuration.security.token.impl;

import individual.userservice.configuration.security.token.AccessToken;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class AccessTokenImpl implements AccessToken {
    private final String subject;
    private final Long userId;
    private final String role;

    public AccessTokenImpl(String subject, Long userId, String role) {
        this.subject = subject;
        this.userId = userId;
        this.role = role != null ? role : "DEFAULT_ROLE";
    }

    @Override
    public boolean hasRole(String roleName) {
        return this.role.equals(roleName);
    }
}
