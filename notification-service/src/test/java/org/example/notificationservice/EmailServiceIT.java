package org.example.notificationservice;


import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetup;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class EmailServiceIT {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetup.SMTP);

    @Autowired
    private EmailService emailService;

    @Test
    public void whenSendEmail_thenEmailIsReceived() throws Exception {

        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test Content";


        emailService.sendEmail(to, subject, text);


        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertEquals(1, receivedMessages.length);
        assertEquals(subject, receivedMessages[0].getSubject());
        assertTrue(receivedMessages[0].getContent().toString().contains(text));
        assertEquals(to, receivedMessages[0].getAllRecipients()[0].toString());
    }
}