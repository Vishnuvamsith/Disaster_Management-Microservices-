package com.example.safeplaces.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection="safeplaces")
@Data @AllArgsConstructor @NoArgsConstructor
public class safeplaces {
    @Id
    private ObjectId id;
    private String name;
    private Double latitude;
    private Double longitude;
}
