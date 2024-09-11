package com.example.swedbankApi.user.service;

import com.example.swedbankApi.user.dto.UserDto;
import com.example.swedbankApi.user.entity.RoleEntity;
import com.example.swedbankApi.user.entity.UserEntity;
import com.example.swedbankApi.user.mapper.UserMapper;
import com.example.swedbankApi.user.repo.RoleRepo;
import com.example.swedbankApi.user.repo.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepo userRepo;
    private final RoleService roleService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<RoleEntity> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
    }

    @Override
    public UserDto login(String emailOrNickName, String password) {
        UserEntity user = userRepo.findByUsername(emailOrNickName)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        user.setActive(true);

        return userMapper.toDto(user);
    }

    public UserDto createUser(final UserDto userDto) throws EntityNotFoundException {
        checkIfUserExist(userDto);
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        UserEntity user = userMapper.toEntity(userDto);
        user.setPassword(encodedPassword);
        user.setActive(true);

        Optional<RoleEntity> role = roleRepo.findById(1L);
        if (role.isEmpty()) {
            throw new NoSuchElementException("Role not found.");
        }
        user.setRoles(Set.of(role.get()));

        UserEntity savedUser = userRepo.save(user);
        return userMapper.toDto(savedUser);
    }

    public List<UserDto> getAllUsers() {
        return userRepo.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserDto updateUser(final Long userId, final UserDto userDto) {
        final UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        userRepo.save(user);
        return userMapper.toDto(user);
    }

    public void deleteUser(Long id) {
        if (!userRepo.existsById(id)) {
            throw new EntityNotFoundException("User not found");
        }
        userRepo.deleteById(id);
    }

    public UserDto getUserById(Long id) {
        final UserEntity user = userRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        return userMapper.toDto(user);
    }

    @Override
    public UserDto getUserByUsername(String username) {
        final UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException(""));
        return userMapper.toDto(user);
    }

    public void deactivateUser(Long id) {
        UserEntity user = userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setActive(false);
        userRepo.save(user);
    }

    private void checkIfUserExist(UserDto userDto) {
        Optional<UserEntity> existingUserOpt = userRepo.findByUsername(userDto.getUsername());

        if (existingUserOpt.isPresent()) {
            UserEntity existingUser = existingUserOpt.get();

            if (existingUser.isActive()) {
                throw new IllegalArgumentException("User with this username already exists");
            } else {
                // If the user is inactive, proceed to create a new user and ensure old data is not shown
                existingUser.setActive(false);
                userRepo.save(existingUser);
            }
        }
    }
}

