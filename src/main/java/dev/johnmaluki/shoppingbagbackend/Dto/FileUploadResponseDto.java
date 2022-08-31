package dev.johnmaluki.shoppingbagbackend.Dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class FileUploadResponseDto implements Serializable {
    private String fileName;
    private String fileCode;
    private String downloadUri;
    private String contentType;
}
