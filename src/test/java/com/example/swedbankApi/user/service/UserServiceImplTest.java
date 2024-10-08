package com.example.swedbankApi.user.service;

import com.example.swedbankApi.TestUtils;
import com.example.swedbankApi.user.config.JwtTokenProvider;
import com.example.swedbankApi.user.dto.LoginDto;
import com.example.swedbankApi.user.dto.UserDto;
import com.example.swedbankApi.user.entity.RoleEntity;
import com.example.swedbankApi.user.entity.UserEntity;
import com.example.swedbankApi.user.mapper.UserMapper;
import com.example.swedbankApi.user.repo.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

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
        UserDto inputUserDto = TestUtils.createUserDto();

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
        UserEntity existingUserEntity = TestUtils.createUserEntity();
        existingUserEntity.setId(1L);
        userRepo.save(existingUserEntity);

        UserDto updatedUserDto = UserDto.builder()
                .id(1L)
                .name("name1")
                .lastName("lastName1")
                .username("username1")
                .email("email1")
                .active(true)
                .roles(Set.of("ROLE_USER"))
                .build();

        UserDto resultUserDto = userService.updateUser(1L, updatedUserDto);
        UserEntity updatedUserEntity = userRepo.findById(1L)
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
        UserEntity existingUserEntity = TestUtils.createUserEntity();
        existingUserEntity.setId(1L);

        userRepo.save(existingUserEntity);
        userService.deleteUser(1L);

        assertFalse(userRepo.existsById(1L));
    }

    @Test
    void getUserById_ExistingUser_ReturnsUserDto() {
        UserEntity existingUserEntity = TestUtils.createUserEntity();
        existingUserEntity.setId(1L);
        userRepo.save(existingUserEntity);
        UserDto resultUserDto = userService.getUserById(1L);

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
        userRepo.save(existingUserEntity);
        UserDto resultUserDto = userService.getUserByUsername("username1");

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
    void login_SuccessfulLogin_ReturnsJwtToken() {

        UserEntity existingUserEntity = TestUtils.createUserEntity();
        existingUserEntity.setUsername("username1");
        existingUserEntity.setPassword(passwordEncoder.encode("password"));
        userRepo.save(existingUserEntity);

        LoginDto loginDto = new LoginDto();
        loginDto.setLogin("username1");
        loginDto.setPassword("password");

        String token = userService.login(loginDto);

        // Assert: Check that the token is not null or empty
        assertNotNull(token);
        assertFalse(token.isEmpty());

        // Uncomment if using actual JWT validation logic
        // Claims claims = jwtTokenProvider.getClaimsFromToken(token);
        // assertEquals("username1", claims.getSubject());
    }

    @Test
    void login_InvalidPassword_ThrowsAuthenticationException() {
        UserEntity existingUserEntity = TestUtils.createUserEntity();
        existingUserEntity.setUsername("username1");
        existingUserEntity.setPassword(passwordEncoder.encode("password"));
        userRepo.save(existingUserEntity);

        LoginDto loginDto = new LoginDto();
        loginDto.setLogin("username1");
        loginDto.setPassword("wrongPassword");

        // Act & Assert: Verify that the method throws an AuthenticationException
        assertThrows(AuthenticationException.class, () -> userService.login(loginDto));
    }

    @Test
    void checkIfUserExist_ExistingActiveUser_ThrowsException() {
        UserDto inputUserDto = TestUtils.createUserDto();
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
        UserDto inputUserDto = TestUtils.createUserDto(); // Creates a new user DTO
        UserEntity existingUser = new UserEntity();
        existingUser.setUsername(inputUserDto.getUsername());
        existingUser.setActive(false);

        // Save the existing user without manually setting the ID
        when(userRepo.save(existingUser)).thenReturn(existingUser);

        UserDto createdUserDto = userService.createUser(inputUserDto);

        UserEntity updatedUser = userRepo.findById(createdUserDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        assertTrue(updatedUser.isActive(), "User should be activated");
    }

    @Test
    void deactivateUser_UserExists_DeactivatesUser() {
        Long userId = 1L;
        UserEntity existingUser = new UserEntity();
        existingUser.setId(userId);
        existingUser.setActive(true);
        userRepo.save(existingUser);

        userService.deactivateUser(userId);

        UserEntity updatedUser = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        assertFalse(updatedUser.isActive());
    }

    @Test
    void deactivateUser_UserDoesNotExist_ThrowsException() {
        Long userId = 1L;
        when(userRepo.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.deactivateUser(userId);
        });
        assertEquals("User not found", exception.getMessage());
    }

}

