package com.example.service;


import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.dto.UserDto;
import com.example.entity.ConfirmationTokenEntity;
import com.example.entity.UserEntity;
import com.example.entity.enums.ERole;
import com.example.exceptions.domain.UserNotFoundException;
import com.example.exceptions.domain.UsernameExistException;
import com.example.payload.request.SignupRequest;
import com.example.repository.UserRepository;
import com.example.service.interf.EmailSender;
import com.example.service.interf.UserService;
import com.example.utility.EmailBuilder;
import com.example.utility.GetUserByPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.example.constant.UserImplConstant.*;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ConfirmationTokenServiceImpl confirmationTokenService;
    private final EmailSender emailSender;
    private final EmailBuilder emailBuilder;
    private final GetUserByPrincipal getUserByPrincipal;


    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder,
                           ConfirmationTokenServiceImpl confirmationTokenService,
                           EmailSender emailSender, EmailBuilder emailBuilder,
                           GetUserByPrincipal getUserByPrincipal) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.confirmationTokenService = confirmationTokenService;
        this.emailSender = emailSender;
        this.emailBuilder = emailBuilder;
        this.getUserByPrincipal = getUserByPrincipal;
    }

    @Override
    public UserEntity createUser(SignupRequest userIn) throws UsernameExistException {
        UserEntity user = new UserEntity();
        user.setEmail(userIn.getEmail());
        user.setName(userIn.getName());
        user.setLastname(userIn.getLastname());
        user.setUsername(userIn.getUsername());
        user.setPassword(passwordEncoder.encode(userIn.getPassword()));
        user.setIsNotLocked(true);
        user.getRole().add(ERole.USER);

        //token for email sender
        String token = UUID.randomUUID().toString();
        ConfirmationTokenEntity confirmationToken = new ConfirmationTokenEntity(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );
        String link = "http://localhost:8080/api/auth/confirm?token=" + token;
        UserEntity userEntity;
        try {
            log.info("Saving User {}", userIn.getEmail());
            //exception
            userEntity = userRepository.save(user);
            confirmationTokenService.saveConfirmationToken(confirmationToken);
        } catch (Exception e) {
            log.error("Error during registration. {}", e.getMessage());
            throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
        }
        emailSender.send(userIn.getEmail(), emailBuilder.buildEmail(userIn.getName(), link));
        return userEntity;
    }


    @Transactional
    @Override
    public String confirmToken(String token) {
        ConfirmationTokenEntity confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("Token not found"));
        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Email already confirmed");
        }
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Token expired");
        }
        confirmationTokenService.setConfirmedAt(token);
        userRepository.enableUser(confirmationToken.getUser().getEmail());
        return "Confirmed";
    }

    @Override
    public UserEntity updateUser(UserDto userDto, Principal principal) {
        UserEntity user = getUserByPrincipal.getUserByPrincipal(principal);
        user.setName(userDto.getName());
        user.setLastname(userDto.getLastname());
        user.setBio(userDto.getBio());
        return userRepository.save(user);
    }

    @Override
    public UserEntity getCurrentUser(Principal principal) {
        return getUserByPrincipal.getUserByPrincipal(principal);
    }


    @Override
    public UserEntity getUserById(Long userId) {
        return userRepository.findUserById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(NO_USER_FOUND_BY_ID + userId));
    }

    @Override
    public UserEntity getUserByUsername(String username) throws UserNotFoundException {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + username));
    }


}
