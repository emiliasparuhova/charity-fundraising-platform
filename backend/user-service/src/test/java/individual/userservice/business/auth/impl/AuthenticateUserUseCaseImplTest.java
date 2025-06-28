package individual.userservice.business.auth.impl;

import individual.userservice.business.auth.exception.InvalidCredentialsException;
import individual.userservice.configuration.security.token.AccessTokenEncoder;
import individual.userservice.configuration.security.token.impl.AccessTokenImpl;
import individual.userservice.domain.enums.UserRole;
import individual.userservice.persistence.UserRepository;
import individual.userservice.persistence.entity.CredentialsEntity;
import individual.userservice.persistence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticateUserUseCaseImplTest {

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private AccessTokenEncoder accessTokenEncoderMock;

    @Mock
    private PasswordEncoder passwordEncoderMock;

    @InjectMocks
    private AuthenticateUserUseCaseImpl authenticateUserUseCase;

    private UserEntity mockUserEntity;
    private String email;
    private String rawPassword;
    private String hashedPassword;
    private String expectedToken;

    @BeforeEach
    void setUp() {
        email = "user@example.com";
        rawPassword = "plainPass123";
        hashedPassword = "hashedPass123";
        expectedToken = "mocked.jwt.token";

        mockUserEntity = UserEntity.builder()
                .id(1L)
                .name("Jane Doe")
                .email(email)
                .joinDate(LocalDate.now())
                .profilePicture(null)
                .role(UserRole.USER)
                .isDeleted(false)
                .credentials(CredentialsEntity.builder()
                        .hashedPassword(hashedPassword)
                        .build())
                .build();
    }

    @Test
    void authenticateUser_returnsAccessToken_whenCredentialsAreValid() {
        when(userRepositoryMock.findByEmailAndIsDeletedFalse(email)).thenReturn(Optional.of(mockUserEntity));
        when(passwordEncoderMock.matches(rawPassword, hashedPassword)).thenReturn(true);
        when(accessTokenEncoderMock.encode(any(AccessTokenImpl.class))).thenReturn(expectedToken);

        String token = authenticateUserUseCase.authenticateUser(email, rawPassword);

        assertEquals(expectedToken, token);
        verify(userRepositoryMock, times(1)).findByEmailAndIsDeletedFalse(email);
        verify(passwordEncoderMock, times(1)).matches(rawPassword, hashedPassword);
        verify(accessTokenEncoderMock, times(1)).encode(any(AccessTokenImpl.class));
    }

    @Test
    void authenticateUser_throwsInvalidCredentialsException_whenUserNotFound() {
        when(userRepositoryMock.findByEmailAndIsDeletedFalse(email)).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class,
                () -> authenticateUserUseCase.authenticateUser(email, rawPassword));

        verify(userRepositoryMock, times(1)).findByEmailAndIsDeletedFalse(email);
        verifyNoInteractions(passwordEncoderMock);
        verifyNoInteractions(accessTokenEncoderMock);
    }

    @Test
    void authenticateUser_throwsInvalidCredentialsException_whenPasswordDoesNotMatch() {
        when(userRepositoryMock.findByEmailAndIsDeletedFalse(email)).thenReturn(Optional.of(mockUserEntity));
        when(passwordEncoderMock.matches(rawPassword, hashedPassword)).thenReturn(false);

        assertThrows(InvalidCredentialsException.class,
                () -> authenticateUserUseCase.authenticateUser(email, rawPassword));

        verify(userRepositoryMock, times(1)).findByEmailAndIsDeletedFalse(email);
        verify(passwordEncoderMock, times(1)).matches(rawPassword, hashedPassword);
        verifyNoInteractions(accessTokenEncoderMock);
    }
}
