package individual.charityservice.business.charity.impl;

import individual.charityservice.business.charity.CreateCharityUseCase;
import individual.charityservice.business.converter.CharityConverter;
import individual.charityservice.domain.Charity;
import individual.charityservice.messaging.MessageSender;
import individual.charityservice.persistence.CharityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class CreateCharityUseCaseImpl implements CreateCharityUseCase {
    private final CharityRepository charityRepository;
    private final MessageSender messageSender;

    @Override
    public Long createCharity(Charity charity) {
        charity.setCreationDate(LocalDate.now());
        charity.setIsActive(true);

        Charity createdCharity = CharityConverter.convertToDomain(
                charityRepository.save(CharityConverter.convertToEntity(charity)));

        String message = "UserID " + charity.getCreatorId() +
                " created a charity named " + charity.getTitle();
        messageSender.sendCharityCreatedMessage(message);

        return createdCharity.getId();
    }
}
