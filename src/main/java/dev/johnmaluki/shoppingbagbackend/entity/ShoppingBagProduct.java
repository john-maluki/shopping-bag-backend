package dev.johnmaluki.shoppingbagbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "shopping_bag_product")
public class ShoppingBagProduct {
    @Id
    @SequenceGenerator(
            name = "shopping_bag_product_sequence",
            sequenceName = "shopping_bag_product_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "shopping_bag_product_sequence"
    )
    private long id;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "shopping_bag_id")
    private ShoppingBag shoppingBag;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "shop_product_id")
    private ShopProduct shopProduct;

    @Column(name = "quantity")
    private int quantity;
}
