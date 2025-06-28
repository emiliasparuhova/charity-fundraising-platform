package individual.userservice.business.user.impl;

import individual.userservice.business.converter.UserConverter;
import individual.userservice.business.user.exception.EmailAlreadyInUseException;
import individual.userservice.domain.User;
import individual.userservice.domain.enums.UserRole;
import individual.userservice.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseImplTest {

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private PasswordEncoder passwordEncoderMock;

    @InjectMocks
    private CreateUserUseCaseImpl createUserUseCase;

    private User user;
    private String plainTextPassword;
    private String hashedPassword;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .profilePicture(null)
                .joinDate(LocalDate.now())
                .role(UserRole.USER)
                .build();

        plainTextPassword = "securePassword";
        hashedPassword = "hashedSecurePassword";
    }

    @Test
    void createUser_successfulWhenEmailIsNotTaken() {
        when(userRepositoryMock.existsByEmailAndIsDeletedFalse(user.getEmail())).thenReturn(false);
        when(passwordEncoderMock.encode(plainTextPassword)).thenReturn(hashedPassword);
        when(userRepositoryMock.save(any())).thenReturn(UserConverter.convertToEntity(user, hashedPassword));

        Long actualResult = createUserUseCase.createUser(user, plainTextPassword);

        assertNotNull(actualResult);
        assertEquals(1L, actualResult);
        verify(userRepositoryMock, times(1)).save(any());
    }

    @Test
    void createUser_shouldThrowExceptionWhenEmailIsAlreadyInUse() {
        when(userRepositoryMock.existsByEmailAndIsDeletedFalse(user.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyInUseException.class, () -> createUserUseCase.createUser(user, plainTextPassword));  // Ensure exception is thrown
        verify(userRepositoryMock, never()).save(any());
    }
}