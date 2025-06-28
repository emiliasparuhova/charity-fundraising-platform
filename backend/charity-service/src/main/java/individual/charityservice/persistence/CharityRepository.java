package individual.charityservice.persistence;

import individual.charityservice.persistence.entity.CharityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CharityRepository extends JpaRepository<CharityEntity, Long> {
    List<CharityEntity> findAllByCreatorId(Long creatorId);
}
