package individual.donationservice.business.donation.impl;

import individual.donationservice.domain.DonationStats;
import individual.donationservice.persistence.DonationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetDonationStatsByCharityImplTest {

    @Mock
    DonationRepository donationRepositoryMock;

    @InjectMocks
    GetDonationStatsByCharityImpl getDonationStatsByCharity;

    private final Long charityId = 1L;

    @Test
    void getDonationStats_returnsCorrectStatsWhenValidCharityId() {
        Object[] stats = {10L, 5000.0};
        when(donationRepositoryMock.getDonationCountAndTotalAmountForCharity(charityId)).thenReturn(Collections.singletonList(stats));

        DonationStats actualStats = getDonationStatsByCharity.getDonationStats(charityId);
        DonationStats expectedStats = DonationStats.builder()
                .donationCount(10L)
                .totalFundsRaised(5000.0)
                .build();

        assertEquals(expectedStats, actualStats);

        verify(donationRepositoryMock, times(1)).getDonationCountAndTotalAmountForCharity(charityId);
    }

    @Test
    void getDonationStats_returnsEmptyStatsWhenNoDonationsExist() {
        Object[] stats = {0L, 0.0};
        when(donationRepositoryMock.getDonationCountAndTotalAmountForCharity(charityId)).thenReturn(Collections.singletonList(stats));

        DonationStats actualStats = getDonationStatsByCharity.getDonationStats(charityId);
        DonationStats expectedStats = DonationStats.builder()
                .donationCount(0L)
                .totalFundsRaised(0.0)
                .build();

        assertEquals(expectedStats, actualStats);

        verify(donationRepositoryMock, times(1)).getDonationCountAndTotalAmountForCharity(charityId);
    }

    @Test
    void getDonationStats_throwsExceptionWhenNoStatsReturned() {
        when(donationRepositoryMock.getDonationCountAndTotalAmountForCharity(charityId)).thenReturn(Collections.emptyList());

        assertThrows(IndexOutOfBoundsException.class, () -> getDonationStatsByCharity.getDonationStats(charityId));

        verify(donationRepositoryMock, times(1)).getDonationCountAndTotalAmountForCharity(charityId);
    }
}
