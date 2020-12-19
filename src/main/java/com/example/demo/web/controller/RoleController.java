package com.example.demo.web.controller;

import com.example.demo.service.role.RoleService;
import com.example.demo.utils.exception.TypeOfResponseTypeProcessingException;
import com.example.demo.web.dto.RoleDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/role")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping()
    public ResponseEntity getListRoles(@RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.listRoles(token));
    }

    @PostMapping()
    public ResponseEntity createRole(@RequestHeader("Authorization") String token,
                                     @RequestBody RoleDto roleDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.createRole(token, roleDto));
    }

    @PutMapping()
    public ResponseEntity updateRole(@RequestHeader("Authorization") String token,
                                     @RequestBody RoleDto roleDto) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(roleService.updateRole(token, roleDto));
    }

    @DeleteMapping()
    public ResponseEntity deleteRole(@RequestHeader("Authorization") String token,
                                     @PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(roleService.deleteRole(token, id));
    }


}
