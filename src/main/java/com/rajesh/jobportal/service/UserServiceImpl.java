package com.rajesh.jobportal.service;

import com.rajesh.jobportal.dto.UserRequestDto;
import com.rajesh.jobportal.dto.UserResponseDto;
import com.rajesh.jobportal.entity.User;
import com.rajesh.jobportal.enums.Role;
import com.rajesh.jobportal.exception.ResourceNotFoundException;
import com.rajesh.jobportal.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDto registerUser(UserRequestDto requestDto) {

        if (requestDto.getRole() == Role.ADMIN) {
            throw new IllegalArgumentException(
                    "Admin registration is not allowed"
            );
        }

        User user = new User(
                null,
                requestDto.getName(),
                requestDto.getEmail(),
                requestDto.getPhoneNumber(),
                passwordEncoder.encode(requestDto.getPassword()),
                requestDto.getRole()
        );

        User savedUser = userRepository.save(user);

        return new UserResponseDto(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getPhoneNumber(),
                savedUser.getRole()
        );
    }

    @Override
    public List<UserResponseDto> getAllUsers() {

        List<User> users = userRepository.findAll();

        List<UserResponseDto> responseList = new ArrayList<>();

        for (User user : users) {

            UserResponseDto responseDto = new UserResponseDto(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getPhoneNumber(),
                    user.getRole()
            );

            responseList.add(responseDto);
        }

        return responseList;
    }

    @Override
    public UserResponseDto getUserById(Long id) {

        User user =
                userRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found with Id: " + id
                                ));

        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole()
        );
    }

    @Override
    public UserResponseDto updateUser(
            Long id,
            UserRequestDto user) {

        User existingUser =
                userRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found with id: " + id
                                ));

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhoneNumber(user.getPhoneNumber());

        // Hash the password before storing it
        if (user.getPassword() != null &&
                !user.getPassword().isBlank()) {

            existingUser.setPassword(
                    passwordEncoder.encode(
                            user.getPassword()
                    )
            );
        }

        // Do not allow ADMIN role through normal user update
        if (user.getRole() == Role.ADMIN) {
            throw new IllegalArgumentException(
                    "Admin role cannot be assigned through this operation"
            );
        }

        existingUser.setRole(user.getRole());

        User savedUser =
                userRepository.save(existingUser);

        return new UserResponseDto(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getPhoneNumber(),
                savedUser.getRole()
        );
    }

    @Override
    public void deleteUser(Long id) {

        if (!userRepository.existsById(id)) {

            throw new ResourceNotFoundException(
                    "User not found with id: " + id
            );
        }

        userRepository.deleteById(id);
    }
}