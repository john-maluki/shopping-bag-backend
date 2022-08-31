package dev.johnmaluki.shoppingbagbackend.controller;

import dev.johnmaluki.shoppingbagbackend.Dto.LocationDto;
import dev.johnmaluki.shoppingbagbackend.entity.Location;
import dev.johnmaluki.shoppingbagbackend.service.LocationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/shop_location")
public class LocationController {
    private final LocationService locationService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<LocationDto> getShopLocation(@PathVariable("id") long shopId){
        Location location = locationService.getShopLocation(shopId);
        LocationDto locationDto = modelMapper.map(location, LocationDto.class);
        return ResponseEntity.ok().body(locationDto);
    }
}
