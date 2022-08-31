package dev.johnmaluki.shoppingbagbackend.Dto;

import dev.johnmaluki.shoppingbagbackend.entity.Location;
import dev.johnmaluki.shoppingbagbackend.entity.ShopContact;
import lombok.Data;

@Data
public class ShopDto {
    private long id;
    private String name;
    private String description;
    private ShopContact shopContact;
    private Location location;
    private boolean isOpen;
    private boolean isActivated;
}
