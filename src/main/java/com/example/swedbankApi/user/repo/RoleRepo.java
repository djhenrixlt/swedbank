package com.example.swedbankApi.user.repo;

import com.example.swedbankApi.user.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByName(String name);

}
