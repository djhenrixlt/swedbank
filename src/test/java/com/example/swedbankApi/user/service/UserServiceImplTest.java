package com.example.swedbankApi.user.service;

import com.example.swedbankApi.TestUtils;
import com.example.swedbankApi.user.dto.UserDto;
import com.example.swedbankApi.user.entity.RoleEntity;
import com.example.swedbankApi.user.entity.UserEntity;
import com.example.swedbankApi.user.mapper.UserMapper;
import com.example.swedbankApi.user.repo.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void getAllUsers_ReturnsUserList() {
        List<UserDto> userDtoList = userService.getAllUsers();

        assertAll(
                () -> assertEquals(1, userDtoList.size()),
                () -> {
                    UserDto userDto = userDtoList.get(0);
                    assertEquals("John", userDto.getName());
                    assertEquals("Doe", userDto.getLastName());
                    assertEquals("johndoe", userDto.getUsername());
                    assertEquals("john.doe@example.com", userDto.getEmail());
                    assertTrue(userDto.isActive());
                    assertEquals(Set.of("ROLE_USER"), userDto.getRoles());
                }
        );
    }

    @Test
    void createUser_ValidInput_ReturnsUserDto() {
        UserDto inputUserDto = TestUtils.craeateUserDto();


        UserDto createdUserDto = userService.createUser(inputUserDto);

        UserEntity savedUserEntity = userRepo.findById(createdUserDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        assertAll(
                () -> assertEquals(inputUserDto.getName(), savedUserEntity.getName()),
                () -> assertEquals(inputUserDto.getLastName(), savedUserEntity.getLastName()),
                () -> assertEquals(inputUserDto.getEmail(), savedUserEntity.getEmail()),
                () -> assertEquals(inputUserDto.isActive(), savedUserEntity.isActive()),
                () -> assertEquals(inputUserDto.getUsername(), savedUserEntity.getUsername()),
                () -> assertEquals(Set.of("ROLE_USER"),
                        savedUserEntity.getRoles().stream()
                                .map(RoleEntity::getName)
                                .collect(Collectors.toSet()))
        );
    }

    @Test
    void updateUser_ValidInput_ReturnsUpdatedUserDto() {
        final UserEntity existingUserEntity = TestUtils.createUserEntity();
        final UserEntity savedUserEntity = userRepo.save(existingUserEntity);

        UserDto updatedUserDto = UserDto.builder()
                .id(savedUserEntity.getId())
                .name("name1")
                .lastName("lastName1")
                .username("username1")
                .email("email1")
                .active(true)
                .roles(Set.of("ROLE_USER"))
                .build();

        UserDto resultUserDto = userService.updateUser(savedUserEntity.getId(), updatedUserDto);
        UserEntity updatedUserEntity = userRepo.findById(savedUserEntity.getId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        assertAll(
                () -> assertEquals(updatedUserDto.getName(), updatedUserEntity.getName()),
                () -> assertEquals(updatedUserDto.getLastName(), updatedUserEntity.getLastName()),
                () -> assertEquals(updatedUserDto.getUsername(), updatedUserEntity.getUsername()),
                () -> assertEquals(updatedUserDto.getEmail(), updatedUserEntity.getEmail()),
                () -> assertEquals(updatedUserDto.isActive(), updatedUserEntity.isActive()),
                () -> assertEquals(Set.of("ROLE_USER"),
                        updatedUserEntity.getRoles().stream()
                                .map(RoleEntity::getName)
                                .collect(Collectors.toSet()))
        );

        assertAll(
                () -> assertEquals(updatedUserDto.getId(), resultUserDto.getId()),
                () -> assertEquals(updatedUserDto.getName(), resultUserDto.getName()),
                () -> assertEquals(updatedUserDto.getLastName(), resultUserDto.getLastName()),
                () -> assertEquals(updatedUserDto.getUsername(), resultUserDto.getUsername()),
                () -> assertEquals(updatedUserDto.getEmail(), resultUserDto.getEmail()),
                () -> assertEquals(updatedUserDto.isActive(), resultUserDto.isActive()),
                () -> assertEquals(updatedUserDto.getRoles(), resultUserDto.getRoles())
        );
    }

    @Test
    void deleteUser_ExistingUser_UserDeleted() {
        final UserEntity existingUserEntity = TestUtils.createUserEntity();
        final UserEntity savedUserEntity = userRepo.save(existingUserEntity);
        userService.deleteUser(savedUserEntity.getId());

        assertFalse(userRepo.existsById(savedUserEntity.getId()));
    }

    @Test
    void getUserById_ExistingUser_ReturnsUserDto() {
        UserEntity existingUserEntity = TestUtils.createUserEntity();
        existingUserEntity.setId(2L);
        userRepo.save(existingUserEntity);
        UserDto resultUserDto = userService.getUserById(2L);

        assertAll(
                () -> assertEquals(existingUserEntity.getId(), resultUserDto.getId()),
                () -> assertEquals(existingUserEntity.getName(), resultUserDto.getName()),
                () -> assertEquals(existingUserEntity.getLastName(), resultUserDto.getLastName()),
                () -> assertEquals(existingUserEntity.getUsername(), resultUserDto.getUsername()),
                () -> assertEquals(existingUserEntity.getEmail(), resultUserDto.getEmail()),
                () -> assertEquals(existingUserEntity.isActive(), resultUserDto.isActive()),
                () -> assertEquals(Set.of("ROLE_USER"), resultUserDto.getRoles())
        );
    }

    @Test
    void getUserByUsername_ExistingUser_ReturnsUserDto() {
        UserEntity existingUserEntity = TestUtils.createUserEntity();
        existingUserEntity.setUsername("username1");
        final UserEntity savedUserEntity = userRepo.save(existingUserEntity);
        UserDto resultUserDto = userService.getUserByUsername("username1");

        assertAll(
                () -> assertEquals(savedUserEntity.getId(), resultUserDto.getId()),
                () -> assertEquals(savedUserEntity.getName(), resultUserDto.getName()),
                () -> assertEquals(savedUserEntity.getLastName(), resultUserDto.getLastName()),
                () -> assertEquals(savedUserEntity.getUsername(), resultUserDto.getUsername()),
                () -> assertEquals(savedUserEntity.getEmail(), resultUserDto.getEmail()),
                () -> assertEquals(savedUserEntity.isActive(), resultUserDto.isActive()),
                () -> assertEquals(Set.of("ROLE_USER"), resultUserDto.getRoles())
        );
    }

    @Test
    void login_SuccessfulLogin_ReturnsUserDto() {
        final UserEntity existingUserEntity = TestUtils.createUserEntity();
        existingUserEntity.setUsername("username1");
        existingUserEntity.setPassword(passwordEncoder.encode("password"));
        final UserEntity savedUserEntity = userRepo.save(existingUserEntity);

        final UserDto resultUserDto = userService.login("username1", "password");

        assertAll(
                () -> assertEquals(savedUserEntity.getId(), resultUserDto.getId()),
                () -> assertEquals(savedUserEntity.getName(), resultUserDto.getName()),
                () -> assertEquals(savedUserEntity.getLastName(), resultUserDto.getLastName()),
                () -> assertEquals(savedUserEntity.getUsername(), resultUserDto.getUsername()),
                () -> assertEquals(savedUserEntity.getEmail(), resultUserDto.getEmail()),
                () -> assertEquals(savedUserEntity.isActive(), resultUserDto.isActive()),
                () -> assertEquals(Set.of("ROLE_USER"), resultUserDto.getRoles())
        );
    }

    @Test
    void login_InvalidPassword_ThrowsException() {
        UserEntity existingUserEntity = TestUtils.createUserEntity();
        existingUserEntity.setUsername("username1");
        existingUserEntity.setPassword(passwordEncoder.encode("password"));
        userRepo.save(existingUserEntity);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.login("username1", "wrongPassword");
        });

        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    void checkIfUserExist_ExistingActiveUser_ThrowsException() {
        UserDto inputUserDto = TestUtils.craeateUserDto();
        UserEntity existingUser = new UserEntity();
        existingUser.setUsername(inputUserDto.getUsername());
        existingUser.setActive(true);
        userRepo.save(existingUser);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(inputUserDto);
        });
        assertEquals("User with this username already exists", exception.getMessage());
    }

    @Test
    void checkIfUserExist_ExistingInactiveUser_UpdatesUserStatus() {
        UserDto inputUserDto = TestUtils.craeateUserDto();
        UserEntity existingUser = new UserEntity();
        existingUser.setUsername(inputUserDto.getUsername());
        existingUser.setActive(false);
        userRepo.save(existingUser);

        UserDto createdUserDto = userService.createUser(inputUserDto);

        UserEntity updatedUser = userRepo.findById(createdUserDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        assertTrue(updatedUser.isActive());
    }

    @Test
    void deactivateUser_UserExists_DeactivatesUser() {
        final UserEntity existingUser = new UserEntity();
        existingUser.setActive(true);
        final UserEntity updatedUser = userRepo.save(existingUser);

        userService.deactivateUser(updatedUser.getId());

        UserEntity deletedUser = userRepo.findById(updatedUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        assertFalse(deletedUser.isActive());
    }

    @Test
    void deactivateUser_UserDoesNotExist_ThrowsException() {
        Long userId = 2L;

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.deactivateUser(userId);
        });
        assertEquals("User not found", exception.getMessage());
    }

}
