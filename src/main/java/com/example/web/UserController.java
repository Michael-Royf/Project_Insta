package com.example.web;

import com.example.dto.UserDto;
import com.example.entity.UserEntity;
import com.example.exceptions.GlobalExceptionHandler;
import com.example.facade.UserFacade;
import com.example.service.UserServiceImpl;
import com.example.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("api/user")
@CrossOrigin
public class UserController extends GlobalExceptionHandler {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserFacade userFacade;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;

    @GetMapping("/")
    public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
        UserEntity user = userService.getCurrentUser(principal);
        UserDto userDto = userFacade.userToDTO(user);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserProfile(@PathVariable("userId") String userId) {
        UserEntity user = userService.getUserById(Long.parseLong(userId));
        UserDto userDto = userFacade.userToDTO(user);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDto userDto,
                                             BindingResult bindingResult, Principal principal){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        UserEntity user = userService.updateUser(userDto, principal);
        UserDto  userDtoUpdated = userFacade.userToDTO(user);
        return  new ResponseEntity<>(userDtoUpdated, HttpStatus.OK);
    }




}
