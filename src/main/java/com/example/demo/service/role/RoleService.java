package com.example.demo.service.role;

import com.example.demo.utils.builder.CustomResponse;
import com.example.demo.utils.exception.TypeOfResponseTypeProcessingException;
import com.example.demo.web.dto.RoleDto;

import java.util.List;

public interface RoleService {

    CustomResponse createRole(String token, RoleDto createRoleDto) ;

    CustomResponse updateRole(String token, RoleDto updateRoleDto) ;

    CustomResponse deleteRole(String token, Long id) ;

    CustomResponse listRoles(String token) ;
}
