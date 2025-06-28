package individual.donationservice.business.converter;

import individual.donationservice.domain.Donation;
import individual.donationservice.persistence.entity.DonationEntity;

import java.time.LocalDate;
import java.util.Objects;

public final class DonationConverter {

    private DonationConverter() {}

    public static Donation convertToDomain(DonationEntity donationEntity) {
        if (Objects.isNull(donationEntity)) {
            return Donation.builder().build();
        }

        return Donation.builder()
                .id(donationEntity.getId())
                .amount(donationEntity.getAmount())
                .date(donationEntity.getDate())
                .charityId(donationEntity.getCharityId())
                .donorId(donationEntity.getDonorId())
                .build();
    }

    public static DonationEntity convertToEntity(Donation donation) {
        if (Objects.isNull(donation)) {
            return DonationEntity.builder().build();
        }

        return DonationEntity.builder()
                .id(donation.getId())
                .amount(donation.getAmount())
                .date(donation.getDate() != null ? donation.getDate() : LocalDate.now())
                .charityId(donation.getCharityId())
                .donorId(donation.getDonorId())
                .build();
    }
}

