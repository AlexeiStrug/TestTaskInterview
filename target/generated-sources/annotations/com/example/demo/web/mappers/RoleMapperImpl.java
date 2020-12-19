package com.example.demo.web.mappers;

import com.example.demo.domain.Permission;
import com.example.demo.domain.Role;
import com.example.demo.domain.Role.RoleBuilder;
import com.example.demo.web.dto.RoleDto;
import com.example.demo.web.dto.RoleDto.RoleDtoBuilder;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-12-19T12:55:47+0100",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 11.0.4 (Oracle Corporation)"
)
@Component
public class RoleMapperImpl implements RoleMapper {

    @Autowired
    private DateMapper dateMapper;

    @Override
    public Role dtoToRole(RoleDto roleDto) {
        if ( roleDto == null ) {
            return null;
        }

        RoleBuilder role = Role.builder();

        role.id( roleDto.getId() );
        role.createdAt( dateMapper.asTimestamp( roleDto.getCreatedAt() ) );
        role.updatedAt( dateMapper.asTimestamp( roleDto.getUpdatedAt() ) );
        role.roleName( roleDto.getRoleName() );
        Set<Permission> set = roleDto.getPermissions();
        if ( set != null ) {
            role.permissions( new HashSet<Permission>( set ) );
        }

        return role.build();
    }

    @Override
    public RoleDto roleToRoleDto(Role role) {
        if ( role == null ) {
            return null;
        }

        RoleDtoBuilder roleDto = RoleDto.builder();

        roleDto.id( role.getId() );
        roleDto.roleName( role.getRoleName() );
        Set<Permission> set = role.getPermissions();
        if ( set != null ) {
            roleDto.permissions( new HashSet<Permission>( set ) );
        }
        roleDto.createdAt( dateMapper.asOffsetDateTime( role.getCreatedAt() ) );
        roleDto.updatedAt( dateMapper.asOffsetDateTime( role.getUpdatedAt() ) );

        return roleDto.build();
    }
}
