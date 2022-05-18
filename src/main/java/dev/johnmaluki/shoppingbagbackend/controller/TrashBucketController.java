package dev.johnmaluki.shoppingbagbackend.controller;

import dev.johnmaluki.shoppingbagbackend.Dto.TrashBucketDto;
import dev.johnmaluki.shoppingbagbackend.entity.ShoppingBag;
import dev.johnmaluki.shoppingbagbackend.entity.TrashBucket;
import dev.johnmaluki.shoppingbagbackend.service.TrashBucketService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "v1/trash")
public class TrashBucketController {
    private final TrashBucketService trashBucketService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public TrashBucketController(TrashBucketService trashBucketService) {
        this.trashBucketService = trashBucketService;
    }

    @PostMapping(value = "/shopping-bag/{id}")
    public ResponseEntity trashShoppingBag(@PathVariable("id") long shoppingBagId){
        TrashBucket trashBucket = trashBucketService.trashShoppingBag(shoppingBagId);
        if(trashBucket != null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
