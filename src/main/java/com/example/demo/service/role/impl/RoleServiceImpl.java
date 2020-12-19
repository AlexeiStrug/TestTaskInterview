package com.example.demo.service.role.impl;

import com.example.demo.domain.Permission;
import com.example.demo.domain.Role;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.permission.PermissionService;
import com.example.demo.service.role.RoleService;
import com.example.demo.utils.CheckUserToken;
import com.example.demo.utils.builder.CustomResponse;
import com.example.demo.utils.builder.DtoResponseBuilder;
import com.example.demo.utils.builder.enums.ResponseType;
import com.example.demo.utils.exception.AuthException;
import com.example.demo.utils.exception.ForbiddenAccessToTheSystem;
import com.example.demo.utils.exception.NotFoundException;
import com.example.demo.utils.exception.TypeOfResponseTypeProcessingException;
import com.example.demo.web.dto.RoleDto;
import com.example.demo.web.mappers.RoleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final PermissionService permissionService;
    private final RoleRepository roleRepository;
    private final CheckUserToken checkUserToken;
    private final DtoResponseBuilder responseBuilder;
    private final RoleMapper roleMapper;

    @Override
    public CustomResponse createRole(String token, RoleDto createRoleDto) {
        checkAdminRole(token);

        Optional<Role> found = roleRepository.findByRoleName(createRoleDto.getRoleName());
        if (found.isPresent()) {
            throw new ForbiddenAccessToTheSystem("Role with current name already exist.");
        }

        RoleDto created = roleMapper.roleToRoleDto(roleRepository.save(roleMapper.dtoToRole(createRoleDto)));

        return responseBuilder.buildResponse(ResponseType.SUCCESS, "Role is created", created);
    }

    @Override
    public CustomResponse updateRole(String token, RoleDto updateRoleDto) {
        checkAdminRole(token);

        checkIfRoleExist(updateRoleDto, null);

        RoleDto updated = roleMapper.roleToRoleDto(roleRepository.save(roleMapper.dtoToRole(updateRoleDto)));


        return responseBuilder.buildResponse(ResponseType.SUCCESS, "Role is updated", updated);
    }


    @Override
    public CustomResponse deleteRole(String token, Long id) {
        checkAdminRole(token);

        checkIfRoleExist(null, id);

        roleRepository.deleteById(id);

        return responseBuilder.buildResponse(ResponseType.SUCCESS, "Role is deleted with id -> " + id, "");
    }

    @Override
    public CustomResponse listRoles(String token) {
        checkAdminRole(token);

        List<RoleDto> found = roleRepository.findAll()
                .stream()
                .map(roleMapper::roleToRoleDto)
                .collect(Collectors.toList());

        return responseBuilder.buildResponse(ResponseType.SUCCESS, "Roles is found", found);
    }

    private void checkIfRoleExist(RoleDto updateRoleDto, Long id) {
        Optional<Role> found = null;
        if (updateRoleDto == null) {
            found = roleRepository.findById(id);
        } else {
            if (id == null) found = roleRepository.findById(updateRoleDto.getId());
            if (!found.isPresent()) found = roleRepository.findByRoleName(updateRoleDto.getRoleName());
        }

        if (!found.isPresent())
            throw new NotFoundException("Role with not found.");
    }


    private void checkAdminRole(String token) {
        if (token == null)
            throw new AuthException("User not UNAUTHORIZED");

        Set<Permission> permissions = checkUserToken.loadUserByToken(token).getRole().getPermissions();

        if (!permissionService.isAdmin(permissions))
            throw new ForbiddenAccessToTheSystem("User do not have necessary role for this action");
    }
}
