package com.tpo.TPO.service.geo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.tpo.TPO.service.geo.*;

@Service
public class GeoService {

    @Value("${google.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getCoordinatesFromAddress(String address) {
        String url = UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/geocode/json")
                .queryParam("address", address)
                .queryParam("key", apiKey)
                .toUriString();

        GoogleGeocodeResponse response = restTemplate.getForObject(url, GoogleGeocodeResponse.class);

        if (response != null && response.getResults() != null && !response.getResults().isEmpty()) {
            return response.getResults().get(0).getGeometry().getLocation().toString();
        }
        return null;
    }

    public String getAddressFromCoordinates(double lat, double lng) {
        String url = UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/geocode/json")
                .queryParam("latlng", lat + "," + lng)
                .queryParam("key", apiKey)
                .toUriString();

        GoogleGeocodeResponse response = restTemplate.getForObject(url, GoogleGeocodeResponse.class);

        if (response != null && response.getResults() != null && !response.getResults().isEmpty()) {
            return response.getResults().get(0).getFormattedAddress();
        }
        return null;
    }
}
