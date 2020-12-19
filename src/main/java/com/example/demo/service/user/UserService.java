package com.example.demo.service.user;

import com.example.demo.utils.builder.CustomResponse;
import com.example.demo.web.dto.UserDto;

public interface UserService {
    CustomResponse createUser(String token, UserDto createUserDto) ;

    CustomResponse updateUser(String token, UserDto updateUserDto) ;

    CustomResponse deleteUser(String token, Long id) ;

    CustomResponse getAllUser(String token) ;
}
