package com.example.service.interf;

import com.example.entity.ConfirmationTokenEntity;

import java.util.Optional;

public interface ConfirmationTokenService {
    void saveConfirmationToken(ConfirmationTokenEntity token);

    Optional<ConfirmationTokenEntity> getToken(String token);

    int setConfirmedAt(String token);
}
