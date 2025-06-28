package individual.userservice.business.user.impl;

import individual.userservice.business.converter.UserConverter;
import individual.userservice.domain.User;
import individual.userservice.domain.enums.UserRole;
import individual.userservice.persistence.UserRepository;
import individual.userservice.persistence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetUsersUseCaseImplTest {

    @Mock
    private UserRepository userRepositoryMock;

    @InjectMocks
    private GetUsersUseCaseImpl getUsersUseCase;

    private UserEntity userEntity1;
    private UserEntity userEntity2;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        userEntity1 = UserEntity.builder()
                .id(1L)
                .name("Alice Smith")
                .email("alice@example.com")
                .profilePicture(null)
                .joinDate(LocalDate.now())
                .role(UserRole.USER)
                .isDeleted(false)
                .deletionDate(null)
                .build();

        userEntity2 = UserEntity.builder()
                .id(2L)
                .name("Bob Johnson")
                .email("bob@example.com")
                .profilePicture(null)
                .joinDate(LocalDate.now())
                .role(UserRole.USER)
                .isDeleted(false)
                .deletionDate(null)
                .build();

        user1 = UserConverter.convertToDomain(userEntity1);
        user2 = UserConverter.convertToDomain(userEntity2);
    }

    @Test
    void getUsers_shouldReturnAllUsers() {
        when(userRepositoryMock.findAll()).thenReturn(Arrays.asList(userEntity1, userEntity2));

        List<User> result = getUsersUseCase.getUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(user1.getName(), result.get(0).getName());
        assertEquals(user2.getName(), result.get(1).getName());
        verify(userRepositoryMock, times(1)).findAll();
    }

    @Test
    void getUsers_shouldReturnEmptyListWhenNoUsersExist() {
        when(userRepositoryMock.findAll()).thenReturn(List.of());

        List<User> result = getUsersUseCase.getUsers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepositoryMock, times(1)).findAll();
    }
}
