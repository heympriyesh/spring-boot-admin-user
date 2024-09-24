package com.usermanagement.service;

import com.usermanagement.dto.req.UserRequestDto;
import com.usermanagement.dto.resp.UserResponseDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<UserResponseDto> getAllUser();
    public UserResponseDto createUser(UserRequestDto userRequestDto);

}
