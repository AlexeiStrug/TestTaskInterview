package com.example.demo.web.controller;

import com.example.demo.service.auth.AuthService;
import com.example.demo.service.user.impl.UserServiceImpl;
import com.example.demo.utils.exception.TypeOfResponseTypeProcessingException;
import com.example.demo.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserDto userDto)  {
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(userDto));
    }

    @PostMapping("/forgot")
    public ResponseEntity forgot(final HttpServletRequest request, @RequestBody UserDto userDto)  {
        return ResponseEntity.status(HttpStatus.OK).body(authService.forgotPassword(getAppUrl(request), userDto));
    }

    @PostMapping("/reset")
    public ResponseEntity reset(@RequestParam("code") String code,
                                @RequestBody UserDto userDto)  {
        return ResponseEntity.status(HttpStatus.OK).body(authService.resetPassword(code, userDto));
    }

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }


}
