package com.example.demo.web.mappers;

import com.example.demo.domain.Permission;
import com.example.demo.domain.Role;
import com.example.demo.domain.Role.RoleBuilder;
import com.example.demo.domain.User;
import com.example.demo.domain.User.UserBuilder;
import com.example.demo.web.dto.RoleDto;
import com.example.demo.web.dto.RoleDto.RoleDtoBuilder;
import com.example.demo.web.dto.UserDto;
import com.example.demo.web.dto.UserDto.UserDtoBuilder;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-12-19T12:17:47+0100",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 11.0.4 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    private DateMapper dateMapper;

    @Override
    public UserDto userToDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDtoBuilder userDto = UserDto.builder();

        userDto.id( user.getId() );
        userDto.username( user.getUsername() );
        userDto.password( user.getPassword() );
        userDto.role( roleToRoleDto( user.getRole() ) );
        userDto.createdAt( dateMapper.asOffsetDateTime( user.getCreatedAt() ) );
        userDto.updatedAt( dateMapper.asOffsetDateTime( user.getUpdatedAt() ) );

        return userDto.build();
    }

    @Override
    public User dtoToUser(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        UserBuilder user = User.builder();

        user.id( userDto.getId() );
        user.createdAt( dateMapper.asTimestamp( userDto.getCreatedAt() ) );
        user.updatedAt( dateMapper.asTimestamp( userDto.getUpdatedAt() ) );
        user.username( userDto.getUsername() );
        user.password( userDto.getPassword() );
        user.role( roleDtoToRole( userDto.getRole() ) );

        return user.build();
    }

    protected RoleDto roleToRoleDto(Role role) {
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

    protected Role roleDtoToRole(RoleDto roleDto) {
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
}
