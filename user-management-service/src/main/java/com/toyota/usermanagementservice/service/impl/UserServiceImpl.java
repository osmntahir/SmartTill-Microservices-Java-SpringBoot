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

/**
 * Implementation of the UserService interface providing operations for managing users.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve
     * @return The UserDto representation of the user
     * @throws UserNotFoundException If no user is found with the given ID
     */
    @Override
    public UserDto getUser(Long id) {
        logger.debug("Fetching user by ID: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> {
            logger.warn("User not found with ID: {}", id);
            return new UserNotFoundException("User not found");
        });
        logger.info("Retrieved user successfully. Username: {}", user.getUsername());
        return mapToDTO(user);
    }

    /**
     * Creates a new user with the provided userDto.
     *
     * @param userDto The UserDto containing user information to be created
     * @return The UserDto of the created user
     * @throws InvalidEmailFormatException If the email format is invalid
     * @throws EmailAlreadyExistsException If the email already exists in the system
     * @throws UsernameAlreadyExistsException If the username already exists in the system
     */
    @Override
    public UserDto createUser(UserDto userDto) {
        logger.debug("Creating new user with username: {}", userDto.getUsername());
        if (!isValidEmail(userDto.getEmail())) {
            logger.warn("Invalid email format: {}", userDto.getEmail());
            throw new InvalidEmailFormatException("Invalid email format: " + userDto.getEmail());
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            logger.warn("Email already exists: {}", userDto.getEmail());
            throw new EmailAlreadyExistsException("Email already exists: " + userDto.getEmail());
        }

        if (userRepository.existsByUsername(userDto.getUsername())) {
            logger.warn("Username already exists: {}", userDto.getUsername());
            throw new UsernameAlreadyExistsException("Username already exists: " + userDto.getUsername());
        }

        User user = convertToEntity(userDto);
        userRepository.save(user);
        userDto.setId(user.getId());
        logger.info("User created successfully. Username: {}", user.getUsername());
        return userDto;
    }

    /**
     * Updates an existing user with the provided userDto.
     *
     * @param id      The ID of the user to update
     * @param userDto The UserDto containing updated user information
     * @return The updated UserDto
     * @throws UserNotFoundException        If no user is found with the given ID
     * @throws UsernameAlreadyExistsException If the new username already exists in the system
     * @throws EmailAlreadyExistsException    If the new email already exists in the system
     * @throws InvalidEmailFormatException   If the email format is invalid
     */
    public UserDto updateUser(Long id, UserDto userDto) {
        logger.debug("Updating user with ID: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> {
            logger.warn("User not found with ID: {}", id);
            return new UserNotFoundException("User not found");
        });

        if (!user.getUsername().equals(userDto.getUsername()) && userRepository.existsByUsername(userDto.getUsername())) {
            logger.warn("Username is already taken: {}", userDto.getUsername());
            throw new UsernameAlreadyExistsException("Username is already taken: " + userDto.getUsername());
        }

        if (!user.getEmail().equals(userDto.getEmail()) && userRepository.existsByEmail(userDto.getEmail())) {
            logger.warn("Email address is already registered: {}", userDto.getEmail());
            throw new EmailAlreadyExistsException("Email address is already registered: " + userDto.getEmail());
        }

        if (!isValidEmail(userDto.getEmail())) {
            logger.warn("Invalid email address format: {}", userDto.getEmail());
            throw new InvalidEmailFormatException("Invalid email address format: " + userDto.getEmail());
        }

        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());

        userRepository.save(user);
        logger.info("User updated successfully. Username: {}", user.getUsername());
        return mapToDTO(user);
    }

    /**
     * Deletes a user with the given ID by marking them as deleted in the database.
     *
     * @param id The ID of the user to delete
     * @throws UserNotFoundException If no user is found with the given ID
     */
    @Override
    public void deleteUser(Long id) {
        logger.debug("Deleting user with ID: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> {
            logger.warn("User not found with ID: {}", id);
            return new UserNotFoundException("User not found with ID: " + id);
        });
        user.setDeleted(true);
        userRepository.save(user);
        logger.info("User deleted successfully. Username: {}", user.getUsername());
    }

    /**
     * Retrieves a paginated list of users based on optional filter criteria.
     *
     * @param firstname  Filter by first name
     * @param lastname   Filter by last name
     * @param username   Filter by username
     * @param email      Filter by email
     * @param page       Page number (starting from 0)
     * @param size       Number of records per page
     * @param sortList   List of fields to sort by
     * @param sortOrder  Sort order (ASC or DESC)
     * @return A Page of UserResponse objects
     */
    @Override
    public Page<UserResponse> getAll(String firstname, String lastname, String username, String email,
                                     int page, int size, List<String> sortList, String sortOrder) {
        logger.info("Fetching users with filters - firstname: {}, lastname: {}, username: {}, email: {}", firstname, lastname, username, email);
        Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sortList, sortOrder)));
        Page<User> entities = userRepository.getUsersFiltered(firstname, lastname, email, username, pageable);
        logger.info("Fetched {} users", entities.getTotalElements());
        return entities.map(this::convertToResponse);
    }

    /**
     * Adds a role to a user identified by userId.
     *
     * @param userId The ID of the user to add the role to
     * @param role   The Role enum value to add
     * @return The updated UserResponse after adding the role
     * @throws RoleAlreadyExistsException If the user already has the specified role
     * @throws UserNotFoundException     If no user is found with the given ID
     */
    @Override
    public UserResponse addRole(Long userId, Role role) {
        logger.debug("Adding role {} to user with ID: {}", role, userId);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (user.getRoles().contains(role)) {
                logger.warn("User already has this role: {}", role);
                throw new RoleAlreadyExistsException("User already has this Role: " + role.toString());
            }

            user.getRoles().add(role);
            userRepository.save(user);
            logger.info("Added role {} to user successfully. Username: {}", role, user.getUsername());
            return convertToResponse(user);
        } else {
            logger.warn("User not found with ID: {}", userId);
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
    }

    /**
     * Removes a role from a user identified by userId.
     *
     * @param userId The ID of the user to remove the role from
     * @param role   The Role enum value to remove
     * @return The updated UserResponse after removing the role
     * @throws SingleRoleRemovalException If the user has only one role and it cannot be removed
     * @throws RoleNotFoundException     If the user does not have the specified role
     * @throws UserNotFoundException     If no user is found with the given ID
     */
    @Override
    public UserResponse removeRole(Long userId, Role role) {
        logger.debug("Removing role {} from user with ID: {}", role, userId);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (user.getRoles().size() <= 1) {
                logger.warn("User has only one role. Cannot remove role: {}", role);
                throw new SingleRoleRemovalException("User has only one role. Cannot remove the role.");
            }

            if (!user.getRoles().contains(role)) {
                logger.warn("User does not have this role: {}", role);
                throw new RoleNotFoundException("User does not have this role: " + role);
            }

            user.getRoles().remove(role);
            userRepository.save(user);
            logger.info("Removed role {} from user successfully. Username: {}", role, user.getUsername());
            return convertToResponse(user);
        } else {
            logger.warn("User not found with ID: {}", userId);
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
    }

    /**
     * Helper method to create a list of Sort.Order based on sortList and sortOrder.
     *
     * @param sortList  List of fields to sort by
     * @param sortOrder Sort order (ASC or DESC)
     * @return List of Sort.Order objects for sorting
     */
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

    /**
     * Helper method to validate email format using regex.
     *
     * @param email The email address to validate
     * @return true if the email format is valid, false otherwise
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    /**
     * Converts a User entity to a UserResponse DTO.
     *
     * @param user The User entity to convert
     * @return The corresponding UserResponse DTO
     */
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

    /**
     * Converts a User entity to a UserDto.
     *
     * @param user The User entity to convert
     * @return The corresponding UserDto
     */
    private UserDto mapToDTO(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles());
        return userDto;
    }

    /**
     * Converts a UserDto to a User entity.
     *
     * @param userDto The UserDto to convert
     * @return The corresponding User entity
     */
    public User convertToEntity(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setRoles(userDto.getRoles());
        return user;
    }
}
