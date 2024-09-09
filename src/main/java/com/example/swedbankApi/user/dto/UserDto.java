package com.example.swedbankApi.user.dto;

import lombok.*;

import java.util.Set;

@Builder(toBuilder = true)
@AllArgsConstructor
@Getter
@Setter
@Value
public class UserDto {

    long id;
    String name;
    String lastName;
    String nickName;
    String password;
    String email;
    boolean active;
    Set<String> roles;
}
