package com.example.swedbankApi.user.Controller;

import com.example.swedbankApi.user.dto.UserDto;
import com.example.swedbankApi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1.0")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login( String username, String password) {
           UserDto userDto = userService.login(username, password);
           return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> addUser(@RequestBody  UserDto userDto) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(userDto));
    }

    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public UserDto finUserById(@PathVariable long id) {
        return userService.getUserById(id);
    }


    @PostMapping("/users/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserDto updateUser( @PathVariable Long id, @RequestBody  UserDto userDto) throws Exception {
        return userService.updateUser(id, userDto);
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
    }

}
