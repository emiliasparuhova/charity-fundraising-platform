package individual.donationservice.controller;

import individual.donationservice.business.donation.CreateDonationUseCase;
import individual.donationservice.business.donation.GetDonationStatsByCharity;
import individual.donationservice.domain.Donation;
import individual.donationservice.domain.DonationStats;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class DonationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateDonationUseCase createDonationUseCase;

    @MockitoBean
    private GetDonationStatsByCharity getDonationStatsByCharity;

    @Test
    void createDonation_shouldReturn201_whenDonationCreated() throws Exception {
        when(createDonationUseCase.createDonation(any(Donation.class))).thenReturn(1L);

        mockMvc.perform(post("/donations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "amount": 100.0,
                            "charityId": 1,
                            "donorId": 2
                        }
                    """))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void createDonation_shouldReturn500_whenErrorOccurs() throws Exception {
        when(createDonationUseCase.createDonation(any(Donation.class))).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(post("/donations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "amount": 100.0,
                            "charityId": 1,
                            "donorId": 2
                        }
                    """))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getDonationStatsForCharity_shouldReturn200_whenStatsFound() throws Exception {
        DonationStats stats = DonationStats.builder()
                .donationCount(100L)
                .totalFundsRaised(5000.0)
                .build();

        when(getDonationStatsByCharity.getDonationStats(1L)).thenReturn(stats);

        mockMvc.perform(get("/donations/charity/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.donationCount").value(100L))
                .andExpect(jsonPath("$.totalFundsRaised").value(5000.0));
    }

    @Test
    void getDonationStatsForCharity_shouldReturn500_whenErrorOccurs() throws Exception {
        when(getDonationStatsByCharity.getDonationStats(1L)).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/donations/charity/1"))
                .andExpect(status().isInternalServerError());
    }
}
