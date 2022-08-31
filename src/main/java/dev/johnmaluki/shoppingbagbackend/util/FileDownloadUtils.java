package dev.johnmaluki.shoppingbagbackend.util;

import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public final class FileDownloadUtils {
    public static Resource findResourceStartingWithFileCode(String fileCode) throws IOException {
        Optional<Path> foundFile;
        String uploadDirectory = new PathResource(
                ProductImageFileUtil.PRODUCT_IMAGE_DIRECTORY)
                .getFile()
                .getAbsolutePath();
        foundFile = Files.list(Paths.get(uploadDirectory))
                .filter(file -> {
                    String s = file.getFileName().toString().substring(0, FileUploadUtils.FILE_CODE_NUMBER);
                    return s.equals(fileCode);
                })
                .findFirst();

        if (foundFile.isPresent()){
            return new UrlResource(foundFile.get().toUri());
        } else {
            throw new IOException();
        }
    }
}
