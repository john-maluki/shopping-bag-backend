package dev.johnmaluki.shoppingbagbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"location"})
@Builder
@Table(name = "shop")
public class Shop {
    @Id
    @SequenceGenerator(
            name = "shop_sequence",
            sequenceName = "shop_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "shop_sequence"
    )
    private long id;

    @Column(name = "shop_name")
    private String name;

    @Column(name = "shop_description")
    private String description;

    @Embedded
    private ShopContact shopContact;

    @JsonIgnore
    @OneToOne(mappedBy = "shop", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Location location;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(
            name = "shop_keeper_id",
            referencedColumnName = "id"

    )
    private ShopKeeper shopKeeper;

    @Column(name = "is_open")
    private boolean isOpen;

    @Column(name = "is_activated")
    private boolean isActivated;

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "shop")
    private Set<ShopProduct> shopProducts = new HashSet<>();

}
