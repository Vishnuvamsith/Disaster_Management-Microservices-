package com.example.mail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.mail.DTO.coord;
import com.example.mail.DTO.maildto;
@Service
public class mailservice {
    @Autowired
    private JavaMailSender emailSender;
     public void sendSimpleMessage(maildto data) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(data.getTo());
        message.setSubject(data.getSubject());
        message.setText(data.getBody());
        emailSender.send(message);
    }
    public void sendcoord(coord data)
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("pra191029228@gmail.com");
        message.setSubject("coordinates of the victim");
        message.setText(
    "Emergency Type: " + data.getEmergencyType() + "\n" +
    "Latitude: " + data.getLatitude() + "\n" +
    "Longitude: " + data.getLongitude()
);
        emailSender.send(message);

    }
}
