package com.toyota.usermanagementservice.resource;

import com.toyota.usermanagementservice.domain.Role;
import com.toyota.usermanagementservice.dto.RoleRequest;
import com.toyota.usermanagementservice.dto.UserDto;
import com.toyota.usermanagementservice.dto.UserResponse;
import com.toyota.usermanagementservice.service.abstracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing user operations.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Endpoint for creating a new user.
     *
     * @param userDto The UserDto object containing user details
     * @return ResponseEntity with the created UserDto object
     */
    @PostMapping("/add")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.createUser(userDto));
    }

    /**
     * Endpoint for retrieving a user by ID.
     *
     * @param id The ID of the user to retrieve
     * @return ResponseEntity with the UserDto object
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    /**
     * Endpoint for updating an existing user.
     *
     * @param id      The ID of the user to update
     * @param userDto The UserDto object containing updated user details
     * @return ResponseEntity with the updated UserDto object
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    /**
     * Endpoint for deleting a user by ID.
     *
     * @param id The ID of the user to delete
     * @return ResponseEntity indicating successful deletion
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint for retrieving all users with optional filtering and pagination.
     *
     * @param firstName  Filter by first name (default: "")
     * @param lastName   Filter by last name (default: "")
     * @param username   Filter by username (default: "")
     * @param email      Filter by email (default: "")
     * @param page       Page number (default: 0)
     * @param size       Number of records per page (default: 5)
     * @param sortList   List of fields to sort by (default: empty)
     * @param sortOrder  Sort order (default: "ASC")
     * @return Page of UserResponse objects based on the filtering and pagination criteria
     */
    @GetMapping("/getAll")
    public Page<UserResponse> getAll(
            @RequestParam(defaultValue = "") String firstName,
            @RequestParam(defaultValue = "") String lastName,
            @RequestParam(defaultValue = "") String username,
            @RequestParam(defaultValue = "") String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "") List<String> sortList,
            @RequestParam(defaultValue = "ASC") String sortOrder) {
        return userService.getAll(firstName, lastName, username, email, page, size, sortList, sortOrder);
    }

    /**
     * Endpoint for adding a role to a user.
     *
     * @param user_id     ID of the user
     * @param roleRequest RoleRequest object containing the role to add
     * @return ResponseEntity with the updated UserResponse object
     */
    @PutMapping("/role/add/{user_id}")
    public ResponseEntity<UserResponse> addRole(@PathVariable("user_id") Long user_id, @RequestBody RoleRequest roleRequest) {
        Role role = Role.forValue(roleRequest.getRole());
        UserResponse response = userService.addRole(user_id, role);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Endpoint for removing a role from a user.
     *
     * @param user_id     ID of the user
     * @param roleRequest RoleRequest object containing the role to remove
     * @return ResponseEntity with the updated UserResponse object
     */
    @PutMapping("/role/remove/{user_id}")
    public ResponseEntity<UserResponse> removeRole(@PathVariable("user_id") Long user_id, @RequestBody RoleRequest roleRequest) {
        Role role = Role.forValue(roleRequest.getRole());
        UserResponse response = userService.removeRole(user_id, role);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
