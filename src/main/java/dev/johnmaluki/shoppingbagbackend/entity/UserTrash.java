package dev.johnmaluki.shoppingbagbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_trash")
public class UserTrash {
    @Id
    @SequenceGenerator(
            name = "user_trash_sequence",
            sequenceName = "user_trash_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_trash_sequence"
    )
    private long id;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id"
    )
    private User user;

    @OneToMany(mappedBy = "userTrash", cascade = CascadeType.ALL)
    @Builder.Default
    private List<TrashBucket> trashBuckets =  new ArrayList<>();
}
