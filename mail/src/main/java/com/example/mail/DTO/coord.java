package com.example.mail.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class coord {
    private Double latitude;
    private Double longitude;
    private String emergencyType;
}
