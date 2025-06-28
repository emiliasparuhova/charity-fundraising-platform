package individual.userservice.business.user.impl;

import individual.userservice.business.user.exception.UserNotFoundException;
import individual.userservice.domain.enums.UserRole;
import individual.userservice.messaging.MessageSender;
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
class DeactivateUserUseCaseImplTest {

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    MessageSender messageSenderMock;

    @InjectMocks
    private DeactivateUserUseCaseImpl deactivateUserUseCase;

    private UserEntity existingUserEntity;

    @BeforeEach
    void setUp() {
        existingUserEntity = UserEntity.builder()
                .id(1L)
                .name("Bob Builder")
                .email("bob@example.com")
                .joinDate(LocalDate.of(2020, 1, 1))
                .role(UserRole.ADMIN)
                .profilePicture(new byte[]{1, 2, 3})
                .credentials(CredentialsEntity.builder().hashedPassword("secret123").build())
                .isDeleted(false)
                .build();
    }

    @Test
    void deactivateUser_shouldMarkUserAsDeleted() {
        when(userRepositoryMock.findById(1L)).thenReturn(Optional.of(existingUserEntity));

        deactivateUserUseCase.deactivateUser(1L);

        verify(userRepositoryMock).save(argThat(user ->
                user.getIsDeleted() &&
                        user.getDeletionDate().equals(LocalDate.now()) &&
                        user.getRole() == null &&
                        user.getJoinDate() == null &&
                        user.getProfilePicture() == null
        ));
    }

    @Test
    void deactivateUser_shouldThrowWhenUserNotFound() {
        when(userRepositoryMock.findById(2L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> deactivateUserUseCase.deactivateUser(2L));

        verify(userRepositoryMock, never()).save(any());
    }
}
