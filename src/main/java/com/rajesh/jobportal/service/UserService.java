package com.rajesh.jobportal.service;

import com.rajesh.jobportal.dto.UserRequestDto;
import com.rajesh.jobportal.dto.UserResponseDto;
import com.rajesh.jobportal.entity.User;

import java.util.List;

public interface UserService {
    UserResponseDto registerUser(UserRequestDto requestDto);
    List<UserResponseDto> getAllUsers();
    UserResponseDto getUserById(Long id);
    UserResponseDto updateUser(Long id, UserRequestDto requestDto);
    void deleteUser(Long id);
}
