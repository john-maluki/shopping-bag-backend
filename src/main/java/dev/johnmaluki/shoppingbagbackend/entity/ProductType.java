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
@Table(name = "product_type")
public class ProductType {
    @Id
    @SequenceGenerator(
            name = "product_type_sequence",
            sequenceName = "product_type_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_type_sequence"
    )
    private long id;

    @Column(
            name = "product_type_name",
            nullable = false
    )
    private String name;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(
            name = "category_id",
            referencedColumnName = "id"
    )
    private Category category;
}
