package dev.johnmaluki.shoppingbagbackend.controller;

import dev.johnmaluki.shoppingbagbackend.Dto.ShopDto;
import dev.johnmaluki.shoppingbagbackend.entity.Shop;
import dev.johnmaluki.shoppingbagbackend.service.ShopService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/shops")
public class ShopController {
    private final ShopService shopService;
    private final ModelMapper modelMapper;

    @Autowired
    public ShopController(ShopService shopService, ModelMapper modelMapper) {
        this.shopService = shopService;
        this.modelMapper = modelMapper;
    }
    @GetMapping(value = "")
    public ResponseEntity<List<ShopDto>> getShops(){
        List<ShopDto> shops = shopService.getShops()
                .stream()
                .map(shop -> modelMapper.map(shop, ShopDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(shops);
    }

    @GetMapping(value = "/{shopId}")
    public ResponseEntity<ShopDto> getShop(@PathVariable("shopId") long shopId){
        Shop shop = shopService.getShop(shopId);
        ShopDto shopDto = modelMapper.map(shop, ShopDto.class);
        return ResponseEntity.ok().body(shopDto);
    }

    @GetMapping(value = "/sk/{id}")
    public ResponseEntity<List<ShopDto>> getUserShops(@PathVariable("id") long shopKeeperId){
        List<ShopDto> shops = shopService.getUserShops(shopKeeperId)
                .stream().
                map(shop -> modelMapper.map(shop, ShopDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(shops);
    }
}
