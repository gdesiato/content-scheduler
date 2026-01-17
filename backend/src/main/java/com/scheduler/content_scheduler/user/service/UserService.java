package com.scheduler.content_scheduler.user.service;

import com.scheduler.content_scheduler.post.model.Platform;
import com.scheduler.content_scheduler.user.dto.UserRequestDTO;
import com.scheduler.content_scheduler.user.dto.UserResponseDTO;
import com.scheduler.content_scheduler.exception.UserNotFoundException;
import com.scheduler.content_scheduler.user.mapper.UserMapper;
import com.scheduler.content_scheduler.user.model.UserEntity;
import com.scheduler.content_scheduler.user.model.UserPlatformToken;
import com.scheduler.content_scheduler.user.repository.UserPlatformTokenRepository;
import com.scheduler.content_scheduler.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserPlatformTokenRepository userPlatformTokenRepository;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       UserPlatformTokenRepository userPlatformTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userPlatformTokenRepository = userPlatformTokenRepository;
    }

    public List<UserResponseDTO> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    public UserResponseDTO getUserById(UUID id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with ID %d not found.", id)));
        return UserMapper.toDTO(user);
    }

    public UserResponseDTO getUserByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with username %s not found.", username)));
        return UserMapper.toDTO(user);
    }

    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                String.format("User with username %s not found.", username)
                        )
                );
    }

    public UserEntity findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                String.format("User with id %s not found.", id)
                        )
                );
    }

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        UserEntity user = UserMapper.toEntity(userRequestDTO);
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        UserEntity savedUser = userRepository.save(user);
        return UserMapper.toDTO(savedUser);
    }

    public UserResponseDTO updateUser(UUID id, UserRequestDTO updatedUserData) {

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

    public void deleteUserById(UUID id) {
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        userRepository.delete(existingUser);
    }

    @Transactional
    public void savePlatformToken(
            UserEntity user,
            Platform platform,
            String accessToken,
            String refreshToken
    ) {
        UserPlatformToken token = userPlatformTokenRepository
                .findByUserAndPlatform(user, platform)
                .orElseGet(() -> new UserPlatformToken(user, platform));

        token.updateTokens(accessToken, refreshToken);

        userPlatformTokenRepository.save(token);
    }

    public UserPlatformToken getPlatformToken(
            UserEntity user,
            Platform platform
    ) {
        return userPlatformTokenRepository
                .findByUserAndPlatform(user, platform)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No token for platform " + platform
                        )
                );
    }
}
