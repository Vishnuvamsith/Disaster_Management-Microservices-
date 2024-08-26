package com.example.mail.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class maildto {
    private String to;
    private String subject;
    private String body;
}
