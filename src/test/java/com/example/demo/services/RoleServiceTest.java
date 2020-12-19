package com.example.demo.services;

import com.example.demo.domain.Permission;
import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.permission.PermissionService;
import com.example.demo.service.role.impl.RoleServiceImpl;
import com.example.demo.utils.CheckUserToken;
import com.example.demo.utils.InitData;
import com.example.demo.utils.builder.CustomResponse;
import com.example.demo.utils.builder.DtoResponseBuilder;
import com.example.demo.utils.builder.enums.ResponseType;
import com.example.demo.utils.exception.AuthException;
import com.example.demo.utils.exception.ForbiddenAccessToTheSystem;
import com.example.demo.utils.exception.NotFoundException;
import com.example.demo.web.dto.PermissionDto;
import com.example.demo.web.dto.RoleDto;
import com.example.demo.web.mappers.RoleMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RoleServiceTest {

    @InjectMocks
    private RoleServiceImpl roleService;

    @Mock
    private PermissionService permissionService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private CheckUserToken checkUserToken;

    @Mock
    private DtoResponseBuilder responseBuilder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleMapper roleMapper;

    List<PermissionDto> fullPermissionDtos = null;
    List<Permission> fullPermissions = null;
    Role adminRole = null;
    Role customRole = null;
    RoleDto roleDto = null;
    User user = null;
    CustomResponse customResponse = null;

    String token = "some-token-value";

    @Before
    public void setUp() {
        fullPermissions = new InitData().initFullPermissions();
        fullPermissionDtos = new InitData().initFullPermissionsDto();
        adminRole = new InitData().initRole(fullPermissions);
        roleDto = new InitData().initRoleDto(fullPermissions);
        user = new InitData().initAdminUser();
    }

    @Test
    public void test_createRole_basic() {
        user.setRole(adminRole);
        customResponse = new InitData().generateCustomResponse(ResponseType.SUCCESS, "Role is created", roleDto);

        when(checkUserToken.loadUserByToken(any(String.class))).thenReturn(user);
        when(permissionService.isAdmin(any(Set.class))).thenReturn(true);

        when(roleMapper.dtoToRole(any(RoleDto.class))).thenReturn(adminRole);
        when(roleRepository.save(any(Role.class))).thenReturn(adminRole);
        when(roleMapper.roleToRoleDto(any(Role.class))).thenReturn(roleDto);

        when(responseBuilder.buildResponse(any(ResponseType.class), any(String.class), any(RoleDto.class))).thenReturn(customResponse);

        CustomResponse response = roleService.createRole(token, roleDto);

        Assert.assertNotNull(response);
        Assert.assertEquals(response, customResponse);
        Assert.assertEquals(response.getMessage(), "Role is created");
        Assert.assertEquals(response.getResponseType(), ResponseType.SUCCESS);
        Assert.assertEquals(response.getResult(), roleDto);
    }

    @Test(expected = ForbiddenAccessToTheSystem.class)
    public void test_createRole_nonAdminUser_exception() {
        customRole = new InitData().initRole(new InitData().initCreatePermissions());
        user.setRole(customRole);

        when(checkUserToken.loadUserByToken(any(String.class))).thenReturn(user);
        when(permissionService.isAdmin(any(Set.class))).thenReturn(false);

        roleService.createRole(token, roleDto);
    }

    @Test(expected = ForbiddenAccessToTheSystem.class)
    public void test_createRole_withExistRole_exception() {
        customRole = new InitData().initRole(new InitData().initCreatePermissions());
        user.setRole(customRole);

        when(checkUserToken.loadUserByToken(any(String.class))).thenReturn(user);
        when(permissionService.isAdmin(any(Set.class))).thenReturn(false);

        roleService.createRole(token, roleDto);
    }

    @Test(expected = ForbiddenAccessToTheSystem.class)
    public void test_createRole_whenUserFound_exception() {
        customRole = new InitData().initRole(new InitData().initCreatePermissions());
        user.setRole(customRole);

        when(checkUserToken.loadUserByToken(any(String.class))).thenReturn(user);
        when(permissionService.isAdmin(any(Set.class))).thenReturn(false);

        roleService.createRole(token, roleDto);
    }

    @Test(expected = AuthException.class)
    public void test_createRole_tokenNull_exception() {
        roleService.createRole(null, roleDto);
    }


    @Test
    public void test_updateRole_basic() {
        user.setRole(adminRole);
        customResponse = new InitData().generateCustomResponse(ResponseType.SUCCESS, "Role is updated", roleDto);

        when(checkUserToken.loadUserByToken(any(String.class))).thenReturn(user);
        when(permissionService.isAdmin(any(Set.class))).thenReturn(true);

        when(roleRepository.findById(any(Long.class))).thenReturn(java.util.Optional.ofNullable(adminRole));

        when(roleMapper.dtoToRole(any(RoleDto.class))).thenReturn(adminRole);
        when(roleRepository.save(any(Role.class))).thenReturn(adminRole);
        when(roleMapper.roleToRoleDto(any(Role.class))).thenReturn(roleDto);

        when(responseBuilder.buildResponse(any(ResponseType.class), any(String.class), any(RoleDto.class))).thenReturn(customResponse);

        CustomResponse response = roleService.updateRole(token, roleDto);

        Assert.assertNotNull(response);
        Assert.assertEquals(response, customResponse);
        Assert.assertEquals(response.getMessage(), "Role is updated");
        Assert.assertEquals(response.getResponseType(), ResponseType.SUCCESS);
        Assert.assertEquals(response.getResult(), roleDto);
    }

    @Test(expected = NotFoundException.class)
    public void test_updateRole_ifRoleNotFound_byDtoIdAndUsernameNull() {
        user.setRole(adminRole);

        when(checkUserToken.loadUserByToken(any(String.class))).thenReturn(user);
        when(permissionService.isAdmin(any(Set.class))).thenReturn(true);

        when(roleRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        when(roleRepository.findByRoleName(any(String.class))).thenReturn(Optional.empty());

        roleService.updateRole(token, roleDto);
    }

    @Test(expected = AuthException.class)
    public void test_updateRole_tokenNull_exception() {
        roleService.updateRole(null, roleDto);
    }


    @Test
    public void test_deleteRole_basic() {
        user.setRole(adminRole);
        long id = 1L;
        customResponse = new InitData().generateCustomResponse(ResponseType.SUCCESS, "Role is deleted with id -> " + id, null);

        when(checkUserToken.loadUserByToken(any(String.class))).thenReturn(user);
        when(permissionService.isAdmin(any(Set.class))).thenReturn(true);

        when(roleRepository.findById(any(Long.class))).thenReturn(java.util.Optional.ofNullable(adminRole));

        when(responseBuilder.buildResponse(any(ResponseType.class), any(String.class), any(Object.class))).thenReturn(customResponse);

        doNothing().when(roleRepository).deleteById(id);

        CustomResponse response = roleService.deleteRole(token, id);

        verify(roleRepository).deleteById(id);
        Assert.assertNotNull(response);
        Assert.assertEquals(response, customResponse);
        Assert.assertEquals(response.getMessage(), "Role is deleted with id -> " + id);
        Assert.assertEquals(response.getResponseType(), ResponseType.SUCCESS);
        Assert.assertNull(response.getResult());
    }

    @Test(expected = NotFoundException.class)
    public void test_deleteRole_IfRoleNotFound() {
        user.setRole(adminRole);

        when(checkUserToken.loadUserByToken(any(String.class))).thenReturn(user);
        when(permissionService.isAdmin(any(Set.class))).thenReturn(true);

        when(roleRepository.findById(any(Long.class))).thenReturn(java.util.Optional.empty());

        roleService.deleteRole(token, 1L);
    }

    @Test(expected = AuthException.class)
    public void test_deleteRole_tokenNull_exception() {
        roleService.deleteRole(null, 1L);
    }

    @Test
    public void test_listRoles_basic() {
        List<Role> roles = new ArrayList<>();
        roles.add(adminRole);
        roles.add(adminRole);
        roles.add(adminRole);

        List<RoleDto> roleDtos = new ArrayList<>();
        roleDtos.add(roleDto);
        roleDtos.add(roleDto);
        roleDtos.add(roleDto);

        user.setRole(adminRole);
        customResponse = new InitData().generateCustomResponse(ResponseType.SUCCESS, "Role is found", roleDtos);

        when(checkUserToken.loadUserByToken(any(String.class))).thenReturn(user);
        when(permissionService.isAdmin(any(Set.class))).thenReturn(true);

        when(roleRepository.findAll()).thenReturn(roles);

        when(roleMapper.roleToRoleDto(any(Role.class))).thenReturn(roleDto);

        when(responseBuilder.buildResponse(any(ResponseType.class), any(String.class), any(Object.class))).thenReturn(customResponse);

        CustomResponse response = roleService.listRoles(token);

        Assert.assertNotNull(response);
        Assert.assertEquals(customResponse, response);
        Assert.assertEquals("Role is found", response.getMessage());
        Assert.assertEquals(ResponseType.SUCCESS, response.getResponseType());
        Assert.assertNotNull(response.getResult());
        Assert.assertEquals(roleDtos, Arrays.asList(response.getResult()).get(0));
    }

    @Test
    public void test_listRoles_emptyReturn() {
        List<Role> roles = new ArrayList<>();
        List<RoleDto> roleDtos = new ArrayList<>();

        user.setRole(adminRole);
        customResponse = new InitData().generateCustomResponse(ResponseType.SUCCESS, "Role is found", roleDtos);

        when(checkUserToken.loadUserByToken(any(String.class))).thenReturn(user);
        when(permissionService.isAdmin(any(Set.class))).thenReturn(true);

        when(roleRepository.findAll()).thenReturn(roles);

        when(responseBuilder.buildResponse(any(ResponseType.class), any(String.class), any(Object.class))).thenReturn(customResponse);

        CustomResponse response = roleService.listRoles(token);

        Assert.assertNotNull(response);
        Assert.assertEquals(customResponse, response);
        Assert.assertEquals("Role is found", response.getMessage());
        Assert.assertEquals(ResponseType.SUCCESS, response.getResponseType());
        Assert.assertNotNull(response.getResult());
        Assert.assertEquals(roleDtos, Arrays.asList(response.getResult()).get(0));
    }

    @Test(expected = AuthException.class)
    public void test_listRoles_tokenNull_exception() {
        roleService.listRoles(null);
    }
}
