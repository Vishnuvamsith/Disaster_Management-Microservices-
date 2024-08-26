package com.example.safeplaces.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.safeplaces.entity.safeplaces;

@Repository
public interface saferepo extends MongoRepository<safeplaces, ObjectId>{
    safeplaces findByName(String name);
}
