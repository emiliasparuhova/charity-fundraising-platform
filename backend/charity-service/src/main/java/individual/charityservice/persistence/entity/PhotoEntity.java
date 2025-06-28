package individual.charityservice.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "photo", schema = "public")
@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PhotoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long photoId;

    @Column(name = "photo_data", columnDefinition = "bytea")
    private byte[] photoData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charity_id", nullable = false)
    private CharityEntity charity;
}
