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
@Table(name = "location")
public class Location {
    @Id
    @SequenceGenerator(
            name = "location_sequence",
            sequenceName = "location_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "location_sequence"
    )
    private long locationId;

    @Column(name = "state")
    private String state;

    @Column( name = "town")
    private String town;

    @Column(name = "street")
    private String street;

    @Column(name = "shop_location_description")
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "shop_id",
            referencedColumnName = "shopId"
    )
    private Shop shop;
}
