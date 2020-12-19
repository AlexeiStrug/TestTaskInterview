package com.example.demo.service.permission;

import com.example.demo.domain.Permission;
import com.example.demo.web.dto.PermissionDto;

import java.util.List;
import java.util.Set;

public interface PermissionService {

    List<PermissionDto> findAll();

    boolean isAdmin(Set<Permission> permissions);

    boolean isCanCreate(Set<Permission> permissions);

    boolean isCanUpdate(Set<Permission> permissions);

    boolean isCanDelete(Set<Permission> permissions);

    boolean isCanList(Set<Permission> permissions);
}
