package individual.donationservice.controller;

import individual.donationservice.business.donation.CreateDonationUseCase;
import individual.donationservice.business.donation.GetDonationStatsByCharity;
import individual.donationservice.controller.converter.DonationRequestConverter;
import individual.donationservice.controller.dto.donation.CreateDonationRequest;
import individual.donationservice.controller.dto.donation.CreateDonationResponse;
import individual.donationservice.controller.dto.donation.GetDonationStatsForCharityResponse;
import individual.donationservice.domain.Donation;
import individual.donationservice.domain.DonationStats;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/donations")
@AllArgsConstructor
public class DonationController {

    private final CreateDonationUseCase createDonationUseCase;
    private final GetDonationStatsByCharity getDonationStatsByCharity;

    @PostMapping
    public ResponseEntity<CreateDonationResponse> createDonation(@RequestBody @Valid CreateDonationRequest request) {
        try {
            final Donation donation = DonationRequestConverter.convertCreateRequest(request);

            final CreateDonationResponse response = DonationRequestConverter
                    .convertCreateResponse(createDonationUseCase.createDonation(donation));

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/charity/{id}")
    public ResponseEntity<GetDonationStatsForCharityResponse> getDonationStatsForCharity(@PathVariable(value = "id") Long id){
        try{
            final DonationStats stats = getDonationStatsByCharity.getDonationStats(id);

            final GetDonationStatsForCharityResponse response =
                    DonationRequestConverter.convertGetDonationStatsForCharityResponse(stats);

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
