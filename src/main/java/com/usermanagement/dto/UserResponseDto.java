package com.usermanagement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    private long id;
    private String name;
    private String email;
    private String aboutMe;
    private String role;
    private CategoryDto category;

}