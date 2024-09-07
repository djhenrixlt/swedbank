package com.example.swedbankApi.user.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserEntity {

    private long id;
    private String name;
    private String lastName;
    private String nickName;
    private String password;
    private String email;
    private boolean isAdmin = true;
    private boolean isActive = true;
}
