package com.example.swedbankApi.user.dto;

import lombok.*;

import java.util.Set;

@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

   private long id;
   private String name;
   private String lastName;
   private String username;
   private String password;
   private String email;
   private boolean active;
   private Set<String> roles;
}
