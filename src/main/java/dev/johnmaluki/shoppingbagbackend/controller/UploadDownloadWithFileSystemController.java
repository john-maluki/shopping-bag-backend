package dev.johnmaluki.shoppingbagbackend.controller;

import dev.johnmaluki.shoppingbagbackend.Dto.FileUploadResponseDto;
import dev.johnmaluki.shoppingbagbackend.service.FileStorageService;
import dev.johnmaluki.shoppingbagbackend.util.FileUploadUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/v1")
public class UploadDownloadWithFileSystemController {
    private final FileStorageService fileStorageService;

    public UploadDownloadWithFileSystemController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/product_image/upload")
    FileUploadResponseDto productImageUpload(@RequestParam("file") MultipartFile file){
        String fileName = fileStorageService.storeFile(file);
        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/v1/product_image/download/")
                .path(fileName)
                .toUriString();
        String contentType = file.getContentType();

        FileUploadResponseDto fileUploadResponse = new FileUploadResponseDto();
        fileUploadResponse.setFileName(
                fileName.substring(FileUploadUtils.FILE_CODE_NUMBER + 1)); // + 1 to remove dash (-)
        fileUploadResponse.setFileCode(fileName.substring(0, 50));
        fileUploadResponse.setDownloadUri(url);
        fileUploadResponse.setContentType(contentType);

        return fileUploadResponse;
    }

    @GetMapping("/product_image/download/{fileName}")
    ResponseEntity<Resource> productImageDownload(@PathVariable("fileName") String fileName){
        Resource productImageFile = fileStorageService.downloadFile(fileName);
        MediaType contentType = MediaType.IMAGE_JPEG;
        return ResponseEntity
                .ok()
                .contentType(contentType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
                        + productImageFile.getFilename() + "\"")
                .body(productImageFile);
    }
}
