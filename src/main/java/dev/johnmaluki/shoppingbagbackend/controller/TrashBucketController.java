package dev.johnmaluki.shoppingbagbackend.controller;

import dev.johnmaluki.shoppingbagbackend.Dto.TrashBucketDto;
import dev.johnmaluki.shoppingbagbackend.entity.TrashBucket;
import dev.johnmaluki.shoppingbagbackend.service.TrashBucketService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "v1/trash")
public class TrashBucketController {
    private final TrashBucketService trashBucketService;
    private final ModelMapper modelMapper;

    @Autowired
    public TrashBucketController(TrashBucketService trashBucketService, ModelMapper modelMapper) {
        this.trashBucketService = trashBucketService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<List<TrashBucketDto>> getALlTrashedBags(@PathVariable("id") long userTrashId){
        List<TrashBucket> trashBuckets = trashBucketService.getUserTrashedShoppingBags(userTrashId);

        return ResponseEntity.ok().body(
                trashBuckets.stream()
                        .map(trashBucket -> this.modelMapper.
                                map(trashBucket, TrashBucketDto.class))
                        .collect(Collectors.toList())
        );
    }

    @PostMapping(value = "/shopping-bag/{id}")
    public ResponseEntity<TrashBucket> trashShoppingBag(@PathVariable("id") long shoppingBagId){
        TrashBucket trashBucket = trashBucketService.trashShoppingBag(shoppingBagId);
        if(trashBucket != null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @DeleteMapping(value = "/bucket/{id}")
    public ResponseEntity<?> deleteShoppingBagPermanently(@PathVariable("id") long trashBucketId){
        trashBucketService.deleteShoppingBagPermanently(trashBucketId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
