package com.example.swedbankApi.user.controller;

import com.example.swedbankApi.user.dto.UserDto;
import com.example.swedbankApi.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    @GetMapping("/index")
    public String home(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result,
                               Model model){
        if(result.hasErrors()){
            model.addAttribute("user", userDto);
            return "/register";
        }
        userService.createUser(userDto);
        return "redirect:/register?success";

    }

    @GetMapping("/users")
    public String users(Model model){
        List<UserDto> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users";
    }
}
