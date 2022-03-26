package com.example.exceptions.domain1;

public class NotAnImageFileException extends Exception{
    public NotAnImageFileException(String message) {
        super(message);
    }
}
