package individual.charityservice.business.charity.impl;

import individual.charityservice.business.charity.exception.CharityNotFoundException;
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
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCharityByIdUseCaseImplTest {
    @Mock
    private CharityRepository charityRepositoryMock;

    @InjectMocks
    private GetCharityByIdUseCaseImpl getCharityByIdUseCase;

    private Charity charity;
    private CharityEntity charityEntity;

    @BeforeEach
    void setUp() {
        charity = Charity.builder()
                .id(1L)
                .title("Charity Title")
                .description("Charity Description")
                .fundraisingGoal(10000.0)
                .creatorId(12345L)
                .creationDate(LocalDate.now())
                .photos(Collections.emptyList())
                .isActive(true)
                .build();

        charityEntity = CharityConverter.convertToEntity(charity);
    }

    @Test
    void getCharity_returnsCharityWhenFound() {
        when(charityRepositoryMock.findById(1L)).thenReturn(Optional.of(charityEntity));

        Charity actualCharity = getCharityByIdUseCase.getCharity(1L);

        assertNotNull(actualCharity);
        assertEquals(charity.getId(), actualCharity.getId());
        assertEquals(charity.getTitle(), actualCharity.getTitle());
        assertEquals(charity.getCreatorId(), actualCharity.getCreatorId());
        assertEquals(charity.getCreationDate(), actualCharity.getCreationDate());

        verify(charityRepositoryMock, times(1)).findById(1L);
    }

    @Test
    void getCharity_throwsCharityNotFoundExceptionWhenNotFound() {
        when(charityRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CharityNotFoundException.class, () -> getCharityByIdUseCase.getCharity(1L));

        verify(charityRepositoryMock, times(1)).findById(1L);
    }

}