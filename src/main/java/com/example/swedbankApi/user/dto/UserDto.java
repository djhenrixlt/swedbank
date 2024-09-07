package com.example.swedbankApi.user.dto;

import lombok.*;

@Builder
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
    boolean admin;
    boolean active;
}
