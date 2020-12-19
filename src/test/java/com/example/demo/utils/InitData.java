package com.example.demo.utils;

import com.example.demo.domain.PasswordResetToken;
import com.example.demo.domain.Permission;
import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.example.demo.domain.enums.PermissionEnum;
import com.example.demo.utils.builder.CustomResponse;
import com.example.demo.utils.builder.enums.ResponseType;
import com.example.demo.web.dto.PermissionDto;
import com.example.demo.web.dto.RoleDto;
import com.example.demo.web.dto.UserDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class InitData {
    List<PermissionDto> permissionDtos = null;
    List<Permission> permissions = null;

    public List<Permission> initFullPermissions() {
        permissions = new ArrayList<>();
        permissions.add(Permission.builder().id(1L).name(PermissionEnum.CREATE.getName()).build());
        permissions.add(Permission.builder().id(2L).name(PermissionEnum.UPDATE.getName()).build());
        permissions.add(Permission.builder().id(3L).name(PermissionEnum.DELETE.getName()).build());
        permissions.add(Permission.builder().id(4L).name(PermissionEnum.LIST_ALL.getName()).build());

        return permissions;
    }

    public List<Permission> initCreatePermissions() {
        permissions = new ArrayList<>();
        permissions.add(Permission.builder().id(1L).name(PermissionEnum.CREATE.getName()).build());
        return permissions;
    }

    public List<Permission> initUpdatePermissions() {
        permissions = new ArrayList<>();
        permissions.add(Permission.builder().id(2L).name(PermissionEnum.UPDATE.getName()).build());

        return permissions;
    }

    public List<Permission> initDeletePermissions() {
        permissions = new ArrayList<>();
        permissions.add(Permission.builder().id(3L).name(PermissionEnum.DELETE.getName()).build());

        return permissions;
    }

    public List<Permission> initListPermissions() {
        permissions = new ArrayList<>();
        permissions.add(Permission.builder().id(4L).name(PermissionEnum.LIST_ALL.getName()).build());

        return permissions;
    }

    public List<PermissionDto> initFullPermissionsDto() {
        permissionDtos = new ArrayList<>();
        permissionDtos.add(PermissionDto.builder().id(1L).name(PermissionEnum.CREATE.getName()).build());
        permissionDtos.add(PermissionDto.builder().id(2L).name(PermissionEnum.UPDATE.getName()).build());
        permissionDtos.add(PermissionDto.builder().id(3L).name(PermissionEnum.DELETE.getName()).build());
        permissionDtos.add(PermissionDto.builder().id(4L).name(PermissionEnum.LIST_ALL.getName()).build());

        return permissionDtos;
    }

    public Role initRole(List<Permission> permissions) {
        if (permissions == null) return Role.builder().id(1L).roleName("Admin").build();
        return Role.builder().id(1L).roleName("Admin").permissions(new HashSet<>(permissions)).build();
    }

    public RoleDto initRoleDto(List<Permission> permissions) {
        if (permissions == null) return RoleDto.builder().id(1L).roleName("Admin").build();
        return RoleDto.builder().id(1L).roleName("Admin").permissions(new HashSet<>(permissions)).build();
    }

    public PasswordResetToken initPasswordResetToken() {
        return PasswordResetToken.builder().id(1L).token("some_token").build();
    }

    public User initAdminUser() {
        return User.builder().id(1L).username("Admin").password("admin").build();
    }

    public UserDto initUserDto() {
        return UserDto.builder().id(1L).username("Admin").password("admin").build();
    }

    public CustomResponse generateCustomResponse(ResponseType responseType, String message, Object object) {
        return new CustomResponse().builder().responseType(responseType).message(message).result(object).build();
    }


}
