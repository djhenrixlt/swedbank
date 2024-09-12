package com.example.swedbankApi.user.controller;

import com.example.swedbankApi.user.dto.AuthResponseDto;
import com.example.swedbankApi.user.dto.LoginDto;
import com.example.swedbankApi.user.dto.UserDto;
import com.example.swedbankApi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RequiredArgsConstructor
@org.springframework.web.bind.annotation.RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1.0")
public class RestController {


    private final UserService userService;


    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto) {

        //Receive the token from AuthService
        String token = userService.login(loginDto);

        //Set the token as a response using JwtAuthResponse Dto class
        AuthResponseDto authResponseDto = new AuthResponseDto();
        authResponseDto.setAccessToken(token);

        //Return the response to the user
        return new ResponseEntity<>(authResponseDto, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/users/self")
    public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
        UserDto userDto = userService.getUserByUsername(principal.getName());
        return ResponseEntity.ok(userDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #principal.name == @userService.getUserById(#id).getEmail()")
    @GetMapping("/users/{id}")
    public UserDto finUserById(@PathVariable long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/users/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        return userService.updateUser(id, userDto);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/users/self")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserDto updateUserProfile(@RequestBody UserDto userDto, Principal principal) {
        UserDto user = userService.getUserByUsername(principal.getName());
        return userService.updateUser(user.getId(), userDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
    }


    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateUser(@PathVariable long id) {
        userService.deactivateUser(id);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/users/self/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateCurrentUser(Principal principal) {
        UserDto user = userService.getUserByUsername(principal.getName());
        userService.deactivateUser(user.getId());
    }
}
