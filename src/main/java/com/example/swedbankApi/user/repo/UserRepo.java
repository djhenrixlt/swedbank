package com.example.swedbankApi.user.repo;

import com.example.swedbankApi.user.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserDto, Long> {


    Optional<UserDto> findByUserName(String username);

}
