package dev.johnmaluki.shoppingbagbackend.service;

import dev.johnmaluki.shoppingbagbackend.entity.Product;
import dev.johnmaluki.shoppingbagbackend.exception.NotFoundException;
import dev.johnmaluki.shoppingbagbackend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getSystemProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(long productId) {

        return productRepository.getProductById(productId).orElseThrow(
                ()-> new NotFoundException("Product with id {%d} not found".formatted(productId))
        );
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

}
