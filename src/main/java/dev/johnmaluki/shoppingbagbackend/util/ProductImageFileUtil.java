package dev.johnmaluki.shoppingbagbackend.util;

import org.springframework.web.multipart.MultipartFile;

public final class ProductImageFileUtil {
    public static final String PRODUCT_IMAGE_DIRECTORY = "shopping_bag_product_images";
    public static long getFileSize(MultipartFile file){
        return file.getSize();
    }
}
