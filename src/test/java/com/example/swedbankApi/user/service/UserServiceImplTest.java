package com.example.swedbankApi.user.service;

import com.example.swedbankApi.TestUtils;
import com.example.swedbankApi.user.dto.UserDto;
import com.example.swedbankApi.user.entity.UserEntity;
import com.example.swedbankApi.user.mapper.UserMapper;
import com.example.swedbankApi.user.repo.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@Sql(scripts = {"classpath:sql/user_data.sql"})
class UserServiceImplTest {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserMapper userMapper;

    @Test
    void findAll() {
        List<UserDto> userDtoList = userService.getAllUsers();

        assertAll(
                () -> assertEquals(1, userDtoList.size()),
                () -> {
                    UserDto userDto = userDtoList.get(0);
                    assertEquals("John", userDto.getName());
                    assertEquals("Doe", userDto.getLastName());
                    assertEquals("johndoe", userDto.getNickName());
                    assertEquals("password123", userDto.getPassword());
                    assertEquals("john.doe@example.com", userDto.getEmail());
                    assertEquals(false, userDto.isAdmin());
                    assertEquals(true, userDto.isActive());
                }
        );
    }

    @Test
    void testCreateUser()  {
        UserDto inputUserDto = TestUtils.craateUserDto();
        UserEntity expectedUserEntity = TestUtils.craateUserEntity();


        UserDto createdUserDto = userService.createUser(inputUserDto);

        UserEntity savedUserEntity = userRepo.findById(createdUserDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        assertAll(
                () -> assertEquals(expectedUserEntity.getName(), savedUserEntity.getName() ),
                () -> assertEquals(expectedUserEntity.getLastName(), savedUserEntity.getLastName() ),
                () -> assertEquals(expectedUserEntity.getEmail(), savedUserEntity.getEmail()),
                () -> assertEquals(expectedUserEntity.isAdmin(), savedUserEntity.isAdmin()),
                () -> assertEquals(expectedUserEntity.isActive(), savedUserEntity.isActive()),
                () -> assertEquals(expectedUserEntity.getNickName(), savedUserEntity.getNickName()),
                () -> assertEquals(expectedUserEntity.getPassword(), savedUserEntity.getPassword())
        );
    }

    @Test
    void testUpdateUser() {
        UserEntity existingUserEntity = TestUtils.craateUserEntity();
        existingUserEntity.setId(1L);
        userRepo.save(existingUserEntity);

        UserDto updatedUserDto = UserDto.builder()
                .id(1L)
                .name("name1")
                .lastName("lastName1")
                .nickName("username1")
                .email("email1")
                .password("password")
                .admin(true)
                .active(true)
                .build();

        UserDto resultUserDto = userService.updateUser(1L, updatedUserDto);
        UserEntity updatedUserEntity = userRepo.findById(1L)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        assertAll(
                () -> assertEquals(updatedUserDto.getName(), updatedUserEntity.getName()),
                () -> assertEquals(updatedUserDto.getLastName(), updatedUserEntity.getLastName()),
                () -> assertEquals(updatedUserDto.getNickName(), updatedUserEntity.getNickName()),
                () -> assertEquals(updatedUserDto.getPassword(), updatedUserEntity.getPassword()),
                () -> assertEquals(updatedUserDto.getEmail(), updatedUserEntity.getEmail()),
                () -> assertEquals(updatedUserDto.isAdmin(), updatedUserEntity.isAdmin()),
                () -> assertEquals(updatedUserDto.isActive(), updatedUserEntity.isActive())
        );

        assertAll(
                () -> assertEquals(updatedUserDto.getId(), resultUserDto.getId()),
                () -> assertEquals(updatedUserDto.getName(), resultUserDto.getName()),
                () -> assertEquals(updatedUserDto.getLastName(), resultUserDto.getLastName()),
                () -> assertEquals(updatedUserDto.getNickName(), resultUserDto.getNickName()),
                () -> assertEquals(updatedUserDto.getPassword(), resultUserDto.getPassword()),
                () -> assertEquals(updatedUserDto.getEmail(), resultUserDto.getEmail()),
                () -> assertEquals(updatedUserDto.isAdmin(), resultUserDto.isAdmin()),
                () -> assertEquals(updatedUserDto.isActive(), resultUserDto.isActive())
        );
    }

    @Test
    void testDeleteUser() {
        UserEntity existingUserEntity = TestUtils.craateUserEntity();
        existingUserEntity.setId(1L);
        userRepo.save(existingUserEntity);
        userService.deleteUser(1L);

        assertFalse(userRepo.existsById(1L));
    }

    @Test
    void testGetUserById() {
        UserEntity existingUserEntity = TestUtils.craateUserEntity();
        existingUserEntity.setId(1L);
        userRepo.save(existingUserEntity);
        UserDto resultUserDto = userService.getUserById(1L);

        assertAll(
                () -> assertEquals(existingUserEntity.getId(), resultUserDto.getId()),
                () -> assertEquals(existingUserEntity.getName(), resultUserDto.getName()),
                () -> assertEquals(existingUserEntity.getLastName(), resultUserDto.getLastName()),
                () -> assertEquals(existingUserEntity.getNickName(), resultUserDto.getNickName()),
                () -> assertEquals(existingUserEntity.getPassword(), resultUserDto.getPassword()),
                () -> assertEquals(existingUserEntity.getEmail(), resultUserDto.getEmail()),
                () -> assertEquals(existingUserEntity.isAdmin(), resultUserDto.isAdmin()),
                () -> assertEquals(existingUserEntity.isActive(), resultUserDto.isActive())
        );
    }

    @Test
    void testGetUserByUsername() {
        UserEntity existingUserEntity = TestUtils.craateUserEntity();
        existingUserEntity.setNickName("username1");
        userRepo.save(existingUserEntity);
        UserDto resultUserDto = userService.getUserByUsername("username1");

        assertAll(
                () -> assertEquals(existingUserEntity.getId(), resultUserDto.getId()),
                () -> assertEquals(existingUserEntity.getName(), resultUserDto.getName()),
                () -> assertEquals(existingUserEntity.getLastName(), resultUserDto.getLastName()),
                () -> assertEquals(existingUserEntity.getNickName(), resultUserDto.getNickName()),
                () -> assertEquals(existingUserEntity.getPassword(), resultUserDto.getPassword()),
                () -> assertEquals(existingUserEntity.getEmail(), resultUserDto.getEmail()),
                () -> assertEquals(existingUserEntity.isAdmin(), resultUserDto.isAdmin()),
                () -> assertEquals(existingUserEntity.isActive(), resultUserDto.isActive())
        );
    }

}

