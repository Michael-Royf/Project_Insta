package com.example.service;

import com.example.entity.ConfirmationTokenEntity;
import com.example.repository.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;


    public void saveConfirmationToken(ConfirmationTokenEntity token) {
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationTokenEntity> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }
}
