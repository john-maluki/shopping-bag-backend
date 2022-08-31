package dev.johnmaluki.shoppingbagbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

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
    private long id;

    @Column(name = "description")
    private String description;

    @Column(name = "date_created", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime dateCreated = LocalDateTime.now();

    @JsonIgnore
    @ManyToOne(cascade = {
            CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.DETACH, CascadeType.REFRESH,
    })
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id"
    )
    private User owner;

    @Column(name = "shopping_status")
    @Basic
    private int shoppingStatus;

    @Transient
    private ShoppingProcessStatus shoppingProcessStatus;

    @Transient
    private BigDecimal totalProductPrice;

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "shoppingBag")
    private Set<ShoppingBagProduct> shoppingBagProducts = new HashSet<>();

    @PostLoad
    private void fillTransient() {
        if (shoppingStatus > 0) {
            this.shoppingProcessStatus = ShoppingProcessStatus.of(shoppingStatus);
        }
    }

    @PrePersist
    private void fillPersistent() {
        if (shoppingProcessStatus != null) {
            this.shoppingStatus = shoppingProcessStatus
                    .getProcessStatusValue();
        }
    }

    @Getter
    public enum ShoppingProcessStatus {
        ONGOING(1), COMPLETE(2);

        final private int processStatusValue;

        ShoppingProcessStatus(int processStatusValue){
            this.processStatusValue = processStatusValue;
        }

        public static ShoppingProcessStatus of(int shoppingProcessStatus){
            return Stream.of(ShoppingProcessStatus.values())
                    .filter(status -> status.getProcessStatusValue() == shoppingProcessStatus)
                    .findFirst().orElseThrow(IllegalArgumentException::new);
        }
    }


}
