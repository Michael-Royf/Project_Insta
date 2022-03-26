package com.example.exceptions.domain1;

import com.example.constant.ExceptionConstant;

public class EmailExistException extends Exception {
    public EmailExistException(String message) {
        super(message);
    }
}
