package individual.charityservice.business.charity.impl;

import individual.charityservice.business.charity.UpdateCharitiesAfterUserDeletionUseCase;
import individual.charityservice.business.converter.CharityConverter;
import individual.charityservice.domain.Charity;
import individual.charityservice.persistence.CharityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UpdateCharitiesAfterUserDeletionUseCaseImpl implements UpdateCharitiesAfterUserDeletionUseCase {
    private final CharityRepository charityRepository;

    public void updateCharitiesAfterUserDeletion(Long userId) {
        List<Charity> charities = charityRepository.findAllByCreatorId(userId).stream()
                .map(CharityConverter::convertToDomain)
                .toList();

        for (Charity charity : charities) {
            charity.getPhotos().clear();

            if (Boolean.TRUE.equals(charity.getIsActive())) {
                charity.setIsActive(false);
            }
        }

        charityRepository.saveAll(charities.stream()
                .map(CharityConverter::convertToEntity)
                .toList());
    }
}
