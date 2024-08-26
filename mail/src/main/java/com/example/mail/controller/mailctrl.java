package com.example.mail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mail.DTO.maildto;
import com.example.mail.service.mailservice;

@RestController
@RequestMapping("/email")
public class mailctrl {
    @Autowired
    private mailservice service;
    @PostMapping("/send")
    public String sendEmail(@RequestBody maildto emailRequest) {
        service.sendSimpleMessage(emailRequest);
        return "Email sent successfully";
    }
}
