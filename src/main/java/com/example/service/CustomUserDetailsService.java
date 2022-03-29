package com.example.service;

import com.example.entity.UserEntity;
import com.example.entity.enums.ERole;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.Collection;

import static com.example.constant.UserImplConstant.NO_USER_FOUND_BY_USERNAME;

@Transactional
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, LoginAttemptService loginAttemptService) {
        this.userRepository = userRepository;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + username));

        validateLoginAttempt(user);
        return user;
    }


    public UserEntity loadUserById(Long id) {
        return userRepository.findUserById(id).orElse(null);
    }

//    public static User build(UserEntity user) {
//        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
//        user.getRole().forEach(role -> {
//            authorities.add(new SimpleGrantedAuthority(role.name()));
//        });
//
//        return new org.springframework.security.core.userdetails.User(
//                user.getUsername(),
//                user.getPassword(),
//                authorities
//        );
//    }
    private ERole getRoleEnumName(String role) {
        return ERole.valueOf(role.toUpperCase());
    }


    private void validateLoginAttempt(UserEntity user) {
        if(user.isAccountNonLocked()) {
            if(loginAttemptService.hasExceededMaxAttempts(user.getUsername())) {
                user.setIsNotLocked(false);
            } else {
                user.setIsNotLocked(true);
            }
        } else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
        }
    }
}
