package com.usermanagement.dto.resp;

public interface UserListProjection {

    int getId();

    String getName();

    String getEmail();

    String getAboutMe();

    Integer getCategoryId();
}