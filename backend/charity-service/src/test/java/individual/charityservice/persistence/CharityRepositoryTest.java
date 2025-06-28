package individual.charityservice.persistence;

import individual.charityservice.persistence.entity.CharityEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class CharityRepositoryTest {

    @Autowired
    private CharityRepository charityRepository;

    @BeforeEach
    void setUp() {
        CharityEntity charity1 = CharityEntity.builder()
                .title("Charity One")
                .description("Helping children")
                .creatorId(123L)
                .creationDate(LocalDate.now())
                .isActive(true)
                .photos(List.of())
                .paypalEmail("email@email.com")
                .build();

        CharityEntity charity2 = CharityEntity.builder()
                .title("Charity Two")
                .description("Animal rescue")
                .creatorId(123L)
                .creationDate(LocalDate.now())
                .isActive(true)
                .photos(List.of())
                .paypalEmail("email@email.com")
                .build();

        charityRepository.saveAll(List.of(charity1, charity2));
    }

    @Test
    void findAllByCreatorId_shouldReturnAllCharitiesForGivenCreator() {
        List<CharityEntity> result = charityRepository.findAllByCreatorId(123L);

        assertEquals(2, result.size(), "There should be 2 charities for creatorId 123.");
        assertTrue(result.stream().allMatch(c -> c.getCreatorId().equals(123L)), "All returned charities should belong to creatorId 123.");
    }

    @Test
    void findAllByCreatorId_shouldReturnEmptyListWhenNoMatch() {
        List<CharityEntity> result = charityRepository.findAllByCreatorId(999L);

        assertTrue(result.isEmpty(), "There should be no charities for creatorId 999.");
    }
}
