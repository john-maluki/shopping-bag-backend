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
@Table(name = "shop_product")
public class ShopProduct {
    @Id
    @SequenceGenerator(
            name = "shop_product_sequence",
            sequenceName = "shop_product_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "shop_product_sequence"
    )
    private long shopProductId;

    @Column(name = "shop_id")
    private long shop;

    @Column(name = "product_id")
    private long product;

    @Column(name = "shop_product_price")
    private int price;

}
