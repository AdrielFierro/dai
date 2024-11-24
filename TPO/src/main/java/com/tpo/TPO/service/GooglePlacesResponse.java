package com.tpo.TPO.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GooglePlacesResponse {

    @JsonProperty("results")
    private List<Place> results;

    public List<Place> getResults() {
        return results;
    }

    public void setResults(List<Place> results) {
        this.results = results;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Place {
        @JsonProperty("name")
        private String name;

        @JsonProperty("vicinity")
        private String vicinity;

        @JsonProperty("geometry")
        private Geometry geometry;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVicinity() {
            return vicinity;
        }

        public void setVicinity(String vicinity) {
            this.vicinity = vicinity;
        }

        public Geometry getGeometry() {
            return geometry;
        }

        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Geometry {
        @JsonProperty("location")
        private Location location;

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Location {
        @JsonProperty("lat")
        private double lat;

        @JsonProperty("lng")
        private double lng;

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }
    }
}
