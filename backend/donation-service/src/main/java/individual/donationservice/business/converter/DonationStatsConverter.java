package individual.donationservice.business.converter;

import individual.donationservice.domain.DonationStats;

public final class DonationStatsConverter {
    private DonationStatsConverter() {
    }

    public static DonationStats convertToDomain(Object[] donationStats) {
        if (donationStats == null || donationStats.length < 2) {
            throw new IllegalArgumentException("Invalid donation stats data.");
        }

        Long donationCount = ((Number) donationStats[0]).longValue();
        Double totalAmount = donationStats[1] != null ? ((Number) donationStats[1]).doubleValue() : 0.0;

        return new DonationStats(donationCount, totalAmount);
    }
}
