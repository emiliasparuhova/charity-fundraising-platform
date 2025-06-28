package individual.userservice.business.converter;

import individual.userservice.domain.User;
import individual.userservice.domain.enums.UserRole;
import individual.userservice.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserConverterTest {

    @Test
    void convertToDomain_successful() {
        UserEntity mockUserEntity = mock(UserEntity.class);
        when(mockUserEntity.getId()).thenReturn(1L);
        when(mockUserEntity.getName()).thenReturn("John Doe");
        when(mockUserEntity.getEmail()).thenReturn("johndoe@example.com");
        when(mockUserEntity.getProfilePicture()).thenReturn(new byte[]{1, 2, 3});
        when(mockUserEntity.getJoinDate()).thenReturn(LocalDate.of(2025, 3, 25));
        when(mockUserEntity.getRole()).thenReturn(UserRole.USER);

        User convertedUser = UserConverter.convertToDomain(mockUserEntity);

        assertEquals(1L, convertedUser.getId());
        assertEquals("John Doe", convertedUser.getName());
        assertEquals("johndoe@example.com", convertedUser.getEmail());
        assertArrayEquals(new byte[]{1, 2, 3}, convertedUser.getProfilePicture());
        assertEquals(LocalDate.of(2025, 3, 25), convertedUser.getJoinDate());
        assertEquals(UserRole.USER, convertedUser.getRole());
    }

    @Test
    void convertToEntity_successful() {
        User mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn(1L);
        when(mockUser.getName()).thenReturn("John Doe");
        when(mockUser.getEmail()).thenReturn("johndoe@example.com");
        when(mockUser.getProfilePicture()).thenReturn(new byte[]{1, 2, 3});
        when(mockUser.getJoinDate()).thenReturn(LocalDate.of(2025, 3, 25));
        when(mockUser.getRole()).thenReturn(UserRole.USER);

        String hashedPassword = "hashedPassword123";

        UserEntity convertedUserEntity = UserConverter.convertToEntity(mockUser, hashedPassword);

        assertEquals(1L, convertedUserEntity.getId());
        assertEquals("John Doe", convertedUserEntity.getName());
        assertEquals("johndoe@example.com", convertedUserEntity.getEmail());
        assertArrayEquals(new byte[]{1, 2, 3}, convertedUserEntity.getProfilePicture());
        assertEquals(LocalDate.of(2025, 3, 25), convertedUserEntity.getJoinDate());
        assertEquals(UserRole.USER, convertedUserEntity.getRole());
        assertNotNull(convertedUserEntity.getCredentials());
    }

    @Test
    void convertToDomain_returnsEmptyDomain_whenEntityIsNull() {
        User convertedUser = UserConverter.convertToDomain(null);

        assertNull(convertedUser.getId());
        assertNull(convertedUser.getName());
        assertNull(convertedUser.getEmail());
        assertNull(convertedUser.getProfilePicture());
        assertNull(convertedUser.getJoinDate());
        assertNull(convertedUser.getRole());
    }

    @Test
    void convertToEntity_returnsEmptyEntity_whenDomainIsNull() {
        UserEntity convertedUserEntity = UserConverter.convertToEntity(null, "hashedPassword123");

        assertNotNull(convertedUserEntity);
        assertNull(convertedUserEntity.getId());
        assertNull(convertedUserEntity.getName());
        assertNull(convertedUserEntity.getEmail());
        assertNull(convertedUserEntity.getProfilePicture());
        assertNull(convertedUserEntity.getJoinDate());
        assertNull(convertedUserEntity.getRole());
        assertNotNull(convertedUserEntity.getCredentials());
    }
}
