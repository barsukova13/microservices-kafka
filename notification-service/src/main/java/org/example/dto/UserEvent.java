package org.example.dto;


import lombok.Data;

@Data
public class UserEvent {
    private String email;       // Было email → to
    private String subject;  // Добавляем
    private String text;     // Добавляем
    private UserOperation operation;
    private String to;
}
