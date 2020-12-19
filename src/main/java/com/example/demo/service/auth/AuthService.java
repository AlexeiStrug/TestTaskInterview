package com.example.demo.service.auth;

import com.example.demo.utils.builder.CustomResponse;
import com.example.demo.web.dto.UserDto;

public interface AuthService {

    CustomResponse login(UserDto loginDto) ;

    CustomResponse resetPassword(String code, UserDto resetPasswordDto) ;

    CustomResponse forgotPassword(String url, UserDto forgotPasswordDto) ;
}
