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
@Table(name = "product")
public class Product {
    @Id
    @SequenceGenerator(
            name = "product_sequence",
            sequenceName = "product_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_sequence"
    )
    private long productId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "product_brand",
            referencedColumnName = "brandId"
    )
    private Brand brand;

    @Column(
            name = "product_name",
            nullable = false
    )
    private String name;

    @Column(name = "product_description")
    private String description;

    @Column(name = "product_img")
    private String picture;

    @Column(name = "product_sku")
    private String sku;

    @Column(name = "model_number")
    private String modelNumber;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            joinColumns = @JoinColumn(
                    name = "product_id",
                    referencedColumnName = "productId"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "product_type_id",
                    referencedColumnName = "productTypeId"
            )
    )
    private List<ProductType> productType;
}
