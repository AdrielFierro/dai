package com.tpo.TPO.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import com.tpo.TPO.service.AdApiService;

@RestController
@RequestMapping("/ads")
public class AdController {

    private final AdApiService adApiService;

    public AdController(AdApiService adApiService) {
        this.adApiService = adApiService;
    }

    /**
     * Retrieves a list of ads from external API.
     *
     * @return ResponseEntity containing the list of ads or an error message
     */
    @GetMapping
    public ResponseEntity<Object> getAllAds() {
        try {
            ResponseEntity<Object> response = adApiService.getAdsFromExternalApi();
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Error al obtener los anuncios externos: " + e.getMessage());
        }
    }
}
