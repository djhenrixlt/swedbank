package com.example.swedbankApi.user.mapper;


import com.example.swedbankApi.user.dto.UserDto;
import com.example.swedbankApi.user.entity.RoleEntity;
import com.example.swedbankApi.user.entity.UserEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "roles", target = "roles", qualifiedByName = "stringToRoles")
    UserEntity toEntity(UserDto userDto);

    @Mapping(source = "roles", target = "roles", qualifiedByName = "rolesToStrings")
    UserDto toDto(UserEntity userEntity);

    @Named("stringToRoles")
    default Set<RoleEntity> stringToRoles(Set<String> roles) {
        return roles.stream()
                .map(role -> {
                    RoleEntity roleEntity = new RoleEntity();
                    roleEntity.setName(role);
                    return roleEntity;
                })
                .collect(Collectors.toSet());
    }

    @Named("rolesToStrings")
    default Set<String> rolesToStrings(Set<RoleEntity> roleEntities) {
        return roleEntities.stream()
                .map(RoleEntity::getName)
                .collect(Collectors.toSet());
    }
}
