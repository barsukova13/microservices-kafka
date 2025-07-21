package org.example.service;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.UserEvent;
import org.example.dto.UserOperation;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.example.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.example.model.User;
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final KafkaTemplate<String, UserEvent> kafkaTemplate;
    private final ReactiveCircuitBreakerFactory circuitBreakerFactory;// Изменили тип

    public void createUser(User user) {
        User savedUser = userRepository.save(user);
        sendEventWithCircuitBreaker(savedUser.getEmail(), UserOperation.CREATED)
                .doOnError(e -> log.error("Failed to send CREATE event for user {}", savedUser.getEmail(), e))
                .subscribe();
    }

    public void deleteUser(Long id) {
        userRepository.findById(id).ifPresent(user -> {
            userRepository.delete(user);
            sendEventWithCircuitBreaker(user.getEmail(), UserOperation.DELETED)
                    .doOnError(e -> log.error("Failed to send DELETE event for user {}", user.getEmail(), e))
                    .subscribe();
        });
    }

    private void sendEvent(String email, UserOperation operation) {
        kafkaTemplate.send("user-events", new UserEvent(email, operation));
    }
}
