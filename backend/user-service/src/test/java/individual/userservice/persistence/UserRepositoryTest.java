package individual.userservice.persistence;

import individual.userservice.domain.enums.UserRole;
import individual.userservice.persistence.entity.CredentialsEntity;
import individual.userservice.persistence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        UserEntity userEntity = UserEntity.builder()
                .email("test@example.com")
                .name("Test User")
                .credentials(CredentialsEntity.builder().build())
                .profilePicture(null)
                .joinDate(LocalDate.now())
                .role(UserRole.USER)
                .isDeleted(false)
                .deletionDate(null)
                .build();
        userRepository.save(userEntity);
    }

    @Test
    void existsByEmailAndIsDeletedFalse_shouldReturnTrue_whenEmailExists() {
        boolean exists = userRepository.existsByEmailAndIsDeletedFalse("test@example.com");

        assertTrue(exists, "The email should exist in the database.");
    }

    @Test
    void existsByEmailAndIsDeletedFalse_shouldReturnFalse_whenEmailDoesNotExist() {
        boolean exists = userRepository.existsByEmailAndIsDeletedFalse("nonexistent@example.com");

        assertFalse(exists, "The email should not exist in the database.");
    }
}
