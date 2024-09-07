package com.example.swedbankApi.user.service;

import com.example.swedbankApi.user.dto.UserDto;
import com.example.swedbankApi.user.repo.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService {

 private UserRepo userRepo;

 public UserDto createUser(UserDto userDto) {
     return userRepo.save(userDto);
 }

 public List<UserDto> getAllUsers() {return userRepo.findAll();}


 public UserDto updateUser(UserDto userDto) {
     Long id = userDto.getId();
     if (id == null) {
         throw new EntityNotFoundException("User not found");
     }
     return userRepo.save(userDto);
 }

 public void deleteUser(Long id) {
     if (!userRepo.existsById(id)) {
         throw new EntityNotFoundException("User not found");
     }
     userRepo.deleteById(id);
 }


 public UserDto getUserById(Long id) {return userRepo.findById(id).get();}

}
