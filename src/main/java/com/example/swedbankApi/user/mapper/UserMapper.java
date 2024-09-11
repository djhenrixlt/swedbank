package com.example.swedbankApi.user.mapper;


import com.example.swedbankApi.user.dto.UserDto;
import com.example.swedbankApi.user.entity.UserEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {RoleMapper.class})
public interface UserMapper {


    @Mapping(target = "roles", ignore = true)
    UserEntity toEntity(UserDto userDto);

    @Mapping(target = "password", ignore = true)
    UserDto toDto(UserEntity userEntity);

}
