package individual.donationservice.business.converter;

import individual.donationservice.domain.DonationStats;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DonationStatsConverterTest {

    @Test
    void convertToDomain_successful() {
        Object[] donationStats = {100L, 5000.0};

        DonationStats convertedStats = DonationStatsConverter.convertToDomain(donationStats);

        assertEquals(100L, convertedStats.getDonationCount());
        assertEquals(5000.0, convertedStats.getTotalFundsRaised());
    }

    @Test
    void convertToDomain_defaultTotalAmount_whenSecondElementIsNull() {
        Object[] donationStats = {100L, null};

        DonationStats convertedStats = DonationStatsConverter.convertToDomain(donationStats);

        assertEquals(100L, convertedStats.getDonationCount());
        assertEquals(0.0, convertedStats.getTotalFundsRaised());
    }

    @Test
    void convertToDomain_successful_withLongAndInteger() {
        Object[] donationStats = {100L, 5000};

        DonationStats convertedStats = DonationStatsConverter.convertToDomain(donationStats);

        assertEquals(100L, convertedStats.getDonationCount());
        assertEquals(5000.0, convertedStats.getTotalFundsRaised());
    }
}
