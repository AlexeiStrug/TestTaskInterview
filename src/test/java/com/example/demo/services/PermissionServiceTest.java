package com.example.demo.services;

import com.example.demo.domain.Permission;
import com.example.demo.repository.PermissionRepository;
import com.example.demo.service.permission.impl.PermissionServiceImpl;
import com.example.demo.utils.InitData;
import com.example.demo.web.dto.PermissionDto;
import com.example.demo.web.mappers.PermissionMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PermissionServiceTest {

    @InjectMocks
    PermissionServiceImpl permissionService;

    @Mock
    PermissionRepository permissionRepository;

    @Mock
    PermissionMapper permissionMapper;

    List<PermissionDto> permissionDtos = null;
    List<Permission> permissions = null;

    @Before
    public void setUp() {
        permissions = new InitData().initFullPermissions();
        permissionDtos = new InitData().initFullPermissionsDto();
    }


    @Test
    public void findAll_basic() {
        when(permissionRepository.findAll()).thenReturn(permissions);
        when(permissionMapper.permissionToDto(any(Permission.class))).thenReturn(permissionDtos.get(0));

        List<PermissionDto> expected = permissionService.findAll();

        Assert.assertNotNull(expected);
        Assert.assertEquals(expected.size(), 4);
        Assert.assertEquals(expected.get(0), permissionDtos.get(0));
    }

    @Test
    public void findAll_emptyArray() {
        permissions.clear();
        permissionDtos.clear();

        when(permissionRepository.findAll()).thenReturn(permissions);
//        when(permissionMapper.permissionToDto(any(Permission.class))).thenReturn(permissionDtos.get(0));

        List<PermissionDto> expected = permissionService.findAll();

        Assert.assertEquals(0, expected.size());
    }
}
