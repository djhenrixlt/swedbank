package com.example.swedbankApi.user.service;

import com.example.swedbankApi.user.dto.UserDto;
import com.example.swedbankApi.user.mapper.UserMapper;
import com.example.swedbankApi.user.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void findAll(){
        List<UserDto> userDtoList =userService.getAllUsers();
        assertEquals(1, userDtoList.size());    }
}
