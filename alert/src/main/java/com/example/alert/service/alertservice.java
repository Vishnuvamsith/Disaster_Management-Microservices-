package com.example.alert.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.geotools.referencing.GeodeticCalculator;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alert")
@CrossOrigin(origins = "http://localhost:3000")
public class alertservice {

    @Autowired
    private JmsTemplate jms;

    private Map<String, Map<String, Object>> places = new HashMap<>();

    @JmsListener(destination = "alert")
    public void triggerSafePlaces(Map<String, Map<String, Object>> data) {
        if (data != null) {
            this.places = data;
            System.out.println("Received and processed data: " + data);
        } else {
            System.out.println("Received null data");
        }
    }


    // @GetMapping("/nearby-safe-places")
    // public List<String> getNearbySafePlaces(
    //         @RequestParam double userLat,
    //         @RequestParam double userLon) {

    //     // Send a request to get the locations
    //     jms.convertAndSend("safeplaces", "send me locations");

    //     GeodeticCalculator calculator = new GeodeticCalculator();
    //     Coordinate userCoordinate = new Coordinate(userLat, userLon);

    //     // Convert the map data to a list of names of safeplaces objects within 10 km
    //     ArrayList<String> nearbyPlaceNames = (ArrayList<String>) places.entrySet().stream()
    //         .filter(entry -> {
    //             Map<String, Object> details = entry.getValue();
    //             Double latitude = (Double) details.get("latitude");
    //             //System.out.println(latitude);
    //             Double longitude = (Double) details.get("longitude");

    //             Coordinate placeCoordinate = new Coordinate(latitude, longitude);

    //             calculator.setStartingGeographicPoint(userCoordinate.x, userCoordinate.y);
    //             calculator.setDestinationGeographicPoint(placeCoordinate.x, placeCoordinate.y);

    //             double distance = calculator.getOrthodromicDistance(); // Distance in meters
    //             System.out.println(distance);
    //             return distance <= 10000;
    //         })
    //         .map(Map.Entry::getKey) // Extract place names
    //         .collect(Collectors.toList());
    //         places = new HashMap<>();

    //     return nearbyPlaceNames;
    // }
    @GetMapping("/nearby-safe-places")
public List<Map<String, Object>> getNearbySafePlaces(
        @RequestParam double userLat,
        @RequestParam double userLon) {
            

    // Send a request to get the locations
    jms.convertAndSend("safeplaces", "send me locations");
    System.out.println(userLat);
            System.out.println(userLon);

    GeodeticCalculator calculator = new GeodeticCalculator();
    Coordinate userCoordinate = new Coordinate(userLat, userLon);

    // Convert the map data to a list of places within 10 km
    List<Map<String, Object>> nearbyPlaces = places.entrySet().stream()
    .filter(entry -> {
        Map<String, Object> details = entry.getValue();
        if (details == null) {
            System.out.println("Details are null for entry: " + entry.getKey());
            return false;
        }
    
        Double latitude = (Double) details.get("latitude");
        Double longitude = (Double) details.get("longitude");
    
        if (latitude == null || longitude == null) {
            System.out.println("Latitude or Longitude is null for entry: " + entry.getKey());
            return false;
        }
    
        Coordinate placeCoordinate = new Coordinate(latitude, longitude);
        calculator.setStartingGeographicPoint(userCoordinate.x, userCoordinate.y);
        calculator.setDestinationGeographicPoint(placeCoordinate.x, placeCoordinate.y);
    
        double distance = calculator.getOrthodromicDistance(); // Distance in meters
        System.out.println("Distance to " + entry.getKey() + ": " + distance);
    
        return distance <= 10000;
    })
    
        .map(entry -> {
            Map<String, Object> details = entry.getValue();
            Map<String, Object> placeDetails = new HashMap<>();
            placeDetails.put("latitude", details.get("latitude"));
            placeDetails.put("longitude", details.get("longitude"));
            placeDetails.put("Name", entry.getKey());
            return placeDetails;
        })
        .collect(Collectors.toList());

    places = new HashMap<>();

    return nearbyPlaces;
}

}
