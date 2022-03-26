package com.example.service;

import com.example.entity.ConfirmationTokenEntity;
import com.example.repository.ConfirmationTokenRepository;
import com.example.service.interf.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Override
    public void saveConfirmationToken(ConfirmationTokenEntity token) {
        confirmationTokenRepository.save(token);
    }

    @Override
    public Optional<ConfirmationTokenEntity> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    @Override
    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }
}
