package dev.johnmaluki.shoppingbagbackend.repository;

import dev.johnmaluki.shoppingbagbackend.entity.Product;
import dev.johnmaluki.shoppingbagbackend.entity.Shop;
import dev.johnmaluki.shoppingbagbackend.entity.ShopContact;
import dev.johnmaluki.shoppingbagbackend.entity.ShopProduct;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DataJpaTest
@DirtiesContext
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ShopProductRepositoryTest {
    @Autowired
    private ShopProductRepository shopProductRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        ShopContact shopContact = ShopContact.builder()
                .contact("0111111111")
                .build();
        Shop shop = Shop.builder()
                .name("Electronic & Computer Dealer")
                .description("Number 1 electronic seller")
                .shopContact(shopContact)
                .isActivated(true)
                .isOpen(true)
                .build();
        shopRepository.save(shop);

        final String productName = "HP 440";
        final String productDescription = "4gb ram and 500gb disk space";
        Product product = Product.builder()
                .name(productName)
                .description(productDescription)
                .build();
        productRepository.save(product);
    }

    @Test
    @Order(1)
    @Rollback(value = false)
    @DisplayName("[Repository] Test saving shop product record")
    public void saveShopProductTest(){
        final long productId = 1l;
        final long shopId = 1l;
        Shop shop = shopRepository.findById(shopId).get();
        Product product = productRepository.findById(productId).get();
        final BigDecimal price = BigDecimal.valueOf(500);
        ShopProduct shopProduct = ShopProduct.builder()
                .product(product)
                .shop(shop)
                .price(price)
                .build();
        shopProductRepository.save(shopProduct);

        final long shopProductId = 1l;
        ShopProduct savedShoProduct = shopProductRepository.findById(shopProductId).get();

        assertThat(savedShoProduct.getShop()).isEqualTo(shop);
        assertThat(savedShoProduct.getProduct()).isEqualTo(product);
        assertThat(savedShoProduct.getPrice()).isEqualTo(price);
    }

    @Test
    @Order(2)
    @Rollback(value = false)
    @DisplayName("[Repository] Test fetching shop products records by shop_id")
    public void findShopProductsByShopIdTest(){
        final long productId = 1l;
        final long shopId = 1l;
        Shop shop = shopRepository.findById(shopId).get();
        Product product = productRepository.findById(productId).get();

        List<ShopProduct> shopProducts = shopProductRepository.findShopProductsByShopId(shopId);

        assertThat(shopProducts).hasSize(1);
        assertThat(shopProducts.get(0).getShop()).isEqualTo(shop);
        assertThat(shopProducts.get(0).getProduct()).isEqualTo(product);
    }

    @Test
    @Order(3)
    @Rollback(value = false)
    @DisplayName("[Repository] Test fetching shop products records by shop_id and product_id")
    public void findShopProductsByShopIdAndProductIdTest(){
        final long productId = 1l;
        final long shopId = 1l;
        Shop shop = shopRepository.findById(shopId).get();
        Product product = productRepository.findById(productId).get();

        ShopProduct savedShopProduct = shopProductRepository
                .findShopProductsByShopIdAndProductIdTest(shopId, productId).get();

        assertThat(savedShopProduct.getShop()).isEqualTo(shop);
        assertThat(savedShopProduct.getProduct()).isEqualTo(product);
    }

    @Test
    @Order(4)
    @Rollback(value = false)
    @DisplayName("[Repository] Test shop_product record exists by given product")
    public void existsByProductTest(){
        final long productId = 1l;
        Product product = getSavedProduct(productId);

        boolean exists = shopProductRepository.existsByProduct(product);

        assertThat(exists).isTrue();
    }

    @Test
    @Order(5)
    @Rollback(value = false)
    @DisplayName("[Repository] Test deleting shop product record by shop_id")
    public void deleteShopProductByShopIdTest(){
        final long productId = 1l;
        final long shopId = 1l;
        shopProductRepository.deleteShopProductByShopIdAndProductId(shopId, productId);

        List<ShopProduct> shopProducts = shopProductRepository.findShopProductsByShopId(shopId);

        assertThat(shopProducts).hasSize(0);

       assertThatCode(()->{
           shopRepository.findById(shopId).get();
           productRepository.findById(productId).get();
        }).doesNotThrowAnyException();
    }

    private Product getSavedProduct(long id){
        return productRepository.findById(id).get();
    }
}