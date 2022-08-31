package dev.johnmaluki.shoppingbagbackend.Dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Getter
@Setter
public class LoginDto implements Serializable {
    @NotEmpty(message = "username required")
    @Email(message = "Please provide a valid email")
    private String username;

    @NotEmpty(message = "password required")
    private String password;
}
