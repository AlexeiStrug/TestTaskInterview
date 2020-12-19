package com.example.demo.web.mappers;

import com.example.demo.domain.Permission;
import com.example.demo.web.dto.PermissionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DateMapper.class})
public interface PermissionMapper {

    PermissionDto permissionToDto(Permission permission);

    PermissionDto dtoToPermission(PermissionDto permissionDto);
}
