package individual.userservice.business.converter;

import individual.userservice.persistence.entity.CredentialsEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CredentialsConverterTest {

    @Test
    void convertToEntity_successful() {
        String hashedPassword = "hashedPassword123";

        CredentialsEntity convertedCredentialsEntity = CredentialsConverter.convertToEntity(hashedPassword);

        assertNotNull(convertedCredentialsEntity);
        assertEquals(hashedPassword, convertedCredentialsEntity.getHashedPassword());
    }

    @Test
    void convertToEntity_returnsEmptyEntity_whenHashedPasswordIsNull() {
        CredentialsEntity convertedCredentialsEntity = CredentialsConverter.convertToEntity(null);

        assertNotNull(convertedCredentialsEntity);
        assertNull(convertedCredentialsEntity.getHashedPassword());
    }
}
