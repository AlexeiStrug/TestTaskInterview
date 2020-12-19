package com.example.demo.web.mappers;

import com.example.demo.domain.Permission;
import com.example.demo.web.dto.PermissionDto;
import com.example.demo.web.dto.PermissionDto.PermissionDtoBuilder;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-12-19T12:55:46+0100",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 11.0.4 (Oracle Corporation)"
)
@Component
public class PermissionMapperImpl implements PermissionMapper {

    @Autowired
    private DateMapper dateMapper;

    @Override
    public PermissionDto permissionToDto(Permission permission) {
        if ( permission == null ) {
            return null;
        }

        PermissionDtoBuilder permissionDto = PermissionDto.builder();

        permissionDto.id( permission.getId() );
        permissionDto.name( permission.getName() );
        permissionDto.createdAt( dateMapper.asOffsetDateTime( permission.getCreatedAt() ) );
        permissionDto.updatedAt( dateMapper.asOffsetDateTime( permission.getUpdatedAt() ) );

        return permissionDto.build();
    }

    @Override
    public PermissionDto dtoToPermission(PermissionDto permissionDto) {
        if ( permissionDto == null ) {
            return null;
        }

        PermissionDtoBuilder permissionDto1 = PermissionDto.builder();

        permissionDto1.id( permissionDto.getId() );
        permissionDto1.name( permissionDto.getName() );
        permissionDto1.createdAt( permissionDto.getCreatedAt() );
        permissionDto1.updatedAt( permissionDto.getUpdatedAt() );

        return permissionDto1.build();
    }
}
