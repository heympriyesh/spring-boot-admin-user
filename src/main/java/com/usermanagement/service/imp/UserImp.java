package com.usermanagement.service.imp;

import com.usermanagement.config.AuthConfig;
import com.usermanagement.dto.CategoryDto;
import com.usermanagement.dto.UserRequestDto;
import com.usermanagement.dto.UserResponseDto;
import com.usermanagement.enitiy.UserEntity;
import com.usermanagement.exp.UserAlreadyExistsException;
import com.usermanagement.repo.UserRepo;
import com.usermanagement.service.UserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepo.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("Retrived Data");
        System.out.println(user.getPassword()+"Retrived Password");
        System.out.println(user.getUsername());
        System.out.println(user.getId());
        System.out.println(user.getEmail());
        System.out.println("-----");
        return user;
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
            UserEntity user = this.userReqDtoToUserEntity(userRequestDto);
            user.setPassword(authConfig.passwordEncoder().encode(user.getPassword()));
            UserEntity createdUser = userRepo.save(user);
            return this.userEntityToUserRespDto(createdUser);
        } else {
            // User already exists, throw an exception
            throw new UserAlreadyExistsException("User with email " + userRequestDto.getEmail() + " already exists");
        }
    }


    public UserEntity userReqDtoToUserEntity(UserRequestDto userReqDto) {
        UserEntity user = this.modelMapper.map(userReqDto, UserEntity.class);
        return user;
    }

    public UserResponseDto userEntityToUserRespDto(UserEntity user) {
        UserResponseDto userRespDto = this.modelMapper.map(user,UserResponseDto.class);
        return userRespDto;
    }

    public List<UserResponseDto> findAll() {
        List<UserEntity> allUsers = userRepo.findAll();
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
}
