package com.example.swedbankApi.user.Controller;

import com.example.swedbankApi.user.dto.UserDto;
import com.example.swedbankApi.user.repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
public class userController {

    UserRepo userRepo;

    @PostMapping
    UserDto login(String username,String email, String password) {
        Optional<UserDto> user = userRepo.findByUserName(username);
        return user.get();

    }

    @PostMapping("/api/signup")
    public ResponseEntity<UserDto> addUser(@RequestBody  UserDto userDto) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userRepo.save(userDto));
    }

    @GetMapping("/api/users")
    public List<UserDto> getAllUsers() {
        return userRepo.findAll();
    }

    @GetMapping("/api/users/{id}")
    public UserDto finUserById(@PathVariable long id) {
        return userRepo.getReferenceById(id);
    }


    @PostMapping("/api/users/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserDto updateUser(@RequestBody  UserDto userDto) throws Exception {
        return userRepo.save(userDto);
    }

    @DeleteMapping("/api/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable long id) {
        Optional<UserDto> user = userRepo.findById(id);
        userRepo.delete(user.get());
    }

}
