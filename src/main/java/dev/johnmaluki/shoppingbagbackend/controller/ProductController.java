package dev.johnmaluki.shoppingbagbackend.controller;

import dev.johnmaluki.shoppingbagbackend.Dto.ProductDto;
import dev.johnmaluki.shoppingbagbackend.entity.Product;
import dev.johnmaluki.shoppingbagbackend.service.ProductService;
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

@RestController
@CrossOrigin
@RequestMapping(value = "v1/products")
public class ProductController {
    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductController(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("")
    public ResponseEntity<List<ProductDto>>  getSystemProducts(){
        List<ProductDto> productDtoList = productService.getSystemProducts()
                .stream()
                .map(product -> this.modelMapper.map(product, ProductDto.class)).toList();
        this.createProductImageUrls(productDtoList);
        return ResponseEntity.ok().body(productDtoList);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDto>  getProduct(@PathVariable("id") long productId){
        Product product = productService.getProductById(productId);
        ProductDto productDto = this.modelMapper.map(product, ProductDto.class);
        return ResponseEntity.ok().body(productDto);
    }

    @PostMapping(value = "")
    public ResponseEntity<ProductDto>  createProduct(@RequestBody ProductDto productDto){
        Product mappedProduct = modelMapper.map(productDto, Product.class);
        Product product = productService.createProduct(mappedProduct);
        ProductDto receivedProductDto = this.modelMapper.map(product, ProductDto.class);
        return ResponseEntity.ok().body(receivedProductDto);
    }

    private void createProductImageUrls(List<ProductDto> productDtoList){
        productDtoList.forEach(productDto -> {
            try {
                Resource resource =  FileDownloadUtils.findResourceStartingWithFileCode(productDto.getImageCode());
                String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/v1/product_image/download/")
                        .path(Objects.requireNonNull(resource.getFilename()))
                        .toUriString();
                productDto.setImageUrl(url);
            } catch (IOException e) {
                productDto.setImageUrl(null);
            }
        });
    }
}
