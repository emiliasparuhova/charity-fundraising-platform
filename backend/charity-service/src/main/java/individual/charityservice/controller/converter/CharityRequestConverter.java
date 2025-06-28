package individual.charityservice.controller.converter;

import individual.charityservice.controller.dto.charity.CreateCharityRequest;
import individual.charityservice.controller.dto.charity.CreateCharityResponse;
import individual.charityservice.controller.dto.charity.GetCharitiesResponse;
import individual.charityservice.controller.dto.charity.GetCharityResponse;
import individual.charityservice.domain.Charity;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public final class CharityRequestConverter {
    private CharityRequestConverter() {}

    public static Charity convertCreateRequest(CreateCharityRequest createCharityRequest) {
        List<byte[]> decodedPhotos = createCharityRequest.getPhotos().stream()
                .map(photoBase64 -> Base64.getDecoder().decode(photoBase64))
                .collect(Collectors.toList());

        return Charity.builder()
                .title(createCharityRequest.getTitle())
                .description(createCharityRequest.getDescription())
                .fundraisingGoal(createCharityRequest.getFundraisingGoal())
                .creatorId(createCharityRequest.getCreatorId())
                .paypalEmail(createCharityRequest.getPaypalEmail())
                .photos(decodedPhotos)
                .category(createCharityRequest.getCategory())
                .build();
    }

    public static CreateCharityResponse convertCreateResponse(Long id) {
        return CreateCharityResponse.builder()
                .id(id)
                .build();
    }

    public static GetCharitiesResponse convertGetCharitiesResponse(List<Charity> charityList) {
        return GetCharitiesResponse.builder()
                .charities(charityList)
                .build();
    }

    public static GetCharityResponse convertGetCharityResponse(Charity charity) {
        return GetCharityResponse.builder()
                .id(charity.getId())
                .title(charity.getTitle())
                .description(charity.getDescription())
                .fundraisingGoal(charity.getFundraisingGoal())
                .creatorId(charity.getCreatorId())
                .paypalEmail(charity.getPaypalEmail())
                .creationDate(charity.getCreationDate())
                .photos(charity.getPhotos())
                .category(charity.getCategory())
                .isActive(charity.getIsActive())
                .build();
    }
}
