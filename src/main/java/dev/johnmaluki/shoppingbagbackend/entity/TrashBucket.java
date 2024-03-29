package dev.johnmaluki.shoppingbagbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private long id;
    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(
            name = "user_trash_id",
            referencedColumnName = "id"
    )
    private UserTrash userTrash;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(
            name = "shopping_bag_id",
            referencedColumnName = "id"
    )
    private ShoppingBag shoppingBag;
}
