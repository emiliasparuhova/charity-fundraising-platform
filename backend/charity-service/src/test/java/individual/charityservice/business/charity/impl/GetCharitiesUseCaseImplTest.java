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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCharitiesUseCaseImplTest {

    @Mock
    private CharityRepository charityRepositoryMock;

    @InjectMocks
    private GetCharitiesUseCaseImpl getCharitiesUseCase;

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
    void getCharities_returnsListOfCharities() {
        when(charityRepositoryMock.findAll()).thenReturn(Arrays.asList(charityEntity));

        List<Charity> actualCharities = getCharitiesUseCase.getCharities();

        assertNotNull(actualCharities);
        assertEquals(1, actualCharities.size());
        assertEquals(charity.getId(), actualCharities.get(0).getId());
        assertEquals(charity.getTitle(), actualCharities.get(0).getTitle());
        assertEquals(charity.getCreatorId(), actualCharities.get(0).getCreatorId());
        assertEquals(charity.getCreationDate(), actualCharities.get(0).getCreationDate());

        verify(charityRepositoryMock, times(1)).findAll();
    }

    @Test
    void getCharities_returnsEmptyListWhenNoCharitiesFound() {
        when(charityRepositoryMock.findAll()).thenReturn(Arrays.asList());

        List<Charity> actualCharities = getCharitiesUseCase.getCharities();

        assertNotNull(actualCharities);
        assertTrue(actualCharities.isEmpty());

        verify(charityRepositoryMock, times(1)).findAll();
    }
}
