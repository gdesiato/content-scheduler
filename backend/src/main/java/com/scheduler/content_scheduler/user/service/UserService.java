package com.scheduler.content_scheduler.user.service;

import com.scheduler.content_scheduler.user.dto.UserRequestDTO;
import com.scheduler.content_scheduler.user.dto.UserResponseDTO;
import com.scheduler.content_scheduler.exception.UserNotFoundException;
import com.scheduler.content_scheduler.user.mapper.UserMapper;
import com.scheduler.content_scheduler.user.model.UserEntity;
import com.scheduler.content_scheduler.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

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
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
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
