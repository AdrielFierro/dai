package com.tpo.TPO.controller.geo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.tpo.TPO.service.geo.GeoService;

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
}
