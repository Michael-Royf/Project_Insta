package com.example.facade;

import com.example.dto.UserDto;
import com.example.entity.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
//клиентская сторона
public class UserFacade {
    @Autowired
    private ModelMapper mapper;

    public UserDto userToDTO(UserEntity user) {
        UserDto userDto = mapper.map(user, UserDto.class);
        return userDto;
    }

    public UserEntity userDtoToUserEntity(UserDto userDto) {
        UserEntity user = mapper.map(userDto, UserEntity.class);
        return user;
    }
}
