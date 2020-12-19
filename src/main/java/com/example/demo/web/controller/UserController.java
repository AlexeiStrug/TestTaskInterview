package com.example.demo.web.controller;

import com.example.demo.service.user.impl.UserServiceImpl;
import com.example.demo.utils.exception.TypeOfResponseTypeProcessingException;
import com.example.demo.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping()
    public ResponseEntity getAllUsers(@RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUser(token));
    }

    @PostMapping()
    public ResponseEntity createUser(@RequestHeader("Authorization") String token,
                                     @RequestBody UserDto userDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(token, userDto));
    }

    @PutMapping()
    public ResponseEntity updateUser(@RequestHeader("Authorization") String token,
                                     @RequestBody UserDto userDto) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.updateUser(token, userDto));
    }

    @DeleteMapping()
    public ResponseEntity deleteUser(@RequestHeader("Authorization") String token,
                                     @PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.deleteUser(token, id));
    }

}
