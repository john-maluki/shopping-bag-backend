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
    private long shoppingBagProductId;

    @Column(name = "shopping_bag_id")
    private long shoppingBag;

    @Column(name = "shop_product_id")
    private long shopProduct;

    @Column(name = "quantity")
    private int quantity;
}
