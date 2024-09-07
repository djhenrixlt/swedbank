package com.example.swedbankApi;

import com.example.swedbankApi.user.dto.UserDto;
import com.example.swedbankApi.user.entity.UserEntity;
import lombok.experimental.UtilityClass;


@UtilityClass
public class TestUtils {

    public UserDto craateUserDto(){
        return UserDto.builder()
                .name("name1")
                .lastName("lastName1")
                .nickName("username1")
                .email("email1")
                .id(1)
                .password("password")
                .admin(true)
                .active(true)
                .build();
    }

    public UserEntity craateUserEntity(){
         UserEntity userEntity = new UserEntity();
                userEntity.setName("name1");
                userEntity.setLastName("lastName1");
                userEntity.setNickName("username1");
                userEntity.setEmail("email1");
                userEntity.setId(1);
                userEntity.setPassword("password");
                userEntity.setAdmin(true);
                userEntity.setActive(true);
        return userEntity;
    }
}
