package com.example.swedbankApi.user.service;

import com.example.swedbankApi.user.config.JwtTokenProvider;
import com.example.swedbankApi.user.dto.LoginDto;
import com.example.swedbankApi.user.dto.UserDto;
import com.example.swedbankApi.user.entity.RoleEntity;
import com.example.swedbankApi.user.entity.UserEntity;
import com.example.swedbankApi.user.mapper.UserMapper;
import com.example.swedbankApi.user.repo.RoleRepo;
import com.example.swedbankApi.user.repo.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;


    @Override
    public String login(final LoginDto loginDto) {
        UserEntity user = userRepo.findByUsernameOrEmail(loginDto.getLogin(), loginDto.getLogin())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + loginDto.getLogin()));

        //AuthenticationManager is used to authenticate the user
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                loginDto.getPassword()
        ));
        //SecurityContextHolder is used to allows the rest of the application to know
        //that the user is authenticated and can use user data from Authentication object */
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //Generate the token based on username and secret key
        String token = jwtTokenProvider.generateToken(authentication);

        return token;
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

    @Override
    public void deactivateUserByEmail(String email) {
        UserEntity user = userRepo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setActive(false);
        userRepo.save(user);
    }

    public UserDto getUsersByEmail(String email) {
        UserEntity user = userRepo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userMapper.toDto(user);
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
