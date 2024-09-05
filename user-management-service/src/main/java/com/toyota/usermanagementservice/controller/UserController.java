package com.toyota.usermanagementservice.controller;

import com.toyota.usermanagementservice.dto.UserDto;
import com.toyota.usermanagementservice.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing users in the system.
 * This controller handles requests related to creating, updating, retrieving, and deleting users, as well as
 * assigning and unassigning roles.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserManagementService userManagementService;

    /**
     * Constructs a new {@link UserController} with the provided {@link UserManagementService}.
     *
     * @param userManagementService the service handling user-related operations
     */
    @Autowired
    public UserController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    /**
     * Retrieves all users from the system.
     *
     * @return a {@link ResponseEntity} containing a list of {@link UserDto}, or {@link HttpStatus#NO_CONTENT} if no users are found
     */
    @GetMapping("")
    public ResponseEntity<List<UserDto>> getUsers() {
        List<UserDto> users = userManagementService.getUsers();

        if (users != null && !users.isEmpty()) {
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    /**
     * Creates a new user in the system.
     *
     * @param userDto the user data to create
     * @return a {@link ResponseEntity} containing the result of the creation process
     */
    @PostMapping("")
    public ResponseEntity<String> createUser(@RequestBody UserDto userDto) {
        return userManagementService.createUser(userDto);
    }

    /**
     * Updates an existing user based on the provided ID.
     *
     * @param id the ID of the user to update
     * @param userDto the new user data
     * @return a {@link ResponseEntity} containing the result of the update process
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable String id, @RequestBody UserDto userDto) {
        return userManagementService.updateUser(id, userDto);
    }

    /**
     * Deletes a user based on the provided ID.
     *
     * @param id the ID of the user to delete
     * @return a {@link ResponseEntity} containing the result of the deletion process
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        return userManagementService.deleteUser(id);
    }

    /**
     * Assigns a role to the user based on the provided user ID and role name.
     *
     * @param id the ID of the user to assign the role to
     * @param roleName the name of the role to assign
     * @return a {@link ResponseEntity} containing the result of the role assignment
     */
    @PostMapping("/assign-role/{id}")
    public ResponseEntity<String> assignRole(@PathVariable String id, @RequestParam String roleName) {
        return userManagementService.assignRole(id, roleName);
    }

    /**
     * Unassigns a role from the user based on the provided user ID and role name.
     *
     * @param id the ID of the user to unassign the role from
     * @param roleName the name of the role to unassign
     * @return a {@link ResponseEntity} containing the result of the role unassignment
     */
    @PostMapping("/unassign-role/{id}")
    public ResponseEntity<String> unassignRole(@PathVariable String id, @RequestParam String roleName) {
        return userManagementService.unassignRole(id, roleName);
    }

}
