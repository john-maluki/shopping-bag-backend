package dev.johnmaluki.shoppingbagbackend.repository;

import dev.johnmaluki.shoppingbagbackend.entity.*;
import dev.johnmaluki.shoppingbagbackend.exception.NotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DataJpaTest
@DirtiesContext
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ShoppingBagProductRepositoryTest {
    @Autowired
    private ShoppingBagProductRepository shoppingBagProductRepository;

    @Autowired
    private ShoppingBagRepository shoppingBagRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ShopProductRepository shopProductRepository;


    @Test
    @Order(1)
    @Rollback(value = false)
    @DisplayName("[Repository] Test saving saving shopping_bag_product record")
    public void saveShoppingBagProductTest(){
        createProduct();
        createShop();
        createShoppingBag();
        createShopProduct();
        ShoppingBag shoppingBag = getShoppingBag(1l);
        ShopProduct shopProduct= getShopProduct(1l);
        final int quantity = 5;
        ShoppingBagProduct shoppingBagProduct = ShoppingBagProduct.builder()
                .shoppingBag(shoppingBag)
                .shopProduct(shopProduct)
                .quantity(quantity)
                .build();
        shoppingBagProductRepository.save(shoppingBagProduct);

        final long shoppingBagProductId = 1l;
        ShoppingBagProduct savedShoppingBagProduct = shoppingBagProductRepository
                .findById(shoppingBagProductId).orElse(null);
        Product product = getProduct(1l);
        Shop shop = getShop(1l);

        assertThat(savedShoppingBagProduct).isNotNull();
        assertThat(savedShoppingBagProduct.getQuantity()).isEqualTo(quantity);
        assertThat(savedShoppingBagProduct.getShoppingBag()).isEqualTo(shoppingBag);
        assertThat(savedShoppingBagProduct.getShopProduct()).isEqualTo(shopProduct);
        assertThat(savedShoppingBagProduct.getShopProduct()
                .getShop()).isEqualTo(shop);
        assertThat(savedShoppingBagProduct.getShopProduct()
                .getProduct()).isEqualTo(product);

    }

    @Test
    @Order(2)
    @Rollback(value = false)
    @DisplayName("[Repository] Test fetching shopping_bag_product record by shopping_bag and shop_product")
    public void findShoppingBagProductByShoppingBagAndShopProductTest(){
        ShoppingBag shoppingBag = getShoppingBag(1l);
        ShopProduct shopProduct = getShopProduct(1l);

        ShoppingBagProduct shoppingBagProduct = shoppingBagProductRepository
                .findByShoppingBagAndShopProduct(shoppingBag, shopProduct).orElse(null);

        assertThat(shoppingBagProduct).isNotNull();
        assertThat(shoppingBagProduct).isNotNull();
        assertThat(shoppingBagProduct.getQuantity()).isEqualTo(5);
        assertThat(shoppingBagProduct.getShoppingBag()).isEqualTo(shoppingBag);
        assertThat(shoppingBagProduct.getShopProduct()).isEqualTo(shopProduct);
    }

    @Test
    @Order(3)
    @Rollback(value = false)
    @DisplayName("[Repository] Test updating saving shopping_bag_product record")
    public void updateShoppingBagProductQuantityTest(){
        ShoppingBag shoppingBag = getShoppingBag(1l);
        ShopProduct shopProduct = getShopProduct(1l);
        ShoppingBagProduct shoppingBagProduct = shoppingBagProductRepository
                .findById(1l).orElse(null);
        final int quantity = 6;
        shoppingBagProduct.setQuantity(quantity);
        shoppingBagProductRepository.save(shoppingBagProduct);

        final long shoppingBagProductId = 1l;
        ShoppingBagProduct savedShoppingBagProduct = shoppingBagProductRepository
                .findById(shoppingBagProductId).orElse(null);

        assertThat(savedShoppingBagProduct).isNotNull();
        assertThat(savedShoppingBagProduct.getQuantity()).isEqualTo(quantity);
    }

    @Test
    @Order(4)
    @Rollback(value = false)
    @DisplayName("[Repository] Test fetching all shopping_bag_product records by shopping_bag")
    public void findAllShoppingBagProductByShoppingBagTest(){
        ShoppingBag shoppingBag = getShoppingBag(1l);

        List<ShoppingBagProduct> shoppingBagProducts = shoppingBagProductRepository
                .findAllByShoppingBag(shoppingBag);

        assertThat(shoppingBagProducts).hasSize(1);
    }


    @Test
    @Order(5)
    @Rollback(value = false)
    @DisplayName("[Repository] Test deleting shopping_bag_product record by shopping_bag and shop_product")
    public void deleteShoppingBagProductByShoppingBagAndShopProductTest(){
        ShoppingBag shoppingBag = getShoppingBag(1l);
        ShopProduct shopProduct = getShopProduct(1l);
        shoppingBagProductRepository.deleteByShoppingBagAndShopProduct(shoppingBag, shopProduct);

        final String errorMessage = "ShoppingBagProduct not found!";
        Throwable shoppingBagProductException = catchThrowable(
                ()-> shoppingBagProductRepository
                        .findByShoppingBagAndShopProduct(shoppingBag, shopProduct)
                        .orElseThrow(()-> new NotFoundException(errorMessage)));

        assertThat(shoppingBagProductException).isNotNull();
        assertThat(shoppingBagProductException)
                .isInstanceOf(NotFoundException.class).hasMessage(errorMessage);

        // Deleting the shoppingBagProduct record should not
        // delete the associated shoppingBag record and shopProduct
        // Fetching there records again to confirm their deletion.
        ShoppingBag shoppingBag1 = getShoppingBag(1l);
        ShopProduct shopProduct1 = getShopProduct(1l);

        assertThat(shoppingBag1).isNotNull();
        assertThat(shopProduct1).isNotNull();
    }

    private void createShoppingBag(){
        ShoppingBag shoppingBag = ShoppingBag.builder()
                .build();
        shoppingBagRepository.save(shoppingBag);
    }

    private void createShop(){
        Shop shop = Shop.builder()
                .name("Electronic & Computer Dealer")
                .description("Number 1 electronic seller")
                .isActivated(true)
                .isOpen(true)
                .build();
        shopRepository.save(shop);
    }

    private void createProduct(){
        final String productName = "HP 440";
        final String productDescription = "4gb ram and 500gb disk space";
        final long brandId = 1l;
        Product product = Product.builder()
                .name(productName)
                .description(productDescription)
                .build();
        productRepository.save(product);
    }

    private void createShopProduct(){
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
    }

    private ShoppingBag getShoppingBag(long shoppingBagId){
        return shoppingBagRepository.findById(shoppingBagId).get();
    }

    private Shop getShop(long shopId){
        return shopRepository.findById(shopId).get();
    }

    private Product getProduct(long productId){
        return productRepository.findById(productId).get();
    }

    private ShopProduct getShopProduct(long shopProductId){
        return shopProductRepository.findById(shopProductId).get();
    }

}