package com.example.swedbankApi.user.service;


import com.example.swedbankApi.user.dto.LoginDto;
import com.example.swedbankApi.user.dto.UserDto;

import java.util.List;
public interface UserService {

    String login(LoginDto loginDto);
    //    UserDto login(String username, String password);
    UserDto createUser(UserDto userDto);
    UserDto updateUser(Long userId, UserDto userDto);
    UserDto getUserById(Long id);
    UserDto getUserByUsername(String username);
    void deleteUser(Long id);
    List<UserDto> getAllUsers();
    void deactivateUser(Long id);
}
