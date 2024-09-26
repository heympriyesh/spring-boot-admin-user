package com.usermanagement.controllers;

import com.usermanagement.dto.ApiResponse;
import com.usermanagement.dto.CategoryDto;
import com.usermanagement.dto.UserResponseDto;
import com.usermanagement.enitiy.UserEnitiy;
import com.usermanagement.repo.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getAllUser() {
        List<UserEnitiy> allUsers = userRepo.findAll();
        List<UserResponseDto> userMapper = allUsers.stream()
                .map(user -> {
                    UserResponseDto dto = modelMapper.map(user, UserResponseDto.class);
                    if (user.getCategory() != null) {
                        CategoryDto categoryDto = modelMapper.map(user.getCategory(), CategoryDto.class);
                        dto.setCategory(categoryDto);
                    }
                    return dto;
                }).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("User fetched successfully", userMapper));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDto>> createUser() {
//        this.userService
        return null;
    }

}
