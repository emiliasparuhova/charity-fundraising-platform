package individual.donationservice.business.donation;

import individual.donationservice.domain.DonationStats;

public interface GetDonationStatsByCharity {

    DonationStats getDonationStats(Long charityId);
}
