package com.example.swedbankApi.user.dto;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserDto {

    private long id;
    private String name;
    private String lastName;
    private String nickName;
    private String password;
    private String email;
    private boolean isAdmin = true;
    private boolean isActive = true;
}
