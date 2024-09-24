package com.usermanagement.dto.resp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    private String name;
    private String email;
//    private String password;
    private String aboutMe;
    private long  id;
}
