package com.example.demo.service.user.impl;

import com.example.demo.config.SecurityConfig;
import com.example.demo.domain.Permission;
import com.example.demo.domain.User;
import com.example.demo.domain.enums.PermissionEnum;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.permission.PermissionService;
import com.example.demo.service.user.UserService;
import com.example.demo.utils.CheckUserToken;
import com.example.demo.utils.builder.CustomResponse;
import com.example.demo.utils.builder.DtoResponseBuilder;
import com.example.demo.utils.builder.enums.ResponseType;
import com.example.demo.utils.exception.AuthException;
import com.example.demo.utils.exception.ForbiddenAccessToTheSystem;
import com.example.demo.utils.exception.NotFoundException;
import com.example.demo.web.dto.UserDto;
import com.example.demo.web.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.demo.domain.enums.PermissionEnum.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PermissionService permissionService;
    private final DtoResponseBuilder responseBuilder;
    private final UserRepository userRepository;
    private final SecurityConfig securityConfig;
    private final CheckUserToken checkUserToken;
    private final UserMapper userMapper;

    @Override
    public CustomResponse createUser(String token, UserDto createUserDto) {
        if (!checkRole(token, CREATE))
            throw new ForbiddenAccessToTheSystem("User do not have necessary role for this action");

        createUserDto.setPassword(securityConfig.passwordEncoder(createUserDto.getPassword()));
        UserDto created = userMapper.userToDto(userRepository.save(userMapper.dtoToUser(createUserDto)));

        return responseBuilder.buildResponse(ResponseType.SUCCESS, "User is created", created);
    }

    @Override
    public CustomResponse updateUser(String token, UserDto updateUserDto) {
        if (!checkRole(token, UPDATE))
            throw new ForbiddenAccessToTheSystem("User do not have necessary role for this action");

        checkIfUserExist(updateUserDto, null);

        if (updateUserDto.getPassword() != null && !updateUserDto.getPassword().equals(""))
            updateUserDto.setPassword(securityConfig.passwordEncoder(updateUserDto.getPassword()));

        UserDto updated = userMapper.userToDto(userRepository.save(userMapper.dtoToUser(updateUserDto)));

        return responseBuilder.buildResponse(ResponseType.SUCCESS, "User is updated", updated);
    }

    @Override
    public CustomResponse deleteUser(String token, Long id) {
        if (!checkRole(token, DELETE))
            throw new ForbiddenAccessToTheSystem("User do not have necessary role for this action");

        checkIfUserExist(null, id);

        userRepository.deleteById(id);

        return responseBuilder.buildResponse(ResponseType.SUCCESS, "User is deleted with id -> " + id, "");
    }

    @Override
    public CustomResponse getAllUser(String token) {
        if (!checkRole(token, LIST_ALL))
            throw new ForbiddenAccessToTheSystem("User do not have necessary role for this action");

        List<UserDto> found = userRepository.findAll()
                .stream()
                .map(userMapper::userToDto)
                .collect(Collectors.toList());

        return responseBuilder.buildResponse(ResponseType.SUCCESS, "Users are found", found);
    }


    private User checkIfUserExist(UserDto updateUserDto, Long id) {
        Optional<User> found = null;
        if (id == null || id.equals("")) found = userRepository.findById(updateUserDto.getId());
        if (updateUserDto == null) found = userRepository.findById(id);

        if (found.isEmpty())
            throw new NotFoundException("User not found.");

        return found.get();
    }

    private boolean checkRole(String token, PermissionEnum permission) {
        if (token == null || token.equals(""))
            throw new AuthException("User not UNAUTHORIZED");

        Set<Permission> permissions = checkUserToken.loadUserByToken(token).getRole().getPermissions();
        boolean flag = false;
        switch (permission) {
            case CREATE:
                flag = permissionService.isCanCreate(permissions);
                break;
            case UPDATE:
                flag = permissionService.isCanUpdate(permissions);
                break;
            case DELETE:
                flag = permissionService.isCanDelete(permissions);
                break;
            case LIST_ALL:
                flag = permissionService.isCanList(permissions);
                break;
            default:
                throw new ForbiddenAccessToTheSystem("User do not have necessary role for this action");
        }
        return flag;
    }
}
