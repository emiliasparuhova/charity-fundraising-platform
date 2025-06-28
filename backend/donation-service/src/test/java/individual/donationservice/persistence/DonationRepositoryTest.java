package individual.donationservice.persistence;

import individual.donationservice.persistence.entity.DonationEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class DonationRepositoryTest {

    @Autowired
    private DonationRepository donationRepository;

    @BeforeEach
    void setUp() {
        DonationEntity donation1 = DonationEntity.builder()
                .charityId(1L)
                .donorId(2L)
                .amount(50.0)
                .build();

        DonationEntity donation2 = DonationEntity.builder()
                .charityId(1L)
                .donorId(3L)
                .amount(100.0)
                .build();

        DonationEntity donation3 = DonationEntity.builder()
                .charityId(2L)
                .donorId(4L)
                .amount(150.0)
                .build();

        donationRepository.save(donation1);
        donationRepository.save(donation2);
        donationRepository.save(donation3);
    }

    @Test
    void getDonationCountAndTotalAmountForCharity_shouldReturnCorrectStats_whenDonationsExist() {
        List<Object[]> donationStats = donationRepository.getDonationCountAndTotalAmountForCharity(1L);

        assertEquals(1, donationStats.size(), "There should be 1 record returned.");
        assertEquals("2", donationStats.get(0)[0].toString(), "Donation count for charityId 1 should be 2.");
        assertEquals(150.0, donationStats.get(0)[1], "Total donation amount for charityId 1 should be 150.0.");
    }

    @Test
    void getDonationCountAndTotalAmountForCharity_shouldReturnZeroStats_whenNoDonationsForCharity() {
        List<Object[]> donationStats = donationRepository.getDonationCountAndTotalAmountForCharity(3L);

        assertEquals(1, donationStats.size(), "There should be 1 record returned.");
        assertEquals("0", donationStats.get(0)[0].toString(), "Donation count for charityId 3 should be 0.");
        assertEquals(null, donationStats.get(0)[1], "Total donation amount for charityId 3 should be 0.0.");
    }
}
