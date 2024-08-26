package com.example.safeplaces.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.safeplaces.entity.safeplaces;
import com.example.safeplaces.repository.saferepo;
@Service
@Component
public class safeservice {
    @Autowired
    private saferepo repository;
    @Autowired
    private JmsTemplate jms;
    public void loadDataFromExcel(MultipartFile file) throws IOException {
        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                String name = row.getCell(0).getStringCellValue();
                double latitude = row.getCell(1).getNumericCellValue();
                double longitude = row.getCell(2).getNumericCellValue();

                safeplaces existingPlace = repository.findByName(name);
                if (existingPlace == null) {
                    safeplaces place = new safeplaces();
                    place.setName(name);
                    place.setLatitude(latitude);
                    place.setLongitude(longitude);
                    repository.save(place);
                }
            }
        }
    }
    public String fetchnames()
    {
        ArrayList<String>list=new ArrayList<>();
        for(safeplaces i : repository.findAll())
        {
            list.add(i.getName());
        }
        jms.convertAndSend("email", list);
        return "email sent succesffully";
    }
    @SuppressWarnings("unused")
    @JmsListener(destination = "safeplaces")
    public void  fetchall() {
        // Fetch the list of places from the repository
        ArrayList<safeplaces> placesList = new ArrayList<>(repository.findAll());
    
        // Check if the list is not null and not empty
        if (placesList != null && !placesList.isEmpty()) {
            Map<String, Map<String, Object>> placesMap = new HashMap<>();
    
            // Iterate through the list of places
            for (safeplaces place : placesList) {
                // Check if the place and its details are not null
                if (place != null && place.getName() != null && place.getLatitude() != null && place.getLongitude() != null) {
                    Map<String, Object> details = new HashMap<>();
                    details.put("latitude", place.getLatitude());
                    details.put("longitude", place.getLongitude());
    
                    // Add to the map if the details are not null
                    if (details.get("latitude") != null && details.get("longitude") != null) {
                        placesMap.put(place.getName(), details);
                    }
                }
            }
    
            // Ensure the keys and values in the placesMap are not null
            placesMap.entrySet().removeIf(entry -> entry.getKey() == null || entry.getValue() == null);
            placesMap.values().forEach(details -> {
                details.entrySet().removeIf(entry -> entry.getValue() == null);
            });
    
            // Print the map before sending it to JMS
            System.out.println("Prepared places map for sending: " + placesMap);
    
            // Send the map to the "alert" destination
            try {
                jms.convertAndSend("alert", placesMap);
                System.out.println( "List of places has been sent");
            } catch (JmsException e) {
                e.printStackTrace(); // Log the exception for debugging
                System.out.println( "Failed to send list of places: " + e.getMessage());
            }
        } else {
            // Handle the case where placesList is null or empty
            System.out.println( "No places available to send");
        }
    }
    
    

    
}
