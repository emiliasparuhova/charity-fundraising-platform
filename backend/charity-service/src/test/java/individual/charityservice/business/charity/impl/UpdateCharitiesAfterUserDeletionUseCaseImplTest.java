package individual.charityservice.business.charity.impl;

import individual.charityservice.business.converter.CharityConverter;
import individual.charityservice.domain.Charity;
import individual.charityservice.persistence.CharityRepository;
import individual.charityservice.persistence.entity.CharityEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCharitiesAfterUserDeletionUseCaseImplTest {

    @Mock
    CharityRepository charityRepositoryMock;

    @InjectMocks
    UpdateCharitiesAfterUserDeletionUseCaseImpl updateCharitiesUseCase;

    private CharityEntity charityEntity;

    @BeforeEach
    void setUp() {
        Charity charity = Charity.builder()
                .id(1L)
                .title("Wildlife Rescue")
                .description("Supports rescue operations for endangered wildlife.")
                .fundraisingGoal(10000.0)
                .creatorId(101L)
                .creationDate(LocalDate.now())
                .isActive(true)
                .photos(List.of(new byte[]{1, 2, 3}, new byte[]{4, 5, 6}))
                .build();

        charityEntity = CharityConverter.convertToEntity(charity);
    }

    @Test
    void updateCharitiesAfterUserDeletion_shouldDeactivateCharitiesAndClearPhotos() {
        when(charityRepositoryMock.findAllByCreatorId(101L))
                .thenReturn(List.of(charityEntity));

        updateCharitiesUseCase.updateCharitiesAfterUserDeletion(101L);

        verify(charityRepositoryMock, times(1)).findAllByCreatorId(101L);
        verify(charityRepositoryMock, times(1)).saveAll(argThat(entities -> {
            for (CharityEntity updated : entities) {
                if (updated.getIsActive() || !updated.getPhotos().isEmpty()) {
                    return false;
                }
            }
            return true;
        }));
    }
}
