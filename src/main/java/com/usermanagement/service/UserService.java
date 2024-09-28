package com.usermanagement.service;

import com.usermanagement.dto.UserRequestDto;
import com.usermanagement.dto.UserResponseDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<UserResponseDto> getAllUser();
    UserResponseDto createUser(UserRequestDto userRequestDto);

    List<UserResponseDto> findAll();

    List<UserResponseDto> getUserListByCategoryId(int categoryId);

    void deleteUserByEmail(String email);

    void deleteUserById(int id);

}
