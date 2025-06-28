package individual.charityservice.business.charity.impl;

import individual.charityservice.business.charity.GetCharityByIdUseCase;
import individual.charityservice.business.charity.exception.CharityNotFoundException;
import individual.charityservice.business.converter.CharityConverter;
import individual.charityservice.domain.Charity;
import individual.charityservice.persistence.CharityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GetCharityByIdUseCaseImpl implements GetCharityByIdUseCase {

    private CharityRepository charityRepository;

    public Charity getCharity(Long id){
        Optional<Charity> charity = charityRepository.findById(id).map(CharityConverter::convertToDomain);

        if (charity.isEmpty()){
            throw new CharityNotFoundException();
        }

        return charity.get();
    }
}
