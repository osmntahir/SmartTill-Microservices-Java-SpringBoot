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

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.createUser(userDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users")
    public Page<UserResponse> getAll(
            @RequestParam(defaultValue = "")String firstName,
            @RequestParam(defaultValue = "")String lastName,
            @RequestParam(defaultValue = "")String username,
            @RequestParam(defaultValue = "")String email,
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "5")int size,
            @RequestParam(defaultValue = "") List<String> sortList,
            @RequestParam(defaultValue = "ASC") String sortOrder

    )
    {
        return userService.getAll(firstName, lastName, username
                , email,page,size,sortList,sortOrder);
    }

    /**
     * Adds role to user
     * @param user_id ID of user
     * @param roleRequest Role to be added
     * @return ResponseEntity with UserResponse
     */

    @PutMapping("/role/add/{user_id}")
    public ResponseEntity<UserResponse> addRole(@PathVariable("user_id") Long user_id, @RequestBody RoleRequest roleRequest) {
        Role role = Role.forValue(roleRequest.getRole());
        UserResponse response = userService.addRole(user_id, role);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Removes role from user
     * @param user_id ID of user
     * @param roleRequest Role to be removed
     * @return ResponseEntity with UserResponse
     */

    @PutMapping("/role/remove/{user_id}")
    public ResponseEntity<UserResponse> removeRole(@PathVariable("user_id") Long user_id, @RequestBody RoleRequest roleRequest) {
        Role role = Role.forValue(roleRequest.getRole());
        UserResponse response = userService.removeRole(user_id, role);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
