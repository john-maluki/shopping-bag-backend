package dev.johnmaluki.shoppingbagbackend.controller;

import dev.johnmaluki.shoppingbagbackend.Dto.ShopProductDto;
import dev.johnmaluki.shoppingbagbackend.service.ShopProductService;
import dev.johnmaluki.shoppingbagbackend.util.FileDownloadUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping(value = "/v1/shop_products")
public class ShopProductController {
    private final ShopProductService shopProductService;

    private final ModelMapper modelMapper;

    @Autowired
    public ShopProductController(ShopProductService shopProductService, ModelMapper modelMapper) {
        this.shopProductService = shopProductService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(value = "/{shopId}")
    public ResponseEntity<List<ShopProductDto>> getShopProducts(@PathVariable("shopId") long shopId){
        List<ShopProductDto> shopProducts = shopProductService.getShopProducts(shopId)
                .stream().map(shopProduct -> modelMapper.map(shopProduct, ShopProductDto.class))
                .collect(Collectors.toList());
        createProductImageUrls(shopProducts);
        return ResponseEntity.ok().body(shopProducts);
    }

    private void createProductImageUrls(List<ShopProductDto> shopProductDtoList){
        shopProductDtoList.forEach(shopProductDto -> {
            try {
                Resource resource =  FileDownloadUtils.findResourceStartingWithFileCode(shopProductDto.getProduct().getImageCode());
                String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/v1/product_image/download/")
                        .path(Objects.requireNonNull(resource.getFilename()))
                        .toUriString();
                shopProductDto.getProduct().setImageUrl(url);
            } catch (IOException e) {
                shopProductDto.getProduct().setImageUrl(null);
            }
        });
    }
}
