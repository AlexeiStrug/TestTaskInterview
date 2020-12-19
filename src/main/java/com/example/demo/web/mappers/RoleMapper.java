package com.example.demo.web.mappers;

import com.example.demo.domain.Role;
import com.example.demo.web.dto.RoleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DateMapper.class})
public interface RoleMapper {

    Role dtoToRole(RoleDto roleDto);

    RoleDto roleToRoleDto(Role role);
}
