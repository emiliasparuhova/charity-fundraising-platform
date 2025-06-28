package individual.charityservice.business.charity;

import individual.charityservice.domain.Charity;

public interface GetCharityByIdUseCase {
    Charity getCharity(Long id);
}
