package individual.charityservice.business.converter;

import individual.charityservice.domain.Charity;
import individual.charityservice.persistence.entity.CharityEntity;

import java.util.Objects;
import java.util.stream.Collectors;

public final class CharityConverter {

    private CharityConverter() {}

    public static Charity convertToDomain(CharityEntity charityEntity) {
        if (Objects.isNull(charityEntity)){
            return Charity.builder().build();
        }

        return Charity.builder()
                .id(charityEntity.getId())
                .title(charityEntity.getTitle())
                .description(charityEntity.getDescription())
                .fundraisingGoal(charityEntity.getFundraisingGoal())
                .creatorId(charityEntity.getCreatorId())
                .paypalEmail(charityEntity.getPaypalEmail())
                .creationDate(charityEntity.getCreationDate())
                .photos(charityEntity.getPhotos().stream()
                        .map(PhotoConverter::convertToByteArray)
                        .collect(Collectors.toList()))
                .category(charityEntity.getCategory())
                .isActive(charityEntity.getIsActive())
                .build();
    }

    public static CharityEntity convertToEntity(Charity charity) {
        if (Objects.isNull(charity)){
            return CharityEntity.builder().build();
        }

        CharityEntity charityEntity = CharityEntity.builder()
                .id(charity.getId())
                .title(charity.getTitle())
                .description(charity.getDescription())
                .fundraisingGoal(charity.getFundraisingGoal())
                .creatorId(charity.getCreatorId())
                .paypalEmail(charity.getPaypalEmail())
                .creationDate(charity.getCreationDate())
                .category(charity.getCategory())
                .isActive(charity.getIsActive())
                .build();

        charityEntity.setPhotos(charity.getPhotos().stream()
                .map(photo -> PhotoConverter.convertToEntity(photo, charityEntity))
                .collect(Collectors.toList()));

        return charityEntity;
    }
}
