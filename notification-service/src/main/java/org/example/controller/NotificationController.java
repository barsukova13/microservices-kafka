package org.example.controller;


import org.example.dto.UserEvent;
import org.example.notificationservice.EmailService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final EmailService emailService;

    public NotificationController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/email")
    public void sendEmail(@RequestBody UserEvent emailMessage) {
        emailService.sendEmail(
                emailMessage.getTo(),
                emailMessage.getSubject(),
                emailMessage.getText()
        );
    }
}
