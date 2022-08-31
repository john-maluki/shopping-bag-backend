package dev.johnmaluki.shoppingbagbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"shop", "product", "shoppingBagProducts"})
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
    private long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "shop_product_price")
    private BigDecimal price;

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "shopProduct")
    private Set<ShoppingBagProduct> shoppingBagProducts = new HashSet<>();

}
