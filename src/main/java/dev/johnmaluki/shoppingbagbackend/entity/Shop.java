package dev.johnmaluki.shoppingbagbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
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
    private long shopId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "shop_keeper",
            referencedColumnName = "shopKeeperId"
    )
    private ShopKeeper shopKeeper;

    @Column(name = "shop_name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "shop_location",
            referencedColumnName = "locationId"
    )
    private Location location;

    @Column(name = "shop_description")
    private String description;

    @Embedded
    private ShopContact shopContact;

    @Column(name = "is_open")
    private boolean isOpen;

    @Column(name = "is_closed")
    private boolean isClosed;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "shop_product",
            joinColumns = @JoinColumn(
                    name = "shop_id",
                    referencedColumnName = "shopId"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "product_id",
                    referencedColumnName = "productId"
            )

    )
    private List<Product> product;
}
