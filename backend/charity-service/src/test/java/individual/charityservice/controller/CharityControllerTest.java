package individual.charityservice.controller;

import individual.charityservice.business.charity.CreateCharityUseCase;
import individual.charityservice.business.charity.GetCharitiesUseCase;
import individual.charityservice.business.charity.GetCharityByIdUseCase;
import individual.charityservice.business.charity.exception.CharityNotFoundException;
import individual.charityservice.domain.Charity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class CharityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateCharityUseCase createCharityUseCase;

    @MockitoBean
    private GetCharitiesUseCase getCharitiesUseCase;

    @MockitoBean
    private GetCharityByIdUseCase getCharityByIdUseCase;

    @Test
    void getCharities_shouldReturn200WithCharities_whenCharitiesFound() throws Exception {
        Charity charity = Charity.builder()
                .id(1L)
                .title("Charity Title")
                .description("Charity Description")
                .fundraisingGoal(10000.0)
                .creatorId(12345L)
                .creationDate(LocalDate.of(2025, 3, 25))
                .build();

        when(getCharitiesUseCase.getCharities()).thenReturn(Arrays.asList(charity));

        mockMvc.perform(get("/charities"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.charities[0].id").value(1L))
                .andExpect(jsonPath("$.charities[0].title").value("Charity Title"))
                .andExpect(jsonPath("$.charities[0].description").value("Charity Description"))
                .andExpect(jsonPath("$.charities[0].fundraisingGoal").value(10000.0))
                .andExpect(jsonPath("$.charities[0].creatorId").value(12345L))
                .andExpect(jsonPath("$.charities[0].creationDate").value("2025-03-25"));
    }

    @Test
    void getCharityById_shouldReturn200WithCharity_whenCharityFound() throws Exception {
        Charity charity = Charity.builder()
                .id(1L)
                .title("Charity Title")
                .description("Charity Description")
                .fundraisingGoal(10000.0)
                .creatorId(12345L)
                .creationDate(LocalDate.of(2025, 3, 25))
                .build();

        when(getCharityByIdUseCase.getCharity(1L)).thenReturn(charity);

        mockMvc.perform(get("/charities/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Charity Title"))
                .andExpect(jsonPath("$.description").value("Charity Description"))
                .andExpect(jsonPath("$.fundraisingGoal").value(10000.0))
                .andExpect(jsonPath("$.creatorId").value(12345L))
                .andExpect(jsonPath("$.creationDate").value("2025-03-25"));
    }

    @Test
    void getCharityById_shouldReturn404_whenCharityNotFound() throws Exception {
        when(getCharityByIdUseCase.getCharity(1L)).thenThrow(CharityNotFoundException.class);

        mockMvc.perform(get("/charities/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void createCharity_shouldReturn201WithCharityId_whenCharityCreated() throws Exception {
        when(createCharityUseCase.createCharity(any(Charity.class))).thenReturn(1L);

        mockMvc.perform(post("/charities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Role", "USER")
                        .content("""
                        {
                            "title": "Charity Title",
                            "description": "Charity Description",
                            "fundraisingGoal": 10000.0,
                            "creatorId": 12345,
                            "paypalEmail": "email@paypal.com",
                            "photos": [],
                            "category": "ANIMALS"
                        }
                    """)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L));
    }

}
