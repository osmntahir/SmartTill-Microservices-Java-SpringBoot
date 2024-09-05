package com.toyota.usermanagementservice.controller;


import com.toyota.usermanagementservice.dto.UserDto;
import com.toyota.usermanagementservice.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserManagementService userManagementService;

    @Autowired
    public UserController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }
    @GetMapping("")
    public ResponseEntity<List<UserDto>> getUsers() {
        List<UserDto> users =  userManagementService.getUsers();

        if (users != null && !users.isEmpty()) {
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }
    @PostMapping("")
    public ResponseEntity<String> createUser(@RequestBody UserDto userDto) {
        return userManagementService.createUser(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable String id, @RequestBody UserDto userDto) {
        return userManagementService.updateUser(id, userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        return userManagementService.deleteUser(id);
    }

    @PostMapping("/assign-role/{id}")
    public ResponseEntity<String> assignRole(@PathVariable String id, @RequestParam String roleName) {
        return userManagementService.assignRole(id, roleName);
    }
    @PostMapping("/unassign-role/{id}")
    public ResponseEntity<String> unassignRole(@PathVariable String id, @RequestParam String roleName) {
        return userManagementService.unassignRole(id, roleName);
    }

}
