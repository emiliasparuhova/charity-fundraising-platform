package individual.charityservice.business.charity;

import individual.charityservice.domain.Charity;

public interface CreateCharityUseCase {
    Long createCharity(Charity charity);
}
