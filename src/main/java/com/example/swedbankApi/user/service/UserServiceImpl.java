package com.example.swedbankApi.user.service;

import com.example.swedbankApi.user.dto.UserDto;
import com.example.swedbankApi.user.entity.RoleEntity;
import com.example.swedbankApi.user.entity.UserEntity;
import com.example.swedbankApi.user.mapper.UserMapper;
import com.example.swedbankApi.user.repo.RoleRepo;
import com.example.swedbankApi.user.repo.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final RoleService roleService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;


    @Override
    public UserDto login(String emailOrNickName, String password) {
        UserEntity user = userRepo.findByNickName(emailOrNickName)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        user.setActive(true);

        return userMapper.toDto(user);
    }

    public UserDto createUser(final UserDto userDto) throws EntityNotFoundException {
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        UserEntity user = userMapper.toEntity(userDto);
        user.setPassword(encodedPassword);
        user.setActive(true);
        Optional<RoleEntity> role = roleRepo.findById(1L);
        if (role.isEmpty()){
            throw new NoSuchElementException("");
        }
        user.setRoles(Set.of(role.get()));

        UserEntity savedUser = userRepo.save(user);
        return userMapper.toDto(savedUser);

    }

    public void saveUser(UserDto userDto) throws EntityNotFoundException {
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());

        UserEntity user = userMapper.toEntity(userDto);
        user.setPassword(encodedPassword);
        user.setActive(true);
        Optional<RoleEntity> role = roleRepo.findById(1L);
        if (role.isEmpty()){
            throw new NoSuchElementException("");
        }
        user.setRoles(Set.of(role.get()));

        userRepo.save(user);
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
        final UserEntity user = userRepo.findByNickName(username)
                .orElseThrow(() -> new NoSuchElementException(""));
        return userMapper.toDto(user);
    }


}
