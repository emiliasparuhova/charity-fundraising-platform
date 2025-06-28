package individual.charityservice.business.converter;

import individual.charityservice.persistence.entity.CharityEntity;
import individual.charityservice.persistence.entity.PhotoEntity;

import java.util.Objects;

public class PhotoConverter {
    private PhotoConverter() {}

    public static byte[] convertToByteArray(PhotoEntity photoEntity) {
        if (Objects.isNull(photoEntity)){
            return new byte[0];
        }

        return photoEntity.getPhotoData();
    }

    public static PhotoEntity convertToEntity(byte[] photoData, CharityEntity charityEntity) {
        if (Objects.isNull(photoData)) {
            return PhotoEntity.builder().build();
        }

        return PhotoEntity.builder()
                .photoData(photoData)
                .charity(charityEntity)
                .build();
    }
}
