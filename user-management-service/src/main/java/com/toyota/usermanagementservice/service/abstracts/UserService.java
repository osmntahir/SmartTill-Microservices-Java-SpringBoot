package com.toyota.usermanagementservice.service.abstracts;

import com.toyota.usermanagementservice.dto.UserDto;
import com.toyota.usermanagementservice.dto.UserResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUser(Long id);

    UserDto updateUser(Long id, UserDto userDto);

    void deleteUser(Long id);

    Page<UserResponse> getAll(String firstname, String lastname, String username, String email, int page, int size, List<String> sortList, String sortOrder);
}
