package com.usermanagement.controllers;

import com.usermanagement.dto.ApiResponse;
import com.usermanagement.dto.UserResponseDto;
import com.usermanagement.service.imp.CategoryImp;
import com.usermanagement.service.imp.UserImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    public UserImp userImp;

    @Autowired
    public CategoryImp categoryImp;

    @GetMapping("all-users")
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getAllUser(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size
    ) {
        List<UserResponseDto> userData = userImp.findAll(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("User fetched successfully", userData));
    }

    @PostMapping("/update-role/{id}")
    public ResponseEntity<ApiResponse<String>> updateUserRole(
            @RequestBody Map<String, Object> requestBody,
            @PathVariable int id
    ) {
        String role = (String) requestBody.get("role");
        this.userImp.updateUserRole(role, id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("User role updated successfully", null));
    }
}
