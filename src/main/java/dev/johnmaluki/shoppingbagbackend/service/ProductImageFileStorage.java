package dev.johnmaluki.shoppingbagbackend.service;

import dev.johnmaluki.shoppingbagbackend.exception.DirectoryCreationException;
import dev.johnmaluki.shoppingbagbackend.exception.FileReadableException;
import dev.johnmaluki.shoppingbagbackend.exception.FileUploadException;
import dev.johnmaluki.shoppingbagbackend.exception.NotFoundException;
import dev.johnmaluki.shoppingbagbackend.util.FileUploadUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Slf4j
@Service
public class ProductImageFileStorage implements FileStorageService{
    private final Path fileStoragePath;
    private final String fileStorageLocation;

    public ProductImageFileStorage(@Value("${file.product.storage.images.location:temp}") String fileStorageLocation) {
        this.fileStorageLocation = fileStorageLocation;
        this.fileStoragePath = Paths.get(fileStorageLocation).toAbsolutePath().normalize();
        try {
            Files.createDirectories(fileStoragePath);
        } catch (IOException e) {
            log.error("'shopping_bag_product_images' directory not created successfully, create one manually.");
            throw new DirectoryCreationException("issue in creating file directory");
        }
    }

    @Override
    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileCode = RandomStringUtils.randomAlphabetic(FileUploadUtils.FILE_CODE_NUMBER);
        String storedFileName = fileCode + "-" + fileName;
        Path filePath = Paths.get(fileStoragePath + File.separator + storedFileName);
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            log.error(
                    "'shopping_bag_product_images' directory may be deleted! " +
                            "Create directory named: shopping_bag_product_images ({})",
                    ex.getMessage());
            throw new FileUploadException("issue in storing the file");
        }
        return storedFileName;
    }

    @Override
    public Resource downloadFile(String fileName) {
        Path path = Paths.get(this.fileStorageLocation).toAbsolutePath().resolve(fileName);
        Resource resource;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            log.error("Issue reading the file: {}" , e.getMessage());
            throw new NotFoundException("Issue reading the file");
        }

        if(resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            log.error("The file doesn't exist or not readable");
            throw new FileReadableException("The file doesn't exist or not readable");
        }
    }
}
