package dev.johnmaluki.shoppingbagbackend.controller;

import dev.johnmaluki.shoppingbagbackend.Dto.ShoppingBagDto;
import dev.johnmaluki.shoppingbagbackend.util.FailureMessage;
import dev.johnmaluki.shoppingbagbackend.util.Message;
import dev.johnmaluki.shoppingbagbackend.util.SuccessMessage;
import dev.johnmaluki.shoppingbagbackend.entity.ShoppingBag;
import dev.johnmaluki.shoppingbagbackend.service.ShoppingBagService;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/shopping_bag")
public class ShoppingBagController {
    private final ShoppingBagService shoppingBagService;

    private final ModelMapper modelMapper;

    @Autowired
    public ShoppingBagController(ShoppingBagService shoppingBagService, ModelMapper modelMapper) {
        this.shoppingBagService = shoppingBagService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(value = "/user_shopping_bags/{id}")
    public ResponseEntity<List<ShoppingBagDto>> getUserShoppingBags(
            @PathVariable("id") long userId) {
        List<ShoppingBagDto> shoppingBags = shoppingBagService
                .getUserShoppingBags(userId)
                .stream().map(shoppingBag -> modelMapper
                        .map(shoppingBag, ShoppingBagDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(shoppingBags);
    }

    @GetMapping(value = "/user_trashed_bags/{id}")
    public ResponseEntity<List<ShoppingBagDto>> getUserTrashedShoppingBags(
            @PathVariable("id") long userId) {
        List<ShoppingBagDto> shoppingBags = shoppingBagService
                .getUserTrashedShoppingBags(userId)
                .stream().map(shoppingBag -> modelMapper
                        .map(shoppingBag, ShoppingBagDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(shoppingBags);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ShoppingBagDto> getShoppingBag(@PathVariable("id") long shoppingId) {
        ShoppingBag shoppingBag = shoppingBagService.getShoppingBag(shoppingId);
        ShoppingBagDto shoppingBagDto = modelMapper.map(shoppingBag, ShoppingBagDto.class);

        return ResponseEntity.ok().body(shoppingBagDto);
    }

    @PostMapping(value = "")
    public ResponseEntity<Message> trashShoppingBag(
            @RequestBody UserAndShoppingBag userAndShoppingBag) {
        boolean isTrashed = shoppingBagService
                .trashShoppingBag(
                        userAndShoppingBag.getUserId(),
                        userAndShoppingBag.getShoppingBagId()
                );
        Message message = isTrashed
                ? new SuccessMessage("Trashing process succeeded")
                : new FailureMessage("Trashing process failed!");

        return ResponseEntity.status(
                message.getHttpStatus())
                .body(message);
    }

    @DeleteMapping(value = "/undo")
    public ResponseEntity<Message> undoTrashedShoppingBag(
            @RequestBody UserAndShoppingBag userAndShoppingBag) {
        boolean isUnTrashed =  shoppingBagService
                .undoTrashedShoppingBag(
                        userAndShoppingBag.getUserId(),
                        userAndShoppingBag.getShoppingBagId()
                );
        Message message = isUnTrashed
                ? new SuccessMessage("Undoing process succeeded")
                : new FailureMessage("Undoing process failed!");
        return ResponseEntity.status(message.getHttpStatus())
                .body(message);
    }

    @Data
    private static class UserAndShoppingBag {
        private long userId;
        private long shoppingBagId;
    }
}
