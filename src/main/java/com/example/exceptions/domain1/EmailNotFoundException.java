package com.example.exceptions.domain1;

public class EmailNotFoundException extends Exception{
    public EmailNotFoundException(String message) {
        super(message);
    }
}
