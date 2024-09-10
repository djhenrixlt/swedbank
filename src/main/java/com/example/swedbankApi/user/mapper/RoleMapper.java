package com.example.swedbankApi.user.mapper;

import com.example.swedbankApi.user.entity.RoleEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface RoleMapper {

    RoleEntity toEntity(String roles);

    default String toDTO(RoleEntity roleEntity) {
        return roleEntity.getName();
    }

    default Set<String> toDTO(Set<RoleEntity> roleEntities) {
        return roleEntities.stream()
                .map(this::toDTO)
                .collect(Collectors.toSet());
    }

    default Set<RoleEntity> toEntity(Set<String> roles) {
        return roles.stream()
                .map(this::toEntity)
                .collect(Collectors.toSet());
    }
}
