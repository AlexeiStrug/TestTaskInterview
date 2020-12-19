//package com.example.demo.web.mappers;
//
//import com.example.demo.domain.Permission;
//import com.example.demo.domain.Role;
//import com.example.demo.repository.PermissionRepository;
//import com.example.demo.web.dto.RoleDto;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.*;
//
//public abstract class RoleMapperDecorator implements RoleMapper {
//
//    private PermissionRepository permissionRepository;
//    private RoleMapper roleMapper;
//
//    @Autowired
//    public RoleMapperDecorator(PermissionRepository permissionRepository) {
//        this.permissionRepository = permissionRepository;
//    }
//
//    @Override
//    public Role dtoToRole(RoleDto roleDto) {
//        Set<Permission> permissionSet = new HashSet<>();
//        roleDto.getPermissions()
//                .forEach(el -> {
//                    permissionSet.add(permissionRepository.findByName(el));
//                });
//        Role role = roleMapper.dtoToRole(roleDto);
//        role.setPermissions(permissionSet);
//        return role;
//    }
//
//    @Override
//    public RoleDto roleToRoleDto(Role role) {
//        List<String> permissions = new ArrayList<String>();
//        for (Permission el : role.getPermissions()) {
//            permissions.add(el.getName());
//        }
//        RoleDto roleDto = roleMapper.roleToRoleDto(role);
//        roleDto.setPermissions(permissions);
//        return roleDto;
//    }
//}
