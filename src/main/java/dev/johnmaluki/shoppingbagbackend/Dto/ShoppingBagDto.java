package dev.johnmaluki.shoppingbagbackend.Dto;

import dev.johnmaluki.shoppingbagbackend.entity.ShoppingBag;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShoppingBagDto {
    private long id;
    private String description;
    private LocalDateTime dateCreated;
    private ShoppingBag.ShoppingProcessStatus shoppingProcessStatus;
}
