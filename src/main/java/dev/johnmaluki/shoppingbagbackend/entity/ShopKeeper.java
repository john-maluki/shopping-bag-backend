package dev.johnmaluki.shoppingbagbackend.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"user", "shops"})
@Table(name = "shop_keeper")
public class ShopKeeper {
    @Id
    @SequenceGenerator(
            name = "shop_keeper_sequence",
            sequenceName = "shop_keeper_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "shop_keeper_sequence"
    )
    private long id;

    @Getter(AccessLevel.NONE)
    @Builder.Default
    @OneToMany( mappedBy = "shopKeeper", cascade = CascadeType.ALL)
    private List<Shop> shops = new ArrayList<>();

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id"
    )
    private User user;
}
