package com.example.demo.service.permission.impl;

import com.example.demo.domain.Permission;
import com.example.demo.domain.enums.PermissionEnum;
import com.example.demo.repository.PermissionRepository;
import com.example.demo.service.permission.PermissionService;
import com.example.demo.web.dto.PermissionDto;
import com.example.demo.web.mappers.PermissionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Override
    public List<PermissionDto> findAll() {
        return permissionRepository.findAll()
                .stream()
                .map(permissionMapper::permissionToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAdmin(Set<Permission> permissions) {
        return permissions.size() == 4;
    }

    @Override
    public boolean isCanCreate(Set<Permission> permissions) {
        return permissions.stream().anyMatch(el -> el.getName().equals(PermissionEnum.CREATE.getName()));
    }

    @Override
    public boolean isCanUpdate(Set<Permission> permissions) {
        return permissions.stream().anyMatch(el -> el.getName().equals(PermissionEnum.UPDATE.getName()));
    }

    @Override
    public boolean isCanDelete(Set<Permission> permissions) {
        return permissions.stream().anyMatch(el -> el.getName().equals(PermissionEnum.DELETE.getName()));
    }

    @Override
    public boolean isCanList(Set<Permission> permissions) {
        return permissions.stream().anyMatch(el -> el.getName().equals(PermissionEnum.LIST_ALL.getName()));
    }


}
