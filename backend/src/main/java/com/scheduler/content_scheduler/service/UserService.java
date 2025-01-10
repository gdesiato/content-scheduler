package com.scheduler.content_scheduler.service;

import com.scheduler.content_scheduler.dto.UserRequestDTO;
import com.scheduler.content_scheduler.dto.UserResponseDTO;
import com.scheduler.content_scheduler.exception.UserNotFoundException;
import com.scheduler.content_scheduler.mapper.UserMapper;
import com.scheduler.content_scheduler.model.UserEntity;
import com.scheduler.content_scheduler.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponseDTO> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    public UserResponseDTO getUserById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with ID %d not found.", id)));
        return UserMapper.toDTO(user);
    }

    public UserResponseDTO getUserByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with username %s not found.", username)));
        return UserMapper.toDTO(user);
    }

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        UserEntity user = UserMapper.toEntity(userRequestDTO);

        System.out.println("Password before hashing: " + userRequestDTO.password());

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        System.out.println("Password after hashing: " + passwordEncoder.encode(userRequestDTO.password()));

        UserEntity savedUser = userRepository.save(user);
        return UserMapper.toDTO(savedUser);
    }

    public UserResponseDTO updateUser(long id, UserRequestDTO updatedUserData) {

        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        existingUser.setUsername(updatedUserData.username());

        if (updatedUserData.password() != null && !updatedUserData.password().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUserData.password()));
        }

        existingUser.setRole(updatedUserData.role());

        UserEntity updatedUser = userRepository.save(existingUser);

        return UserMapper.toDTO(updatedUser);
    }

    public void deleteUserById(Long id) {
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        userRepository.delete(existingUser);
    }
}
