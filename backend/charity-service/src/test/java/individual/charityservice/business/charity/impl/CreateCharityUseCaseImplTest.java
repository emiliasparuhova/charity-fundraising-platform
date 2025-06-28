package individual.charityservice.business.charity.impl;

import individual.charityservice.business.converter.CharityConverter;
import individual.charityservice.domain.Charity;
import individual.charityservice.messaging.MessageSender;
import individual.charityservice.persistence.CharityRepository;
import individual.charityservice.persistence.entity.CharityEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCharityUseCaseImplTest {

    @Mock
    CharityRepository charityRepositoryMock;

    @Mock
    MessageSender messageSenderMock;

    @InjectMocks
    CreateCharityUseCaseImpl createCharityUseCase;

    private Charity charity;
    private CharityEntity charityEntity;

    @BeforeEach
    void setUp() {
        charity = Charity.builder()
                .id(1L)
                .title("Elephant Conservation Fund")
                .description("A charity dedicated to protecting and conserving elephants in their natural habitat.")
                .fundraisingGoal(50000.0)
                .creatorId(12345L)
                .creationDate(LocalDate.now())
                .photos(Collections.emptyList())
                .isActive(true)
                .build();

        charityEntity = CharityConverter.convertToEntity(charity);
    }

    @Test
    void createCharity_successfulWhenInputIsValid() {
        when(charityRepositoryMock.save(charityEntity)).thenReturn(charityEntity);

        Long actualResult = createCharityUseCase.createCharity(charity);
        Long expectedResult = 1L;

        assertEquals(expectedResult, actualResult);

        verify(charityRepositoryMock, times(1)).save(charityEntity);
    }
}
