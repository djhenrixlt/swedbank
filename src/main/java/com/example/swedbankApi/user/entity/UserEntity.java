package com.example.swedbankApi.user.entity;

import jakarta.persistence.*;
import lombok.*;


@Data
@NoArgsConstructor
@Entity
@Table(name="users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;
   private String name;
   private String lastName;
   private String nickName;
   private String password;
   private String email;
   private boolean admin;
   private boolean active;
}
