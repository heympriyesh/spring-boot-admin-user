package com.usermanagement.dto.resp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserListByCategoryIdDto {
    private String name;
    private String email;
    private String aboutMe;
    private int categoryId;

    // Constructor needed for the JPQL expression
    public UserListByCategoryIdDto(String name, String email, String aboutMe, int categoryId) {
        this.name = name;
        this.email = email;
        this.aboutMe = aboutMe;
        this.categoryId = categoryId;
    }
}
