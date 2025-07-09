package com.example.notificationservice.event;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {
    private String email;
    private EventType eventType;

    public enum EventType {
        CREATED, DELETED
    }
}