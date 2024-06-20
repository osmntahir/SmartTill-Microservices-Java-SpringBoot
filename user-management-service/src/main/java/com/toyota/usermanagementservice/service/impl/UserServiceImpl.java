package com.toyota.usermanagementservice.service.impl;

import com.toyota.usermanagementservice.dao.UserRepository;
import com.toyota.usermanagementservice.domain.User;
import com.toyota.usermanagementservice.dto.UserDto;
import com.toyota.usermanagementservice.dto.UserResponse;
import com.toyota.usermanagementservice.exception.UserNotFoundException;
import com.toyota.usermanagementservice.service.abstracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

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

    @Override
    public Page<UserResponse> getAll(String firstname, String lastname,
                                     String username, String email,
                                     int page, int size, List<String> sortList,
                                     String sortOrder) {
        logger.info("Fetching users");
        Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sortList, sortOrder)));
        Page<User> entities = userRepository.getUsersFiltered(firstname, lastname, email, username, pageable);
        logger.info("Fetched {} users");
        return entities.map(this::convertToResponse);
    }

    private UserResponse convertToResponse(User user) {
        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setFirstname(user.getFirstName());
        response.setLastname(user.getLastName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRoles());

        return response;
    }

    private List<Sort.Order> createSortOrder(List<String> sortList, String sortOrder) {
        List<Sort.Order> orders = sortList.stream()
                .map(field -> {
                    if ("ASC".equalsIgnoreCase(sortOrder)) {
                        return Sort.Order.asc(field);
                    } else {
                        return Sort.Order.desc(field);
                    }
                })
                .collect(Collectors.toList());
        return orders;
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
