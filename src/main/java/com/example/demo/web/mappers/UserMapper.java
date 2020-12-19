package com.example.demo.web.mappers;

import com.example.demo.domain.User;
import com.example.demo.web.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DateMapper.class})
public interface UserMapper {

    UserDto userToDto(User user);

    User dtoToUser(UserDto userDto);
}
