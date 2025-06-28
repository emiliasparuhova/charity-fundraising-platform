package individual.donationservice.business.donation.impl;

import individual.donationservice.business.converter.DonationStatsConverter;
import individual.donationservice.business.donation.GetDonationStatsByCharity;
import individual.donationservice.domain.DonationStats;
import individual.donationservice.persistence.DonationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetDonationStatsByCharityImpl implements GetDonationStatsByCharity {
    private DonationRepository donationRepository;

    public DonationStats getDonationStats(Long charityId) {
        Object[] stats = donationRepository.getDonationCountAndTotalAmountForCharity(charityId).get(0);
        return DonationStatsConverter.convertToDomain(stats);
    }
}
