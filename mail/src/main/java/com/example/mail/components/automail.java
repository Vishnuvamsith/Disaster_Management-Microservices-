package com.example.mail.components;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class automail {
    @Autowired
    private JavaMailSender sender;
    @JmsListener(destination = "email")
    public void sendemail(ArrayList<String> data)
    {
        StringBuilder text=new StringBuilder("");
        for(String i : data)
        {
            text.append(i).append("/n");
        }
        SimpleMailMessage mssg=new SimpleMailMessage();
        mssg.setTo("pra191029228@gmail.com");
        mssg.setSubject("list of all safe places");
        mssg.setText(text.toString());
        sender.send(mssg);
    }
}
