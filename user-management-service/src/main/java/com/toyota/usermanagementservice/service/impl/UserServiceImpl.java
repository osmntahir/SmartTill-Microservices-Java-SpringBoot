package com.toyota.usermanagementservice.service.impl;

import com.toyota.usermanagementservice.dao.UserRepository;
import com.toyota.usermanagementservice.domain.User;
import com.toyota.usermanagementservice.dto.UserDto;
import com.toyota.usermanagementservice.exception.UserNotFoundException;
import com.toyota.usermanagementservice.service.abstracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDto createUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setRoles(userDto.getRoles());
        userRepository.save(user);
        userDto.setId(user.getId());
        return userDto;
    }

    public UserDto getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        return mapToDTO(user);
    }

    public UserDto updateUser(Long id, UserDto UserDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setUsername(UserDto.getUsername());
        user.setFirstName(UserDto.getFirstName());
        user.setLastName(UserDto.getLastName());
        user.setEmail(UserDto.getEmail());
        user.setRoles(UserDto.getRoles());
        userRepository.save(user);
        return mapToDTO(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setDeleted(true);
        userRepository.save(user);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private UserDto mapToDTO(User user) {
        UserDto UserDto = new UserDto();
        UserDto.setId(user.getId());
        UserDto.setUsername(user.getUsername());
        UserDto.setFirstName(user.getFirstName());
        UserDto.setLastName(user.getLastName());
        UserDto.setEmail(user.getEmail());
        UserDto.setRoles(user.getRoles());
        return UserDto;
    }
}
