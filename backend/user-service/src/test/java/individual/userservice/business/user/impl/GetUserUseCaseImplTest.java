package individual.userservice.business.user.impl;

import individual.userservice.business.converter.UserConverter;
import individual.userservice.business.user.exception.UserNotFoundException;
import individual.userservice.domain.User;
import individual.userservice.domain.enums.UserRole;
import individual.userservice.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetUserUseCaseImplTest {

    @Mock
    private UserRepository userRepositoryMock;

    @InjectMocks
    private GetUserUseCaseImpl getUserUseCase;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("Alice Smith")
                .email("alice.smith@example.com")
                .profilePicture(null)
                .joinDate(LocalDate.now())
                .role(UserRole.USER)
                .isDeleted(false)
                .deletionDate(null)
                .build();
    }

    @Test
    void getUser_shouldReturnUserWhenUserExists() {
        when(userRepositoryMock.findById(1L)).thenReturn(Optional.of(UserConverter.convertToEntity(user, "password")));

        User result = getUserUseCase.getUser(1L);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getEmail(), result.getEmail());
        verify(userRepositoryMock, times(1)).findById(1L);
    }

    @Test
    void getUser_shouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {
        when(userRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> getUserUseCase.getUser(1L));
        verify(userRepositoryMock, times(1)).findById(1L);
    }

    @Test
    void getUser_shouldThrowUserNotFoundExceptionWhenUserIsDeleted() {
        user.setIsDeleted(true);
        when(userRepositoryMock.findById(1L)).thenReturn(Optional.of(UserConverter.convertToEntity(user, "password")));

        assertThrows(UserNotFoundException.class, () -> getUserUseCase.getUser(1L));
        verify(userRepositoryMock, times(1)).findById(1L);
    }

}
