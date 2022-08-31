package dev.johnmaluki.shoppingbagbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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
    private long id;

    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(
            name = "product_brand",
            referencedColumnName = "id"
    )
    private Brand brand;

    @Column(
            name = "product_name",
            nullable = false
    )
    private String name;

    @Column(name = "product_description")
    private String description;

    @Column(name = "product_img_code")
    private String imageCode;

    @Column(name = "product_sku")
    private String sku;

    @Column(name = "model_number")
    private String modelNumber;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            joinColumns = @JoinColumn(
                    name = "product_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "product_type_id",
                    referencedColumnName = "id"
            )
    )
    private Set<ProductType> productTypes = new HashSet<>();

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private Set<ShopProduct> shopProducts = new HashSet<>();

    @Transient
    private String imageUrl;
}
