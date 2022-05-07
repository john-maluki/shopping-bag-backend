package dev.johnmaluki.shoppingbagbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "shopping_bag")
public class ShoppingBag {
    @Id
    @SequenceGenerator(
            name = "shopping_bag_sequence",
            sequenceName = "shopping_bag_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "shopping_bag_sequence"
    )
    private long shoppingBagId;

    @Column(name = "shop_product_id")
    private long shopProductId;

    @Column(name = "creation_date")
    private Date creationDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "shopping_bag_owner",
            referencedColumnName = "userId"
    )
    private User owner;

    @Column(name = "shopping_status")
    private String shoppingStatus;

    @Column(name = "total_product_price")
    private String totalProductPrice;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "shopping_bag_product",
            joinColumns = @JoinColumn(
                    name = "shopping_bag_id",
                    referencedColumnName = "shoppingBagId"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "shop_product_id",
                    referencedColumnName = "shopProductId"
            )
    )
    private List<ShopProduct> shopProduct;
}
