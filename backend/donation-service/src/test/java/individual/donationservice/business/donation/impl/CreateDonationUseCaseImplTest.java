package individual.donationservice.business.donation.impl;

import individual.donationservice.business.converter.DonationConverter;
import individual.donationservice.domain.Donation;
import individual.donationservice.persistence.DonationRepository;
import individual.donationservice.persistence.entity.DonationEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateDonationUseCaseImplTest {

    @Mock
    DonationRepository donationRepositoryMock;

    @InjectMocks
    CreateDonationUseCaseImpl createDonationUseCase;

    private Donation donation;
    private DonationEntity donationEntity;

    @BeforeEach
    void setUp() {
        donation = Donation.builder()
                .id(1L)
                .amount(100.0)
                .donorId(98765L)
                .charityId(1L)
                .build();

        donationEntity = DonationConverter.convertToEntity(donation);
    }

    @Test
    void createDonation_successfulWhenInputIsValid() {
        when(donationRepositoryMock.save(donationEntity)).thenReturn(donationEntity);

        Long actualResult = createDonationUseCase.createDonation(donation);
        Long expectedResult = 1L;

        assertEquals(expectedResult, actualResult);

        verify(donationRepositoryMock, times(1)).save(donationEntity);
    }

    @Test
    void createDonation_setsCurrentDateWhenSaving() {
        when(donationRepositoryMock.save(donationEntity)).thenReturn(donationEntity);

        createDonationUseCase.createDonation(donation);

        assertEquals(LocalDate.now(), donation.getDate());
    }
}
