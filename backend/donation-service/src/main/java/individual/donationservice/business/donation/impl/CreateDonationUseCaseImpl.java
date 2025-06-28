package individual.donationservice.business.donation.impl;

import individual.donationservice.business.converter.DonationConverter;
import individual.donationservice.business.donation.CreateDonationUseCase;
import individual.donationservice.domain.Donation;
import individual.donationservice.persistence.DonationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class CreateDonationUseCaseImpl implements CreateDonationUseCase {

    private DonationRepository donationRepository;

    public Long createDonation(Donation donation) {
        donation.setDate(LocalDate.now());
        return donationRepository.save(DonationConverter.convertToEntity(donation)).getId();
    }
}
