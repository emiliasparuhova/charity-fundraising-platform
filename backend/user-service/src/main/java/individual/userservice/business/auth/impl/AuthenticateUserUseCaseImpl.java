package individual.userservice.business.auth.impl;

import individual.userservice.business.auth.AuthenticateUserUseCase;
import individual.userservice.business.auth.exception.InvalidCredentialsException;
import individual.userservice.business.converter.UserConverter;
import individual.userservice.configuration.security.token.AccessTokenEncoder;
import individual.userservice.configuration.security.token.impl.AccessTokenImpl;
import individual.userservice.domain.User;
import individual.userservice.persistence.UserRepository;
import individual.userservice.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class AuthenticateUserUseCaseImpl implements AuthenticateUserUseCase {
    private final UserRepository userRepository;
    private final AccessTokenEncoder accessTokenEncoder;
    private final PasswordEncoder passwordEncoder;

    public String authenticateUser(String email, String password) {
        UserEntity userEntity = userRepository.findByEmailAndIsDeletedFalse(email).orElse(null);

        if (Objects.isNull(userEntity)){
            throw new InvalidCredentialsException();
        }

        User user = UserConverter.convertToDomain(userEntity);
        String hashedPassword = userEntity.getCredentials().getHashedPassword();

        if (!passwordEncoder.matches(password, hashedPassword)){
            throw new InvalidCredentialsException();
        }

        return generateAccessToken(user);
    }

    private String generateAccessToken(User user) {
        return accessTokenEncoder.encode(
                new AccessTokenImpl(user.getEmail(), user.getId(), user.getRole().name()));
    }
}
