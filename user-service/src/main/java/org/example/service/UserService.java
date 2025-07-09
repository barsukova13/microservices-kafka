package org.example.service;

import lombok.RequiredArgsConstructor;

import org.example.dto.UserEvent;
import org.example.dto.UserOperation;
import org.springframework.kafka.core.KafkaTemplate;
import org.example.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.example.model.User;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final KafkaTemplate<String, UserEvent> kafkaTemplate; // Изменили тип

    public void createUser(User user) {
        User savedUser = userRepository.save(user);
        sendEvent(savedUser.getEmail(), UserOperation.CREATED); // Используем enum
    }

    public void deleteUser(Long id) {
        userRepository.findById(id).ifPresent(user -> {
            userRepository.delete(user);
            sendEvent(user.getEmail(), UserOperation.DELETED);
        });
    }

    private void sendEvent(String email, UserOperation operation) {
        kafkaTemplate.send("user-events", new UserEvent(email, operation));
    }
}
