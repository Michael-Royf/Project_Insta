package com.example.exceptions.domain;


public class PostNotFoundException extends  Exception {
    public PostNotFoundException(String message) {
        super(message);
    }
}
