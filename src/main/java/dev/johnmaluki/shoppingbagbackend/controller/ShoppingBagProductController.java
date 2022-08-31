package dev.johnmaluki.shoppingbagbackend.controller;

import dev.johnmaluki.shoppingbagbackend.Dto.ShoppingBagProductDto;
import dev.johnmaluki.shoppingbagbackend.entity.ShoppingBagProduct;
import dev.johnmaluki.shoppingbagbackend.service.ShoppingBagProductService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/shopping_bag_product")
public class ShoppingBagProductController {
    private final ShoppingBagProductService shoppingBagProductService;

    private final ModelMapper modelMapper;

    public ShoppingBagProductController(ShoppingBagProductService shoppingBagProductService, ModelMapper modelMapper) {
        this.shoppingBagProductService = shoppingBagProductService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{shoppingBagId}")
    public ResponseEntity<List<ShoppingBagProductDto>> getAllShoppingBagProducts(@PathVariable("shoppingBagId") long shoppingBagId){
        List<ShoppingBagProduct> shoppingBagProducts = shoppingBagProductService
                .findAllShoppingBagProducts(shoppingBagId);
        List<ShoppingBagProductDto>  shoppingBagProductDtoList = shoppingBagProducts.stream()
                .map(product -> modelMapper.map(product, ShoppingBagProductDto.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(shoppingBagProductDtoList);
    }

    @PatchMapping("/{shoppingBagProductId}")
    public ResponseEntity<ShoppingBagProductDto> updateShoppingBagProductQuantity(
            @PathVariable("shoppingBagProductId") long shoppingBagProductId,
            @RequestBody ShoppingBagProductDto shoppingBagProductDto){

        ShoppingBagProduct shoppingBagProduct = shoppingBagProductService
                .updateShoppingBagProductQuantity(shoppingBagProductId, shoppingBagProductDto.getQuantity());
        shoppingBagProductDto = modelMapper.map(shoppingBagProduct, ShoppingBagProductDto.class);

        return ResponseEntity.ok().body(shoppingBagProductDto);
    }
}
