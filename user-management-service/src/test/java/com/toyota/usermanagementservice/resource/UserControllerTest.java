package com.toyota.usermanagementservice.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toyota.usermanagementservice.domain.Role;
import com.toyota.usermanagementservice.dto.RoleRequest;
import com.toyota.usermanagementservice.dto.UserDto;
import com.toyota.usermanagementservice.dto.UserResponse;
import com.toyota.usermanagementservice.service.abstracts.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Unit tests for {@link UserController}.
 */
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    /**
     * Sets up the mockMvc instance before each test method runs.
     */
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    /**
     * Test case for creating a new user.
     *
     * @throws Exception if mockMvc.perform throws an exception
     */
    @Test
    void testCreateUser() throws Exception {
        // Prepare a sample UserDto
        UserDto userDto = new UserDto();
        userDto.setFirstName("John");
        userDto.setLastName("Doe");

        // Mock the userService's behavior
        when(userService.createUser(any(UserDto.class))).thenReturn(userDto);

        // Perform the POST request and validate the response
        mockMvc.perform(post("/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    /**
     * Test case for retrieving a user by ID.
     *
     * @throws Exception if mockMvc.perform throws an exception
     */
    @Test
    void testGetUser() throws Exception {
        // Prepare a sample UserDto
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstName("John");
        userDto.setLastName("Doe");

        // Mock the userService's behavior
        when(userService.getUser(1L)).thenReturn(userDto);

        // Perform the GET request and validate the response
        mockMvc.perform(get("/users/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    /**
     * Test case for updating a user.
     *
     * @throws Exception if mockMvc.perform throws an exception
     */
    @Test
    void testUpdateUser() throws Exception {
        // Prepare a sample UserDto
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstName("Updated John");
        userDto.setLastName("Updated Doe");

        // Mock the userService's behavior
        when(userService.updateUser(eq(1L), any(UserDto.class))).thenReturn(userDto);

        // Perform the PUT request and validate the response
        mockMvc.perform(put("/users/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Updated John"))
                .andExpect(jsonPath("$.lastName").value("Updated Doe"));
    }

    /**
     * Test case for deleting a user.
     *
     * @throws Exception if mockMvc.perform throws an exception
     */
    @Test
    void testDeleteUser() throws Exception {
        // Mock the userService's behavior to do nothing when deleting user
        doNothing().when(userService).deleteUser(1L);

        // Perform the DELETE request and validate the response
        mockMvc.perform(delete("/users/delete/{id}", 1))
                .andExpect(status().isNoContent());
    }

    /**
     * Test case for retrieving all users.
     */
    @Test
    public void testGetAllUsers() {
        // Mock the UserService to return an empty Page of UserResponse
        Page<UserResponse> mockProductPage = Page.empty();
        when(userService.getAll(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt(), anyList(), anyString()))
                .thenReturn(mockProductPage);

        // Call the controller method
        Page<UserResponse> result = userController.getAll("",
                "", "", "",
                0, 5, Collections.emptyList(), "ASC");

        // Assertions
        assertNotNull(result);
        assertEquals(mockProductPage, result);
    }

    /**
     * Test case for adding a role to a user.
     *
     * @throws Exception if mockMvc.perform throws an exception
     */
    @Test
    public void testAddRoleToUser() throws Exception {
        // Prepare a RoleRequest with role "ADMIN"
        RoleRequest roleRequest = new RoleRequest();
        roleRequest.setRole("ADMIN");

        // Prepare a sample UserResponse
        UserResponse userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setFirstname("John");
        userResponse.setLastname("Doe");

        // Mock the userService's behavior
        when(userService.addRole(eq(1L), any(Role.class))).thenReturn(userResponse);

        // Perform the PUT request and validate the response
        mockMvc.perform(put("/users/role/add/{user_id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(roleRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstname").value("John"))
                .andExpect(jsonPath("$.lastname").value("Doe"));
    }

    /**
     * Test case for removing a role from a user.
     *
     * @throws Exception if mockMvc.perform throws an exception
     */
    @Test
    public void testRemoveRoleFromUser() throws Exception {
        // Prepare a RoleRequest with role "ADMIN"
        RoleRequest roleRequest = new RoleRequest();
        roleRequest.setRole("ADMIN");

        // Prepare a sample UserResponse
        UserResponse userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setFirstname("John");
        userResponse.setLastname("Doe");

        // Mock the userService's behavior
        when(userService.removeRole(eq(1L), any(Role.class))).thenReturn(userResponse);

        // Perform the PUT request and validate the response
        mockMvc.perform(put("/users/role/remove/{user_id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(roleRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstname").value("John"))
                .andExpect(jsonPath("$.lastname").value("Doe"));
    }
}
