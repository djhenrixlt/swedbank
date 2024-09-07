package com.example.swedbankApi.user.service;

import com.example.swedbankApi.user.dto.UserDto;
import com.example.swedbankApi.user.repo.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

 private final UserRepo userRepo;

    @Override
    public UserDto login(String username, String password) {

        return  userRepo
                .findByUserName(username)
                .orElseThrow(()-> new NoSuchElementException("User not found"));
    }

    public UserDto createUser(final UserDto userDto) {
     return userRepo.save(userDto);
 }


 public List<UserDto> getAllUsers() {return userRepo.findAll();}


 public UserDto updateUser(final Long userId, final UserDto userDto) {
     final UserDto user = userRepo.findById(userId).orElseThrow(()-> new NoSuchElementException("User not found"));
     return userRepo.save(userDto);
 }

 public void deleteUser(Long id) {
     if (!userRepo.existsById(id)) {
         throw new EntityNotFoundException("User not found");
     }
     userRepo.deleteById(id);
 }


 public UserDto getUserById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(()-> new NoSuchElementException("User not found"));}

    @Override
    public UserDto getUserByUsername(String username) {
     Optional<UserDto> user = userRepo.findByUserName(username);
        return user.get();
    }

}
