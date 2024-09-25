package com.example.swedbankApi;

import com.example.swedbankApi.user.dto.UserDto;
import com.example.swedbankApi.user.entity.RoleEntity;
import com.example.swedbankApi.user.entity.UserEntity;
import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Set;


@UtilityClass
public class TestUtils {

    public UserDto createUserDto() {
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");

        return UserDto.builder()
                .name("name1")
                .lastName("lastName1")
                .username("username1")
                .email("email1")
                .id(1L)
                .password("password")
                .active(true)
                .roles(roles)
                .build();
    }

    public UserEntity createUserEntity() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("ROLE_USER");

        Set<RoleEntity> roles = new HashSet<>();
        roles.add(roleEntity);

        UserEntity userEntity = new UserEntity();
        userEntity.setName("name1");
        userEntity.setLastName("lastName1");
        userEntity.setUsername("username1");
        userEntity.setEmail("email1");
        userEntity.setId(1);
        userEntity.setPassword("password");
        userEntity.setActive(true);
        userEntity.setRoles(roles);
        return userEntity;
    }
}
