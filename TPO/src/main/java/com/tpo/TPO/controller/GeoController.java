package com.tpo.TPO.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.tpo.TPO.service.GeoService;
import com.tpo.TPO.service.GooglePlacesResponse;

@RestController
@RequestMapping("/geo")
public class GeoController {

    @Autowired
    private GeoService geoService;

    @GetMapping("/address")
    public String getCoordinatesFromAddress(@RequestParam String address) {
        return geoService.getCoordinatesFromAddress(address);
    }

    @GetMapping("/coordinates")
    public String getAddressFromCoordinates(@RequestParam double lat, @RequestParam double lng) {
        return geoService.getAddressFromCoordinates(lat, lng);
    }

    @GetMapping("/places/nearby")
    public List<GooglePlacesResponse.Place> getNearbyPlaces(
        @RequestParam double lat,
        @RequestParam double lng,
        @RequestParam int radius) {
    return geoService.getNearbyPlaces(lat, lng, radius);
}

}
