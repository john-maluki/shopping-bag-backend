package dev.johnmaluki.shoppingbagbackend.Dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDto implements Serializable {
    private long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email; // if not present defaults to username
    private String username; // email allowed
    private String mobileNumber;
}
