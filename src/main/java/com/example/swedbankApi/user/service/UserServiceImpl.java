package com.example.swedbankApi.user.service;

import com.example.swedbankApi.user.dto.UserDto;
import com.example.swedbankApi.user.entity.UserEntity;
import com.example.swedbankApi.user.mapper.UserMapper;
import com.example.swedbankApi.user.repo.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

 private final UserRepo userRepo;
 private final UserMapper userMapper;

    @Override
    public UserDto login(String username, String password) {

        return  userRepo
                .findByUserName(username)
                .orElseThrow(()-> new NoSuchElementException("User not found"));
    }

    public UserDto createUser(final UserDto userDto)throws EntityNotFoundException {
        UserEntity user = UserMapper.userMapper.userDtoToUserEntity(userDto);
        userRepo.save(user);
     return UserMapper.userMapper.userToUserDto(user);
 }


 public List<UserDto> getAllUsers() {
        return userRepo.findAll()
                .stream()
                .map(user -> userMapper.userToUserDto(user))
                .toList();
    }


 public UserDto updateUser(final Long userId, final UserDto userDto) {
     final UserEntity user = userRepo.findById(userId).orElseThrow(()-> new NoSuchElementException("User not found"));
     userRepo.save(user);
     return UserMapper.userMapper.userToUserDto(user);
 }

 public void deleteUser(Long id) {
     if (!userRepo.existsById(id)) {
         throw new EntityNotFoundException("User not found");
     }
     userRepo.deleteById(id);
 }


 public UserDto getUserById(Long id) {
     UserEntity user = userRepo.findById(id)
             .orElseThrow(()-> new NoSuchElementException("User not found"));
        return UserMapper.userMapper.userToUserDto(user);
    }

    @Override
    public UserDto getUserByUsername(String username) {
     Optional<UserDto> user = userRepo.findByUserName(username);
        return user.get();
    }

}
