package com.example.exceptions.domain1;

import com.example.constant.ExceptionConstant;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(String message) {
        super(message);
    }
}
