package individual.charityservice.business.converter;

import individual.charityservice.domain.Charity;
import individual.charityservice.persistence.entity.CharityEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CharityConverterTest {

    @Test
    void convertToDomain_successful() {
        CharityEntity mockCharityEntity = mock(CharityEntity.class);
        when(mockCharityEntity.getId()).thenReturn(1L);
        when(mockCharityEntity.getTitle()).thenReturn("CharityTitle");
        when(mockCharityEntity.getDescription()).thenReturn("CharityDescription");
        when(mockCharityEntity.getFundraisingGoal()).thenReturn(5000.0);
        when(mockCharityEntity.getCreatorId()).thenReturn(12345L);
        when(mockCharityEntity.getCreationDate()).thenReturn(LocalDate.of(2025, 3, 25));
        when(mockCharityEntity.getPhotos()).thenReturn(Collections.emptyList());

        Charity convertedCharity = CharityConverter.convertToDomain(mockCharityEntity);

        assertEquals(1L, convertedCharity.getId());
        assertEquals("CharityTitle", convertedCharity.getTitle());
        assertEquals("CharityDescription", convertedCharity.getDescription());
        assertEquals(5000.0, convertedCharity.getFundraisingGoal());
        assertEquals(12345L, convertedCharity.getCreatorId());
        assertEquals(LocalDate.of(2025, 3, 25), convertedCharity.getCreationDate());
    }

    @Test
    void convertToEntity_successful() {
        Charity mockCharity = mock(Charity.class);
        when(mockCharity.getId()).thenReturn(1L);
        when(mockCharity.getTitle()).thenReturn("CharityTitle");
        when(mockCharity.getDescription()).thenReturn("CharityDescription");
        when(mockCharity.getFundraisingGoal()).thenReturn(5000.0);
        when(mockCharity.getCreatorId()).thenReturn(12345L);
        when(mockCharity.getCreationDate()).thenReturn(LocalDate.of(2025, 3, 25));
        when(mockCharity.getPhotos()).thenReturn(Collections.emptyList());

        CharityEntity convertedCharityEntity = CharityConverter.convertToEntity(mockCharity);

        assertEquals(1L, convertedCharityEntity.getId());
        assertEquals("CharityTitle", convertedCharityEntity.getTitle());
        assertEquals("CharityDescription", convertedCharityEntity.getDescription());
        assertEquals(5000.0, convertedCharityEntity.getFundraisingGoal());
        assertEquals(12345L, convertedCharityEntity.getCreatorId());
        assertEquals(LocalDate.of(2025, 3, 25), convertedCharityEntity.getCreationDate());
    }

    @Test
    void convertToDomain_returnsEmptyDomain_whenEntityIsNull() {
        Charity convertedCharity = CharityConverter.convertToDomain(null);

        assertNull(convertedCharity.getId());
        assertNull(convertedCharity.getTitle());
        assertNull(convertedCharity.getDescription());
        assertEquals(0.0, convertedCharity.getFundraisingGoal());
        assertNull(convertedCharity.getCreatorId());
        assertNull(convertedCharity.getCreationDate());
    }

    @Test
    void convertToEntity_returnsEmptyEntity_whenDomainIsNull() {
        CharityEntity convertedCharityEntity = CharityConverter.convertToEntity(null);

        assertNotNull(convertedCharityEntity);
        assertNull(convertedCharityEntity.getId());
        assertNull(convertedCharityEntity.getTitle());
        assertNull(convertedCharityEntity.getDescription());
        assertEquals(0.0, convertedCharityEntity.getFundraisingGoal());
        assertNull(convertedCharityEntity.getCreatorId());
        assertNull(convertedCharityEntity.getCreationDate());
    }
}
