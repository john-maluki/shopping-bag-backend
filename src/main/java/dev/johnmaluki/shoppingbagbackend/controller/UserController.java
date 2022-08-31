package dev.johnmaluki.shoppingbagbackend.controller;

import dev.johnmaluki.shoppingbagbackend.Dto.UserDto;
import dev.johnmaluki.shoppingbagbackend.entity.User;
import dev.johnmaluki.shoppingbagbackend.service.UserRegistrationService;
import dev.johnmaluki.shoppingbagbackend.service.UserService;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@RestController
@CrossOrigin
@RequestMapping(value = "v1/user")
public class UserController {
    private final UserRegistrationService userRegistrationService;
    private final UserService userService;

    private final ModelMapper modelMapper;

    public UserController(UserRegistrationService userRegistrationService, UserService userService, ModelMapper modelMapper) {
        this.userRegistrationService = userRegistrationService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<UserDto> registerUser(
            @Valid @RequestBody UserRegistrationData userRegistrationData
    ){

        User user = userRegistrationService.registerUser(
                userRegistrationData.username,
                userRegistrationData.password
        );
        UserDto userDto =
                modelMapper.map(user, UserDto.class);

        return ResponseEntity.ok().body(userDto);
    }

    @PatchMapping(value = "/register")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto){
        User user = modelMapper.map(userDto, User.class);
        User receivedUser = userService.updateUser(user);

        UserDto receivedUserDto =
                modelMapper.map(receivedUser, UserDto.class);
        return ResponseEntity.ok().body(receivedUserDto);
    }

    @GetMapping(value = "/refresh_token")
    public ResponseEntity<?> refreshToken(){
     return ResponseEntity.ok().build();
    }

    @Data
    private static class UserRegistrationData implements Serializable {
        @NotEmpty(message = "username required")
        @Email(message = "Please provide a valid email")
        private String username;
        @NotEmpty(message = "password required")
        private String password;

        public UserRegistrationData(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
}
