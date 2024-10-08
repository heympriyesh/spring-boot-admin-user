package com.usermanagement.service.imp;

import com.usermanagement.config.AuthConfig;
import com.usermanagement.dto.CategoryDto;
import com.usermanagement.dto.UserRequestDto;
import com.usermanagement.dto.UserResponseDto;
import com.usermanagement.enitiy.CategoryEntity;
import com.usermanagement.enitiy.UserEntity;
import com.usermanagement.exception.ResourceNotFoundException;
import com.usermanagement.exp.UserAlreadyExistsException;
import com.usermanagement.repo.CategoryRepo;
import com.usermanagement.repo.UserRepo;
import com.usermanagement.service.UserService;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserImp implements UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AuthConfig authConfig;

    @Autowired
    private CategoryRepo categoryRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = this.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        // Assuming each user has a single role stored in a field called 'role'
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole());

        // Ensure a non-null authorities collection is returned
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), Collections.singletonList(authority));
    }

    @Override
    public List<UserResponseDto> getAllUser() {
        List<UserEntity> userEntities = userRepo.findAll();
        List<UserResponseDto> userResponseDtoList = userEntities.stream().map(user -> this.userEntityToUserRespDto(user)).collect(Collectors.toList());
        return userResponseDtoList;


    }
    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        Optional<UserEntity> foundUser = this.userRepo.findByEmail(userRequestDto.getEmail());
        if (foundUser.isEmpty()) {
            CategoryEntity category = categoryRepo.findById(userRequestDto.getCategory_id()).orElseThrow(() -> new ResourceAccessException("Category not found with ID :" + userRequestDto.getCategory_id()));
            UserEntity user = this.userReqDtoToUserEntity(userRequestDto, category);
            user.setPassword(authConfig.passwordEncoder().encode(user.getPassword()));
            UserEntity createdUser = userRepo.save(user);
            return this.userEntityToUserRespDto(createdUser);
        } else {
            // User already exists, throw an exception
            throw new UserAlreadyExistsException("User with email " + userRequestDto.getEmail() + " already exists");
        }
    }


    public UserEntity userReqDtoToUserEntity(UserRequestDto userReqDto, CategoryEntity category) {
        UserEntity user = this.modelMapper.map(userReqDto, UserEntity.class);
        user.setCategory(category);
        return user;
    }

    public UserResponseDto userEntityToUserRespDto(UserEntity user) {
        UserResponseDto userRespDto = this.modelMapper.map(user,UserResponseDto.class);
        return userRespDto;
    }

    public List<UserResponseDto> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<UserEntity> allUsers = userRepo.findAll(pageable);
        List<UserResponseDto> userMapper = allUsers.stream()
                .map(user -> {
                    UserResponseDto dto = modelMapper.map(user, UserResponseDto.class);
                    if (user.getCategory() != null) {
                        CategoryDto categoryDto = modelMapper.map(user.getCategory(), CategoryDto.class);
                        dto.setCategory(categoryDto);
                    }
                    return dto;
                }).collect(Collectors.toList());
        return userMapper;
    }

    @Override
    public List<UserResponseDto> getUserListByCategoryId(int categoryId) {
        List<UserEntity> userListByCategoryId = userRepo.getUserListByCategoryId(categoryId);
        return userListByCategoryId.stream().map(user -> modelMapper.map(user, UserResponseDto.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteUserByEmail(String email) {
        if (this.userRepo.existsByEmail(email)) {
            this.userRepo.deleteUserByEmail(email);
        } else {
            throw new RuntimeException("User with email " + email + " not found");
        }
    }

    @Override
    @Transactional
    public void deleteUserById(int id) {
        if (this.userRepo.existsById(id)) {
            this.userRepo.deleteUserById(id);
        } else {
            throw new RuntimeException("User with id " + id + " not found");
        }
    }

    @Override
    public UserEntity findByEmail(String email) {
        return this.userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with email " + email));
    }

    @Override
    @Transactional
    public void updateUserRole(@NonNull String role, int id) {
        Optional<UserEntity> userById = this.userRepo.getUserById((id));
        if (userById.isEmpty()) {
            throw new ResourceNotFoundException("User not found with id " + id);
        }
        System.out.println(userById);
        this.userRepo.updateUserRole(role, id);
    }

}
