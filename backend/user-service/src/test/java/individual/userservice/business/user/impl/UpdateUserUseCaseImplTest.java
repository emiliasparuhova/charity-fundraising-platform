package individual.userservice.business.user.impl;

import individual.userservice.business.user.exception.EmailAlreadyInUseException;
import individual.userservice.business.user.exception.UserNotFoundException;
import individual.userservice.domain.User;
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

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserUseCaseImplTest {

    @Mock
    private UserRepository userRepositoryMock;

    @InjectMocks
    private UpdateUserUseCaseImpl updateUserUseCase;

    private UserEntity existingUserEntity;
    private User updatedUserData;

    @BeforeEach
    void setUp() {
        byte[] originalPic = new byte[]{1, 2, 3};
        byte[] newPic = new byte[]{4, 5, 6};

        existingUserEntity = UserEntity.builder()
                .id(1L)
                .name("Alice Smith")
                .email("alice@example.com")
                .joinDate(LocalDate.now())
                .role(UserRole.USER)
                .profilePicture(originalPic)
                .credentials(CredentialsEntity.builder().hashedPassword("hashedPassword123").build())
                .build();

        updatedUserData = User.builder()
                .id(1L)
                .name("Alice Johnson")
                .email("alice.johnson@example.com")
                .profilePicture(newPic)
                .joinDate(existingUserEntity.getJoinDate())
                .role(existingUserEntity.getRole())
                .build();
    }

    @Test
    void updateUser_shouldUpdateSuccessfullyWhenEmailIsNotTaken() {
        when(userRepositoryMock.findById(1L)).thenReturn(Optional.of(existingUserEntity));
        when(userRepositoryMock.existsByEmailAndIsDeletedFalse(updatedUserData.getEmail())).thenReturn(false);

        updateUserUseCase.updateUser(1L, updatedUserData);

        verify(userRepositoryMock).save(any());
    }

    @Test
    void updateUser_shouldThrowExceptionWhenUserNotFound() {
        when(userRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> updateUserUseCase.updateUser(1L, updatedUserData));
        verify(userRepositoryMock, never()).save(any());
    }

    @Test
    void updateUser_shouldThrowExceptionWhenEmailIsAlreadyInUse() {
        when(userRepositoryMock.findById(1L)).thenReturn(Optional.of(existingUserEntity));
        when(userRepositoryMock.existsByEmailAndIsDeletedFalse(updatedUserData.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyInUseException.class, () -> updateUserUseCase.updateUser(1L, updatedUserData));
        verify(userRepositoryMock, never()).save(any());
    }
}
