package com.example.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserDto {
    private  Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String lastname;
    @NotEmpty
    private String username;
    private String bio;
}
