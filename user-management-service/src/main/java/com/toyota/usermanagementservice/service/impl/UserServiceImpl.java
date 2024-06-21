package com.toyota.usermanagementservice.service.impl;

import com.toyota.usermanagementservice.dao.UserRepository;
import com.toyota.usermanagementservice.domain.Role;
import com.toyota.usermanagementservice.domain.User;
import com.toyota.usermanagementservice.dto.UserDto;
import com.toyota.usermanagementservice.dto.UserResponse;

import com.toyota.usermanagementservice.exception.*;

import com.toyota.usermanagementservice.service.abstracts.UserService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;

import java.util.List;

import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;



    private final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Override
    public UserDto getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        return mapToDTO(user);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        if (!isValidEmail(userDto.getEmail())) {
            throw new InvalidEmailFormatException("Invalid email format: " + userDto.getEmail());
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists: " + userDto.getEmail());
        }

        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists: " + userDto.getUsername());
        }

        User user = convertToEntity(userDto);
        userRepository.save(user);
        userDto.setId(user.getId());
        return userDto;
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));


        if (!user.getUsername().equals(userDto.getUsername()) && userRepository.existsByUsername(userDto.getUsername())) {
            throw new UsernameAlreadyExistsException("Username is already taken: " + userDto.getUsername());
        }


        if (!user.getEmail().equals(userDto.getEmail()) && userRepository.existsByEmail(userDto.getEmail())) {
            throw new EmailAlreadyExistsException("Email address is already registered: " + userDto.getEmail());
        }


        if (!isValidEmail(userDto.getEmail())) {
            throw new InvalidEmailFormatException("Invalid email address format: " + userDto.getEmail());
        }


        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());


        userRepository.save(user);


        return mapToDTO(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
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
        logger.info("Fetched {} users" , entities.getTotalElements());
        return entities.map(this::convertToResponse);
    }
    @Override
    public UserResponse addRole(Long userId, Role role) {
        logger.info("Adding role to user. User ID: {}, New Role: {}", userId, role);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (user.getRoles().contains(role)) {
                logger.warn("User already has this role! Role: {}", role);
                throw new RoleAlreadyExistsException("User already has this Role. Role: " + role.toString());
            }

            user.getRoles().add(role);
            userRepository.save(user);
            logger.info("Added new role to user. Username: {}, Role: {}", user.getUsername(), role);
            return convertToResponse(user);
        } else {
            logger.warn("User not found! ID: {}", userId);
            throw new UserNotFoundException("User not found! ID: " + userId);
        }
    }

    @Override
    public UserResponse removeRole(Long userId, Role role) {
        logger.info("Removing role from user. User ID: {}, Role: {}", userId, role);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (user.getRoles().size() <= 1) {
                logger.warn("User has only one role! Username: {}", user.getUsername());
                throw new SingleRoleRemovalException("User has only one role. Cannot remove the role.");
            }

            if (!user.getRoles().contains(role)) {
                logger.warn("The user does not have this role! Role: {}", role);
                throw new RoleNotFoundException("The user does not have this role! Role: " + role);
            }

            user.getRoles().remove(role);
            userRepository.save(user);
            logger.info("Removed role from user successfully. Username: {}, Role: {}", user.getUsername(), role);
            return convertToResponse(user);
        } else {
            logger.warn("User not found! ID: {}", userId);
            throw new UserNotFoundException("User not found! ID: " + userId);
        }
    }



    private List<Sort.Order> createSortOrder(List<String> sortList, String sortOrder) {
        return sortList.stream()
                .map(field -> {
                    if ("ASC".equalsIgnoreCase(sortOrder)) {
                        return Sort.Order.asc(field);
                    } else {
                        return Sort.Order.desc(field);
                    }
                })
                .collect(Collectors.toList());
    }


    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
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

    private User convertToEntity(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setRoles(userDto.getRoles());
        return user;
    }

}
