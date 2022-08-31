package dev.johnmaluki.shoppingbagbackend.Dto;

import lombok.Data;

@Data
public class LocationDto {
    private long id;
    private String state;
    private String town;
    private String street;
    private String description;
}
