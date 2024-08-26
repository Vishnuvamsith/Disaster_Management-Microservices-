package com.example.safeplaces.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.safeplaces.services.safeservice;
@RestController
@RequestMapping(path="/add")
public class safectrl {
    @Autowired
    private safeservice service;
    @PostMapping("/excel")
    public String uploadFile(@RequestParam MultipartFile file) {
        try {
            service.loadDataFromExcel(file);
            return "Data loaded successfully";
        } catch (IOException e) {
            return "Error loading data";
        }
    }
    @PostMapping("/sendemail")
    public ResponseEntity<String> sendemail()
    {
        return new ResponseEntity<>(service.fetchnames(),HttpStatus.OK);
    }
}
