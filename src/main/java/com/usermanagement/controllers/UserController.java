package com.usermanagement.controllers;

import com.usermanagement.dto.ApiResponse;
import com.usermanagement.dto.UserResponseDto;
import com.usermanagement.service.imp.UserImp;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserImp userImp;

    @Autowired
    private ModelMapper modelMapper;
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getAllUser() {
        List<UserResponseDto> userData = userImp.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("User fetched successfully", userData));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDto>> createUser() {
//        this.userService
        return null;
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getAllUserDetailsByCategoryId(@PathVariable("id") int id) {
        List<UserResponseDto> userListByCategoryId = userImp.getUserListByCategoryId(id);
        System.out.println("userList By Cateogry Id" + userListByCategoryId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Done", userListByCategoryId));
    }

    @DeleteMapping("/removeUser")
    public ResponseEntity<ApiResponse<String>> deleteUserByEmail(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String email
    ) {
        System.out.println("email " + email);
        if (id != null) {
            userImp.deleteUserById(id);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("User deleted successfully by id"));
        } else if (email != null) {
            userImp.deleteUserByEmail(email);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("User deleted successfully by email."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("Either ID or email must be provided to delete a user."));
        }
    }
}
