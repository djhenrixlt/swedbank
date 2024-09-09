package com.example.swedbankApi.user.controller;

import com.example.swedbankApi.TestUtils;
import com.example.swedbankApi.user.dto.UserDto;
import com.example.swedbankApi.user.entity.UserEntity;
import com.example.swedbankApi.user.mapper.UserMapper;
import com.example.swedbankApi.user.repo.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@Sql(scripts = {"classpath:sql/user_data.sql"})
@ActiveProfiles("test")
@AutoConfigureMockMvc
class RestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private WebApplicationContext context;

    private UserDto userDto;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        userDto = TestUtils.craeateUserDto();
        userEntity = TestUtils.createUserEntity();
    }

    @Test
    void givenUserCredentials_whenLogin_thenReturnUserDto() throws Exception {
        mockMvc.perform(post("/api/v1.0/login")
                        .param("username", "johndoe")
                        .param("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void givenUserDto_whenSignup_thenReturnCreatedUser() throws Exception {
        mockMvc.perform(post("/api/v1.0/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));
    }

    @Test
    void whenGetAllUsers_thenReturnUserList() throws Exception {
        mockMvc.perform(get("/api/v1.0/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John"));
    }

    @Test
    void givenUserId_whenFindUserById_thenReturnUserDto() throws Exception {
        mockMvc.perform(get("/api/v1.0/users/{id}", userEntity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void givenUpdatedUser_whenUpdateUser_thenReturnUpdatedUser() throws Exception {
        final UserEntity userSave = userRepo.save(userEntity);
        userSave.setName("Updated Name");
        final UserDto updatedUser = mapper.toDto(userSave);

        mockMvc.perform(put("/api/v1.0/users/{id}", userSave.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    void givenUserId_whenDeleteUser_thenNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1.0/users/{id}", 1))
                .andExpect(status().isNoContent());

        Optional<UserEntity> deletedUser = userRepo.findById(userEntity.getId());
        assertTrue(deletedUser.isEmpty());
    }
}
