package individual.donationservice.business.converter;

import individual.donationservice.domain.Donation;
import individual.donationservice.persistence.entity.DonationEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DonationConverterTest {

    @Test
    void convertToDomain_successful() {
        DonationEntity mockDonationEntity = mock(DonationEntity.class);
        when(mockDonationEntity.getId()).thenReturn(1L);
        when(mockDonationEntity.getAmount()).thenReturn(100.0);
        when(mockDonationEntity.getDate()).thenReturn(LocalDate.of(2025, 3, 25));
        when(mockDonationEntity.getCharityId()).thenReturn(1L);
        when(mockDonationEntity.getDonorId()).thenReturn(12345L);

        Donation convertedDonation = DonationConverter.convertToDomain(mockDonationEntity);

        assertEquals(1L, convertedDonation.getId());
        assertEquals(100.0, convertedDonation.getAmount());
        assertEquals(LocalDate.of(2025, 3, 25), convertedDonation.getDate());
        assertEquals(1L, convertedDonation.getCharityId());
        assertEquals(12345L, convertedDonation.getDonorId());
    }

    @Test
    void convertToEntity_successful() {
        Donation mockDonation = mock(Donation.class);
        when(mockDonation.getId()).thenReturn(1L);
        when(mockDonation.getAmount()).thenReturn(100.0);
        when(mockDonation.getDate()).thenReturn(LocalDate.of(2025, 3, 25));
        when(mockDonation.getCharityId()).thenReturn(1L);
        when(mockDonation.getDonorId()).thenReturn(12345L);

        DonationEntity convertedDonationEntity = DonationConverter.convertToEntity(mockDonation);

        assertEquals(1L, convertedDonationEntity.getId());
        assertEquals(100.0, convertedDonationEntity.getAmount());
        assertEquals(LocalDate.of(2025, 3, 25), convertedDonationEntity.getDate());
        assertEquals(1L, convertedDonationEntity.getCharityId());
        assertEquals(12345L, convertedDonationEntity.getDonorId());
    }

    @Test
    void convertToDomain_returnsEmptyDomain_whenEntityIsNull() {
        Donation convertedDonation = DonationConverter.convertToDomain(null);

        assertNull(convertedDonation.getId());
        assertEquals(0, convertedDonation.getAmount());
        assertNull(convertedDonation.getDate());
        assertNull(convertedDonation.getCharityId());
        assertNull(convertedDonation.getDonorId());
    }

    @Test
    void convertToEntity_returnsEmptyEntity_whenDomainIsNull() {
        DonationEntity convertedDonationEntity = DonationConverter.convertToEntity(null);

        assertNotNull(convertedDonationEntity);
        assertNull(convertedDonationEntity.getId());
        assertEquals(0, convertedDonationEntity.getAmount());
        assertNull(convertedDonationEntity.getDate());
        assertNull(convertedDonationEntity.getCharityId());
        assertNull(convertedDonationEntity.getDonorId());
    }
}
