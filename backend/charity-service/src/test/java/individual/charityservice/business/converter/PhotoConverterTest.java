package individual.charityservice.business.converter;

import individual.charityservice.persistence.entity.CharityEntity;
import individual.charityservice.persistence.entity.PhotoEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PhotoConverterTest {
    @Test
    void convertToByteArray_successful() {
        PhotoEntity mockPhotoEntity = mock(PhotoEntity.class);
        byte[] photoData = new byte[] {1, 2, 3, 4};
        when(mockPhotoEntity.getPhotoData()).thenReturn(photoData);

        byte[] result = PhotoConverter.convertToByteArray(mockPhotoEntity);

        assertArrayEquals(photoData, result);
    }

    @Test
    void convertToByteArray_emptyWhenEntityIsNull() {
        byte[] result = PhotoConverter.convertToByteArray(null);

        assertEquals(0, result.length);
    }

    @Test
    void convertToEntity_successful() {
        byte[] photoData = new byte[] {1, 2, 3, 4};
        CharityEntity mockCharityEntity = mock(CharityEntity.class);

        PhotoEntity photoEntity = PhotoConverter.convertToEntity(photoData, mockCharityEntity);

        assertNotNull(photoEntity);
        assertArrayEquals(photoData, photoEntity.getPhotoData());
        assertEquals(mockCharityEntity, photoEntity.getCharity());
    }

    @Test
    void convertToEntity_emptyWhenPhotoDataIsNull() {
        CharityEntity mockCharityEntity = mock(CharityEntity.class);

        PhotoEntity photoEntity = PhotoConverter.convertToEntity(null, mockCharityEntity);

        assertNotNull(photoEntity);
        assertNull(photoEntity.getPhotoData());
        assertNull(photoEntity.getCharity());
    }

}