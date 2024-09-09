package com.example.swedbankApi.user.repo;

import com.example.swedbankApi.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity, Long> {


    Optional<UserEntity> findByNickName(String username);

    UserEntity findByEmail(String email);

}
