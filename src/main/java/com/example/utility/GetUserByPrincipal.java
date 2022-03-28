package com.example.utility;

import com.example.entity.UserEntity;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import static com.example.constant.UserImplConstant.*;
import java.security.Principal;

@Component
public class GetUserByPrincipal {
    private final UserRepository userRepository;

    @Autowired
    public GetUserByPrincipal(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + username));
    }
}
