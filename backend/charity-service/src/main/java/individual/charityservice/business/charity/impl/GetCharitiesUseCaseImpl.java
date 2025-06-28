package individual.charityservice.business.charity.impl;

import individual.charityservice.business.charity.GetCharitiesUseCase;
import individual.charityservice.business.converter.CharityConverter;
import individual.charityservice.domain.Charity;
import individual.charityservice.persistence.CharityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetCharitiesUseCaseImpl implements GetCharitiesUseCase {
    private final CharityRepository charityRepository;

    @Override
    public List<Charity> getCharities() {
        return charityRepository.findAll()
                .stream()
                .map(CharityConverter::convertToDomain)
                .toList();
    }


}
