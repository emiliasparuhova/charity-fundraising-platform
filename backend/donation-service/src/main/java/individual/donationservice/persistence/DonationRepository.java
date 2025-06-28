package individual.donationservice.persistence;

import individual.donationservice.persistence.entity.DonationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DonationRepository extends JpaRepository<DonationEntity, Long> {

    @Query("SELECT COUNT(d) AS donationCount, SUM(d.amount) AS totalAmount " +
            "FROM DonationEntity d WHERE d.charityId = :charityId")
    List<Object[]> getDonationCountAndTotalAmountForCharity(@Param("charityId") Long charityId);

}
