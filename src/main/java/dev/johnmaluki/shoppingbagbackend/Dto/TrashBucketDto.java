package dev.johnmaluki.shoppingbagbackend.Dto;

import dev.johnmaluki.shoppingbagbackend.entity.ShoppingBag;
import lombok.Data;

@Data
public class TrashBucketDto {
    private long trashBucketId;
    private ShoppingBag shoppingBag;
}
