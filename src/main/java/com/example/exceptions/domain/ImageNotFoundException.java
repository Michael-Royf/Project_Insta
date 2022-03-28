package com.example.exceptions.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ImageNotFoundException  extends Exception{
    public ImageNotFoundException(String message) {
        super(message);
    }
}
