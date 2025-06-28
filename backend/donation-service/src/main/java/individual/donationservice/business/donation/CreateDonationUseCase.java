package individual.donationservice.business.donation;

import individual.donationservice.domain.Donation;

public interface CreateDonationUseCase {
    Long createDonation(Donation donation);
}
