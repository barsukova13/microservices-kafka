package com.example.notificationservice;

import com.example.notificationservice.event.UserEvent;
import com.example.notificationservice.service.NotificationService;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("src/test")
class NotificationServiceIntegrationTest {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetup.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("user", "admin"))
            .withPerMethodLifecycle(false);

    @Autowired
    private NotificationService notificationService;

    @Test
    void testSendEmail() throws Exception {
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test Message";

        notificationService.sendEmail(to, subject, text);

        // Wait for mail to arrive
        greenMail.waitForIncomingEmail(5000, 1);

        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertEquals(1, receivedMessages.length);
        assertEquals(subject, receivedMessages[0].getSubject());
        assertTrue(receivedMessages[0].getContent().toString().contains(text));
        assertEquals(to, receivedMessages[0].getAllRecipients()[0].toString());
    }

    @Test
    void testSendUserNotificationCreated() throws Exception {
        UserEvent event = new UserEvent("user@example.com", UserEvent.EventType.CREATED);

        notificationService.sendUserNotification(event);

        greenMail.waitForIncomingEmail(5000, 1);

        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertEquals(1, receivedMessages.length);
        assertEquals("Account Notification", receivedMessages[0].getSubject());
        assertTrue(receivedMessages[0].getContent().toString()
                .contains("Ваш аккаунт на сайте ваш сайт был успешно создан"));
    }

    @Test
    void testSendUserNotificationDeleted() throws Exception {
        UserEvent event = new UserEvent("user@example.com", UserEvent.EventType.DELETED);

        notificationService.sendUserNotification(event);

        greenMail.waitForIncomingEmail(5000, 1);

        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertEquals(1, receivedMessages.length);
        assertEquals("Account Notification", receivedMessages[0].getSubject());
        assertTrue(receivedMessages[0].getContent().toString()
                .contains("Ваш аккаунт был удалён"));
    }
}
