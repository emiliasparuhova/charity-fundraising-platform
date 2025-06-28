package individual.donationservice.controller.converter;

import individual.donationservice.controller.dto.donation.CreateDonationRequest;
import individual.donationservice.controller.dto.donation.CreateDonationResponse;
import individual.donationservice.controller.dto.donation.GetDonationStatsForCharityResponse;
import individual.donationservice.domain.Donation;
import individual.donationservice.domain.DonationStats;


public final class DonationRequestConverter {
    private DonationRequestConverter() {}

    public static Donation convertCreateRequest(CreateDonationRequest request) {
        return Donation.builder()
                .amount(request.getAmount())
                .charityId(request.getCharityId())
                .donorId(request.getDonorId())
                .build();
    }

    public static CreateDonationResponse convertCreateResponse(Long id) {
        return CreateDonationResponse.builder()
                .id(id)
                .build();
    }

    public static GetDonationStatsForCharityResponse convertGetDonationStatsForCharityResponse(DonationStats stats) {
        return GetDonationStatsForCharityResponse.builder()
                .donationCount(stats.getDonationCount())
                .totalFundsRaised(stats.getTotalFundsRaised())
                .build();
    }
}
