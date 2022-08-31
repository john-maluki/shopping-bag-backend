package dev.johnmaluki.shoppingbagbackend.repository;

import dev.johnmaluki.shoppingbagbackend.entity.Brand;
import dev.johnmaluki.shoppingbagbackend.entity.Product;
import dev.johnmaluki.shoppingbagbackend.entity.ProductType;
import dev.johnmaluki.shoppingbagbackend.exception.NotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@DirtiesContext
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Autowired
    private ShopProductRepository shopProductRepository;

    @Test
    @Order(1)
    @Rollback(value = false)
    @DisplayName("[Repository] Test saving product record")
    public void saveProductTest(){
        final String productName = "HP 440";
        final String productDescription = "4gb ram and 500gb disk space";
        createBrand();
        createProductTypes();
        final long brandId = 1l;
        Brand brand = getBrand(brandId);
        Set<ProductType> productTypes = getProductTypes();
        Product product = Product.builder()
                .name(productName)
                .brand(brand)
                .productTypes(productTypes)
                .description(productDescription)
                .build();
        productRepository.save(product);

        final long productId = 1l;
        Product savedProduct = productRepository.findById(productId).get();

        assertThat(savedProduct.getId()).isEqualTo(productId);
        assertThat(savedProduct.getBrand()).isEqualTo(brand);
        assertThat(savedProduct.getProductTypes())
                .hasSize(2).containsAll(productTypes);
        assertThat(savedProduct.getName()).
                isEqualToIgnoringCase(productName);
        assertThat(savedProduct.getDescription())
                .isEqualToIgnoringCase(productDescription);
    }

    @Test
    @Order(2)
    @Rollback(value = false)
    @DisplayName("[Repository] Test finding product record by id")
    public void findProductByIdTest(){
        final long productId = 1l;
        final String productName = "HP 440";
        final String productDescription = "4gb ram and 500gb disk space";

        Product savedProduct = productRepository.findById(productId).get();

        assertThat(savedProduct.getId()).isEqualTo(productId);
        assertThat(savedProduct.getName()).isEqualToIgnoringCase(productName);
        assertThat(savedProduct.getDescription()).isEqualToIgnoringCase(productDescription);
    }

    @Test
    @Order(3)
    @Rollback(value = false)
    @DisplayName("[Repository] Test fetching product record by id")
    void getProductById() {
        final long productId = 1l;
        final String productName = "HP 440";
        final String productDescription = "4gb ram and 500gb disk space";

        Product product = productRepository.getProductById(productId).get();

        assertThat(product.getId()).isEqualTo(productId);
        assertThat(product.getName()).isEqualToIgnoringCase(productName);
        assertThat(product.getDescription()).isEqualToIgnoringCase(productDescription);

    }

    @Test
    @Order(4)
    @Rollback(value = false)
    @DisplayName("[Repository] Test updating product record")
    public void updateProductTest(){
        final long productId = 1l;
        final String productName = "HP 444";
        final String productDescription = "8gb ram and 1tb disk space";
        Product savedProduct = productRepository.findById(productId).get();
        savedProduct.setName(productName);
        savedProduct.setDescription(productDescription);

        Product updatedProduct = productRepository.findById(productId).get();

        assertThat(updatedProduct.getId()).isEqualTo(productId);
        assertThat(updatedProduct.getName()).isEqualToIgnoringCase(productName);
        assertThat(updatedProduct.getDescription()).isEqualToIgnoringCase(productDescription);
    }

    @Test
    @Order(5)
    @Rollback(value = false)
    @DisplayName("[Repository] Test finding all product records")
    public void findAllProductTest(){
        List<Product> savedProducts = productRepository.findAll();

        assertThat(savedProducts).hasSize(1);
    }

    @Test
    @Order(6)
    @Rollback(value = false)
    @DisplayName("[Repository] Test deleting product record")
    public void deleteProductTest(){
        final long productId = 1l;
        Product product = productRepository.findById(productId).get();
        boolean existsInShopProduct = itExists(product);

        if(!existsInShopProduct){
            productRepository.deleteById(productId);
        }

        final String errorMessage = "Product not found!";
        Throwable productException = catchThrowable(()-> {
            productRepository.findById(productId).orElseThrow(
                    () -> new NotFoundException(errorMessage)
            );
        });
        assertThat(productException).isNotNull();
        assertThat(productException).isInstanceOf(NotFoundException.class)
                .hasMessage(errorMessage);

        // Deleting product should not delete associated brand
        assertThatCode(
                () -> {
                    final long brandId = 1l;
                    brandRepository.findById(brandId).get();
                }
        ).doesNotThrowAnyException();

        // Deleting product should not delete associated product_types
        List<ProductType> productTypes = productTypeRepository.findAll();
        assertThat(productTypes).hasSize(2);
    }

    private void createBrand(){
        //Should be called one for this class tests
        final String brandName = "Hp".toUpperCase();
        Brand brand = Brand.builder()
                .name(brandName)
                .build();
        brandRepository.save(brand);
    }

    private Brand getBrand(long brandId){
        return brandRepository.findById(brandId).get();
    }

    private void createProductTypes(){
        //Should be called one for this class tests
        final String productTypeName1 = "Laptops".toUpperCase();
        final String productTypeName2 = "Phones".toUpperCase();
        ProductType productType1 = ProductType.builder()
                .name(productTypeName1)
                .build();
        ProductType productType2 = ProductType.builder()
                .name(productTypeName2)
                .build();
        productTypeRepository.saveAll(List.of(productType1, productType2));
    }

    private Set<ProductType> getProductTypes(){
        return new HashSet<>(productTypeRepository.findAll());
    }

    private boolean itExists(Product product){
        return shopProductRepository.existsByProduct(product);
    }

}