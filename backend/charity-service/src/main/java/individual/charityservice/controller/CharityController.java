package individual.charityservice.controller;

import individual.charityservice.business.charity.CreateCharityUseCase;
import individual.charityservice.business.charity.GetCharitiesUseCase;
import individual.charityservice.business.charity.GetCharityByIdUseCase;
import individual.charityservice.business.charity.exception.CharityNotFoundException;
import individual.charityservice.controller.converter.CharityRequestConverter;
import individual.charityservice.controller.dto.charity.CreateCharityRequest;
import individual.charityservice.controller.dto.charity.CreateCharityResponse;
import individual.charityservice.controller.dto.charity.GetCharitiesResponse;
import individual.charityservice.controller.dto.charity.GetCharityResponse;
import individual.charityservice.domain.Charity;
import individual.charityservice.domain.enums.UserRole;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/charities")
@AllArgsConstructor
public class CharityController {
    private final CreateCharityUseCase createCharityUseCase;
    private final GetCharitiesUseCase getCharitiesUseCase;
    private final GetCharityByIdUseCase getCharityByIdUseCase;

    @GetMapping
    public ResponseEntity<GetCharitiesResponse> getCharities() {
        try {
            final GetCharitiesResponse response = CharityRequestConverter
                    .convertGetCharitiesResponse(getCharitiesUseCase.getCharities());

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<GetCharityResponse> getCharity(@PathVariable(value = "id") Long id){
        try{
            final Charity charity = getCharityByIdUseCase.getCharity(id);

            final GetCharityResponse response = CharityRequestConverter.convertGetCharityResponse(charity);

            return ResponseEntity.ok().body(response);
        } catch (CharityNotFoundException e){
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<CreateCharityResponse> createCharity(@RequestBody @Valid CreateCharityRequest request,
                                                               @RequestHeader("X-Role") String role) {
        try {
            if (!UserRole.USER.name().equalsIgnoreCase(role)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            final Charity charity = CharityRequestConverter.convertCreateRequest(request);

            final CreateCharityResponse response = CharityRequestConverter
                    .convertCreateResponse(createCharityUseCase.createCharity(charity));

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
