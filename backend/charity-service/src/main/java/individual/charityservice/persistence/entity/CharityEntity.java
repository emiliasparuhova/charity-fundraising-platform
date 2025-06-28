package individual.charityservice.persistence.entity;

import individual.charityservice.domain.enums.CharityCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "charity", schema = "public")
@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CharityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "fundraising_goal", nullable = false)
    private double fundraisingGoal;

    @Column(name = "creator_id", nullable = false)
    private Long creatorId;

    @Column(name = "paypal_email", nullable = false)
    private String paypalEmail;

    @Column(name = "creation_date", columnDefinition = "DATE")
    private LocalDate creationDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "charity", fetch = FetchType.EAGER)
    private List<PhotoEntity> photos;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private CharityCategory category;

    @Column(name = "is_active")
    private Boolean isActive;
}
