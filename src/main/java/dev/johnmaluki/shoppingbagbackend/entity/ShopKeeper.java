package dev.johnmaluki.shoppingbagbackend.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "user")
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
    private long shopKeeperId;

    @OneToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "userId"
    )
    private User shopOwner;

}
