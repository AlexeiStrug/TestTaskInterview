package com.example.demo.services;

import com.example.demo.config.SecurityConfig;
import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.permission.PermissionService;
import com.example.demo.service.user.impl.UserServiceImpl;
import com.example.demo.utils.CheckUserToken;
import com.example.demo.utils.InitData;
import com.example.demo.utils.builder.CustomResponse;
import com.example.demo.utils.builder.DtoResponseBuilder;
import com.example.demo.utils.builder.enums.ResponseType;
import com.example.demo.utils.exception.AuthException;
import com.example.demo.utils.exception.ForbiddenAccessToTheSystem;
import com.example.demo.utils.exception.NotFoundException;
import com.example.demo.web.dto.RoleDto;
import com.example.demo.web.dto.UserDto;
import com.example.demo.web.mappers.UserMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private PermissionService permissionService;
    @Mock
    private DtoResponseBuilder responseBuilder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SecurityConfig securityConfig;
    @Mock
    private CheckUserToken checkUserToken;
    @Mock
    private UserMapper userMapper;

    String token = "some-token-value";
    String hash = "asdasdasdsjkashdjkasd";
    Long id = 1L;
    User user = null;
    UserDto userDto = null;
    Role customRole = null;
    RoleDto customRoleDto = null;
    CustomResponse customResponse = null;


    @Before
    public void setUp() {
        user = new InitData().initAdminUser();
        userDto = new InitData().initUserDto();
    }

    @Test
    public void test_createUser_basic() {
        customRole = new InitData().initRole(new InitData().initCreatePermissions());
        user.setRole(customRole);

        customRoleDto = new InitData().initRoleDto(new InitData().initCreatePermissions());
        userDto.setRole(customRoleDto);
        userDto.setPassword(hash);

        customResponse = new InitData().generateCustomResponse(ResponseType.SUCCESS, "User is created", userDto);

        when(checkUserToken.loadUserByToken(token)).thenReturn(user);
        when(permissionService.isCanCreate(any(Set.class))).thenReturn(true);

        when(securityConfig.passwordEncoder(any(String.class))).thenReturn(hash);

        when(userMapper.dtoToUser(any(UserDto.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.userToDto(any(User.class))).thenReturn(userDto);

        when(responseBuilder.buildResponse(any(ResponseType.class), any(String.class), any(UserDto.class))).thenReturn(customResponse);

        CustomResponse response = userService.createUser(token, userDto);

        Assert.assertNotNull(response);
        Assert.assertEquals(response, customResponse);
        Assert.assertEquals(response.getMessage(), "User is created");
        Assert.assertEquals(response.getResponseType(), ResponseType.SUCCESS);
        Assert.assertEquals(response.getResult(), userDto);
    }

    @Test(expected = ForbiddenAccessToTheSystem.class)
    public void test_createUser_withoutCreateAccess_withListAccess() {
        customRole = new InitData().initRole(new InitData().initListPermissions());
        user.setRole(customRole);

        customRoleDto = new InitData().initRoleDto(new InitData().initListPermissions());
        userDto.setRole(customRoleDto);

        when(checkUserToken.loadUserByToken(token)).thenReturn(user);
        when(permissionService.isCanCreate(any(Set.class))).thenReturn(false);

        userService.createUser(token, userDto);
    }

    @Test(expected = ForbiddenAccessToTheSystem.class)
    public void test_createUser_withoutCreateAccess_withUpdateAccess() {
        customRole = new InitData().initRole(new InitData().initUpdatePermissions());
        user.setRole(customRole);

        customRoleDto = new InitData().initRoleDto(new InitData().initUpdatePermissions());
        userDto.setRole(customRoleDto);

        when(checkUserToken.loadUserByToken(token)).thenReturn(user);
        when(permissionService.isCanCreate(any(Set.class))).thenReturn(false);

        userService.createUser(token, userDto);
    }

    @Test(expected = ForbiddenAccessToTheSystem.class)
    public void test_createUser_withoutCreateAccess_withDeleteAccess() {
        customRole = new InitData().initRole(new InitData().initDeletePermissions());
        user.setRole(customRole);

        customRoleDto = new InitData().initRoleDto(new InitData().initDeletePermissions());
        userDto.setRole(customRoleDto);

        when(checkUserToken.loadUserByToken(token)).thenReturn(user);
        when(permissionService.isCanCreate(any(Set.class))).thenReturn(false);

        userService.createUser(token, userDto);
    }

    @Test(expected = AuthException.class)
    public void test_createUser_withEmptyToken() {
        userService.createUser(null, userDto);
    }

    @Test(expected = ForbiddenAccessToTheSystem.class)
    public void test_createUser_withoutPermissions() {
        customRole = new InitData().initRole(null);
        user.setRole(customRole);

        customRoleDto = new InitData().initRoleDto(null);
        userDto.setRole(customRoleDto);

        when(checkUserToken.loadUserByToken(token)).thenReturn(user);

        userService.createUser(token, userDto);
    }

    @Test
    public void test_updateUser_basic() {
        customRole = new InitData().initRole(new InitData().initUpdatePermissions());
        user.setRole(customRole);

        customRoleDto = new InitData().initRoleDto(new InitData().initUpdatePermissions());
        userDto.setRole(customRoleDto);
        userDto.setPassword(hash);

        customResponse = new InitData().generateCustomResponse(ResponseType.SUCCESS, "User is updated", userDto);

        when(checkUserToken.loadUserByToken(token)).thenReturn(user);
        when(permissionService.isCanUpdate(any(Set.class))).thenReturn(true);

        when(userRepository.findById(any(Long.class))).thenReturn(java.util.Optional.ofNullable(user));

        when(securityConfig.passwordEncoder(any(String.class))).thenReturn(hash);

        when(userMapper.dtoToUser(any(UserDto.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.userToDto(any(User.class))).thenReturn(userDto);

        when(responseBuilder.buildResponse(any(ResponseType.class), any(String.class), any(UserDto.class))).thenReturn(customResponse);

        CustomResponse response = userService.updateUser(token, userDto);

        Assert.assertNotNull(response);
        Assert.assertEquals(response, customResponse);
        Assert.assertEquals(response.getMessage(), "User is updated");
        Assert.assertEquals(response.getResponseType(), ResponseType.SUCCESS);
        Assert.assertEquals(response.getResult(), userDto);
    }

    @Test(expected = NotFoundException.class)
    public void test_updateUser_whenUserNotFound() {
        customRole = new InitData().initRole(new InitData().initUpdatePermissions());
        user.setRole(customRole);

        customRoleDto = new InitData().initRoleDto(new InitData().initUpdatePermissions());
        userDto.setRole(customRoleDto);
        userDto.setPassword(hash);

        customResponse = new InitData().generateCustomResponse(ResponseType.SUCCESS, "User is updated", userDto);

        when(checkUserToken.loadUserByToken(token)).thenReturn(user);
        when(permissionService.isCanUpdate(any(Set.class))).thenReturn(true);

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        userService.updateUser(token, userDto);

    }

    @Test(expected = ForbiddenAccessToTheSystem.class)
    public void test_updateUser_withoutCreateAccess_withListAccess() {
        customRole = new InitData().initRole(new InitData().initListPermissions());
        user.setRole(customRole);

        customRoleDto = new InitData().initRoleDto(new InitData().initListPermissions());
        userDto.setRole(customRoleDto);

        when(checkUserToken.loadUserByToken(token)).thenReturn(user);
        when(permissionService.isCanUpdate(any(Set.class))).thenReturn(false);

        userService.updateUser(token, userDto);
    }

    @Test(expected = ForbiddenAccessToTheSystem.class)
    public void test_updateUser_withoutCreateAccess_withUpdateAccess() {
        customRole = new InitData().initRole(new InitData().initCreatePermissions());
        user.setRole(customRole);

        customRoleDto = new InitData().initRoleDto(new InitData().initCreatePermissions());
        userDto.setRole(customRoleDto);

        when(checkUserToken.loadUserByToken(token)).thenReturn(user);
        when(permissionService.isCanUpdate(any(Set.class))).thenReturn(false);

        userService.updateUser(token, userDto);
    }

    @Test(expected = ForbiddenAccessToTheSystem.class)
    public void test_updateUser_withoutCreateAccess_withDeleteAccess() {
        customRole = new InitData().initRole(new InitData().initDeletePermissions());
        user.setRole(customRole);

        customRoleDto = new InitData().initRoleDto(new InitData().initDeletePermissions());
        userDto.setRole(customRoleDto);

        when(checkUserToken.loadUserByToken(token)).thenReturn(user);
        when(permissionService.isCanUpdate(any(Set.class))).thenReturn(false);

        userService.updateUser(token, userDto);
    }

    @Test(expected = AuthException.class)
    public void test_updateUser_withEmptyToken() {
        userService.updateUser(null, userDto);
    }

    @Test(expected = ForbiddenAccessToTheSystem.class)
    public void test_updateUserr_withoutPermissions() {
        customRole = new InitData().initRole(null);
        user.setRole(customRole);

        customRoleDto = new InitData().initRoleDto(null);
        userDto.setRole(customRoleDto);

        when(checkUserToken.loadUserByToken(token)).thenReturn(user);

        userService.updateUser(token, userDto);
    }

    @Test
    public void test_deleteUser_basic() {
        customRole = new InitData().initRole(new InitData().initDeletePermissions());
        user.setRole(customRole);

        customRoleDto = new InitData().initRoleDto(new InitData().initDeletePermissions());
        userDto.setRole(customRoleDto);
        userDto.setPassword(hash);

        customResponse = new InitData().generateCustomResponse(ResponseType.SUCCESS, "User is deleted with id -> " + id, null);

        when(checkUserToken.loadUserByToken(token)).thenReturn(user);
        when(permissionService.isCanDelete(any(Set.class))).thenReturn(true);

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));

        doNothing().when(userRepository).deleteById(id);

        when(responseBuilder.buildResponse(any(ResponseType.class), any(String.class), any(Object.class))).thenReturn(customResponse);

        CustomResponse response = userService.deleteUser(token, id);

        verify(userRepository).deleteById(id);
        Assert.assertNotNull(response);
        Assert.assertEquals(response, customResponse);
        Assert.assertEquals(response.getMessage(), "User is deleted with id -> " + id);
        Assert.assertEquals(response.getResponseType(), ResponseType.SUCCESS);
    }

    @Test(expected = NotFoundException.class)
    public void test_deleteUser_whenUserNotFound() {
        customRole = new InitData().initRole(new InitData().initDeletePermissions());
        user.setRole(customRole);

        customRoleDto = new InitData().initRoleDto(new InitData().initDeletePermissions());
        userDto.setRole(customRoleDto);
        userDto.setPassword(hash);

        when(checkUserToken.loadUserByToken(token)).thenReturn(user);
        when(permissionService.isCanDelete(any(Set.class))).thenReturn(true);

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        userService.deleteUser(token, id);

    }

    @Test(expected = ForbiddenAccessToTheSystem.class)
    public void test_deleteUser_withoutCreateAccess_withListAccess() {
        customRole = new InitData().initRole(new InitData().initListPermissions());
        user.setRole(customRole);

        customRoleDto = new InitData().initRoleDto(new InitData().initListPermissions());
        userDto.setRole(customRoleDto);

        when(checkUserToken.loadUserByToken(token)).thenReturn(user);
        when(permissionService.isCanDelete(any(Set.class))).thenReturn(false);

        userService.deleteUser(token, id);
    }

    @Test(expected = ForbiddenAccessToTheSystem.class)
    public void test_deleteUser_withoutCreateAccess_withUpdateAccess() {
        customRole = new InitData().initRole(new InitData().initUpdatePermissions());
        user.setRole(customRole);

        customRoleDto = new InitData().initRoleDto(new InitData().initUpdatePermissions());
        userDto.setRole(customRoleDto);

        when(checkUserToken.loadUserByToken(token)).thenReturn(user);
        when(permissionService.isCanDelete(any(Set.class))).thenReturn(false);

        userService.deleteUser(token, id);
    }

    @Test(expected = ForbiddenAccessToTheSystem.class)
    public void test_updateUser_withoutCreateAccess_withCreateAccess() {
        customRole = new InitData().initRole(new InitData().initCreatePermissions());
        user.setRole(customRole);

        customRoleDto = new InitData().initRoleDto(new InitData().initCreatePermissions());
        userDto.setRole(customRoleDto);

        when(checkUserToken.loadUserByToken(token)).thenReturn(user);
        when(permissionService.isCanDelete(any(Set.class))).thenReturn(false);

        userService.deleteUser(token, id);
    }

    @Test(expected = AuthException.class)
    public void test_deleteUser_withEmptyToken() {
        userService.deleteUser(null, id);
    }

    @Test(expected = ForbiddenAccessToTheSystem.class)
    public void test_deleteUser_withoutPermissions() {
        customRole = new InitData().initRole(null);
        user.setRole(customRole);

        customRoleDto = new InitData().initRoleDto(null);
        userDto.setRole(customRoleDto);

        when(checkUserToken.loadUserByToken(token)).thenReturn(user);

        userService.deleteUser(token, id);
    }

    @Test
    public void test_getAllUser_basic() {
        customRole = new InitData().initRole(new InitData().initListPermissions());
        user.setRole(customRole);

        customRoleDto = new InitData().initRoleDto(new InitData().initListPermissions());
        userDto.setRole(customRoleDto);

        List<User> users = new ArrayList<>();
        users.add(user);

        List<UserDto> usersDto = new ArrayList<>();
        usersDto.add(userDto);

        customResponse = new InitData().generateCustomResponse(ResponseType.SUCCESS, "Users are found", usersDto);

        when(checkUserToken.loadUserByToken(token)).thenReturn(user);
        when(permissionService.isCanList(any(Set.class))).thenReturn(true);

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.userToDto(any(User.class))).thenReturn(userDto);

        when(responseBuilder.buildResponse(any(ResponseType.class), any(String.class), any(List.class))).thenReturn(customResponse);

        CustomResponse response = userService.getAllUser(token);

        Assert.assertNotNull(response);
        Assert.assertEquals(response, customResponse);
        Assert.assertEquals(response.getMessage(), "Users are found");
        Assert.assertEquals(response.getResponseType(), ResponseType.SUCCESS);
        Assert.assertEquals(response.getResult(), usersDto);
    }

    @Test(expected = ForbiddenAccessToTheSystem.class)
    public void test_getAllUser_withoutCreateAccess_withDeleteAccess() {
        customRole = new InitData().initRole(new InitData().initDeletePermissions());
        user.setRole(customRole);

        customRoleDto = new InitData().initRoleDto(new InitData().initDeletePermissions());
        userDto.setRole(customRoleDto);

        when(checkUserToken.loadUserByToken(token)).thenReturn(user);
        when(permissionService.isCanList(any(Set.class))).thenReturn(false);

        userService.getAllUser(token);
    }

    @Test(expected = ForbiddenAccessToTheSystem.class)
    public void test_getAllUser_withoutCreateAccess_withUpdateAccess() {
        customRole = new InitData().initRole(new InitData().initUpdatePermissions());
        user.setRole(customRole);

        customRoleDto = new InitData().initRoleDto(new InitData().initUpdatePermissions());
        userDto.setRole(customRoleDto);

        when(checkUserToken.loadUserByToken(token)).thenReturn(user);
        when(permissionService.isCanList(any(Set.class))).thenReturn(false);

        userService.getAllUser(token);
    }

    @Test(expected = ForbiddenAccessToTheSystem.class)
    public void test_getAllUser_withoutCreateAccess_withCreateAccess() {
        customRole = new InitData().initRole(new InitData().initCreatePermissions());
        user.setRole(customRole);

        customRoleDto = new InitData().initRoleDto(new InitData().initCreatePermissions());
        userDto.setRole(customRoleDto);

        when(checkUserToken.loadUserByToken(token)).thenReturn(user);
        when(permissionService.isCanList(any(Set.class))).thenReturn(false);

        userService.getAllUser(token);
    }

    @Test(expected = AuthException.class)
    public void test_getAllUser_withEmptyToken() {
        userService.getAllUser(null);
    }

    @Test(expected = ForbiddenAccessToTheSystem.class)
    public void test_getAllUser_withoutPermissions() {
        customRole = new InitData().initRole(null);
        user.setRole(customRole);

        when(checkUserToken.loadUserByToken(token)).thenReturn(user);

        userService.getAllUser(token);
    }
}
