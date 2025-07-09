package org.example.notificationservice;

import org.example.dto.UserEvent;
import org.example.dto.UserOperation;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    private final JavaMailSender mailSender;

    public KafkaConsumerService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @KafkaListener(topics = "user-events", groupId = "notification-group")
    public void listen(UserEvent event) {
        String subject = event.getOperation() == UserOperation.CREATED
                ? "Аккаунт создан"
                : "Аккаунт удалён";

        String text = event.getOperation() == UserOperation.CREATED
                ? "Здравствуйте! Ваш аккаунт на сайте был успешно создан."
                : "Здравствуйте! Ваш аккаунт был удалён.";

        sendEmail(event.getEmail(), subject, text);
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}

