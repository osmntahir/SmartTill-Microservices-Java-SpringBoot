package com.toyota.usermanagementservice.service.abstracts;

import com.toyota.usermanagementservice.domain.Role;
import com.toyota.usermanagementservice.dto.UserDto;
import com.toyota.usermanagementservice.dto.UserResponse;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Service interface for managing user operations.
 */
public interface UserService {

    /**
     * Creates a new user.
     *
     * @param userDto The UserDto object containing user details
     * @return The created UserDto object
     */
    UserDto createUser(UserDto userDto);

    /**
     * Retrieves a user by ID.
     *
     * @param id The ID of the user to retrieve
     * @return The UserDto object corresponding to the given ID
     */
    UserDto getUser(Long id);

    /**
     * Updates an existing user.
     *
     * @param id      The ID of the user to update
     * @param userDto The UserDto object containing updated user details
     * @return The updated UserDto object
     */
    UserDto updateUser(Long id, UserDto userDto);

    /**
     * Deletes a user by ID.
     *
     * @param id The ID of the user to delete
     */
    void deleteUser(Long id);

    /**
     * Retrieves a page of users with optional filtering and sorting.
     *
     * @param firstname Filter by first name
     * @param lastname  Filter by last name
     * @param username  Filter by username
     * @param email     Filter by email
     * @param page      Page number
     * @param size      Number of records per page
     * @param sortList  List of fields to sort by
     * @param sortOrder Sort order (ASC or DESC)
     * @return A Page of UserResponse objects based on the filtering and pagination criteria
     */
    Page<UserResponse> getAll(String firstname, String lastname, String username, String email, int page, int size, List<String> sortList, String sortOrder);

    /**
     * Adds a role to a user.
     *
     * @param userId ID of the user
     * @param role   Role to add
     * @return The updated UserResponse object
     */
    UserResponse addRole(Long userId, Role role);

    /**
     * Removes a role from a user.
     *
     * @param userId ID of the user
     * @param role   Role to remove
     * @return The updated UserResponse object
     */
    UserResponse removeRole(Long userId, Role role);
}
