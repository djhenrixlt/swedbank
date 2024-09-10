package com.example.swedbankApi.user.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(exclude = "users")
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class RoleEntity {

    @Id
    private Long id;

    @Column(unique = true)
    private String name;

}
