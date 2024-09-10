package com.example.swedbankApi.user.mapper;

import com.example.swedbankApi.TestUtils;
import com.example.swedbankApi.user.dto.UserDto;
import com.example.swedbankApi.user.entity.RoleEntity;
import com.example.swedbankApi.user.entity.UserEntity;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final RoleMapper roleMapper = new RoleMapperImpl();
    private final UserMapper mapper = new UserMapperImpl(roleMapper);

    @Test
    void givenUserEntity_whenMappingToUserDto_thenReturnUserDto() {
        final UserEntity user = TestUtils.createUserEntity();
        final UserDto actual = mapper.toDto(user);

        assertAll(
                () -> assertEquals(user.getName(), actual.getName()),
                () -> assertEquals(user.getId(), actual.getId()),
                () -> assertEquals(user.getNickName(), actual.getNickName()),
                () -> assertEquals(user.getEmail(), actual.getEmail()),
                () -> assertEquals(user.getLastName(), actual.getLastName()),
                () -> assertTrue(actual.isActive()),
                () -> assertNotNull(actual.getRoles()),
                () -> assertEquals(user.getRoles().stream()
                        .map(RoleEntity::getName)
                        .collect(Collectors.toSet()), actual.getRoles()) // Assert roles
        );
    }

    @Test
    void givenUserDto_whenMappingToUserEntity_thenReturnUserEntity() {
        final UserDto userDto = TestUtils.craeateUserDto();
        final UserEntity actual = mapper.toEntity(userDto);

        assertAll(
                () -> assertEquals(userDto.getName(), actual.getName()),
                () -> assertEquals(userDto.getId(), actual.getId()),
                () -> assertEquals(userDto.getNickName(), actual.getNickName()),
                () -> assertEquals(userDto.getEmail(), actual.getEmail()),
                () -> assertEquals(userDto.getLastName(), actual.getLastName()),
                () -> assertTrue(actual.isActive()),
                ()-> assertNull(actual.getRoles())
        );
    }

}
