package dev.johnmaluki.shoppingbagbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "trash_bucket")
public class TrashBucket {
    @Id
    @SequenceGenerator(
            name = "trash_bucket_sequence",
            sequenceName = "trash_bucket_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "trash_bucket_sequence"
    )
    @Column(name = "trash_bucket_id")
    private long trashBucketId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "shopping_bag_id",
            referencedColumnName = "shoppingBagId"
    )
    private ShoppingBag shoppingBag;
}
