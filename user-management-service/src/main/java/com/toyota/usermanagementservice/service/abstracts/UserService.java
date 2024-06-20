package com.toyota.usermanagementservice.service.abstracts;

import com.toyota.usermanagementservice.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUser(Long id);

    UserDto updateUser(Long id, UserDto userDto);

    void deleteUser(Long id);

    List<UserDto> getAllUsers();
}
