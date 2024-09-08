package com.toyota.usermanagementservice.controller;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toyota.usermanagementservice.dto.UserDto;
import com.toyota.usermanagementservice.service.UserManagementService;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {UserController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class UserControllerTest {
    @Autowired
    private UserController userController;

    @MockBean
    private UserManagementService userManagementService;

    /**
     * Method under test: {@link UserController#getUsers()}
     */
    @Test
    void testGetUsers() throws Exception {
        // Arrange
        when(userManagementService.getUsers()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user");

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController).build().perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Method under test: {@link UserController#getUsers()}
     */
    @Test
    void testGetUsers2() throws Exception {
        // Arrange
        ArrayList<UserDto> userDtoList = new ArrayList<>();
        userDtoList.add(new UserDto());
        when(userManagementService.getUsers()).thenReturn(userDtoList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"id\":null,\"username\":null,\"firstName\":null,\"lastName\":null,\"email\":null,\"password\":null,\"roles"
                                        + "\":null}]"));
    }

    /**
     * Method under test: {@link UserController#createUser(UserDto)}
     */
    @Test
    void testCreateUser() throws Exception {
        // Arrange
        when(userManagementService.createUser(Mockito.<UserDto>any()))
                .thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(200)));

        UserDto userDto = new UserDto();
        userDto.setEmail("jane.doe@example.org");
        userDto.setFirstName("Jane");
        userDto.setId("42");
        userDto.setLastName("Doe");
        userDto.setPassword("iloveyou");
        userDto.setRoles(new ArrayList<>());
        userDto.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(userDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link UserController#updateUser(String, UserDto)}
     */
    @Test
    void testUpdateUser() throws Exception {
        // Arrange
        when(userManagementService.updateUser(Mockito.<String>any(), Mockito.<UserDto>any()))
                .thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(200)));

        UserDto userDto = new UserDto();
        userDto.setEmail("jane.doe@example.org");
        userDto.setFirstName("Jane");
        userDto.setId("42");
        userDto.setLastName("Doe");
        userDto.setPassword("iloveyou");
        userDto.setRoles(new ArrayList<>());
        userDto.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(userDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/user/{id}", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link UserController#deleteUser(String)}
     */
    @Test
    void testDeleteUser() throws Exception {
        // Arrange
        when(userManagementService.deleteUser(Mockito.<String>any()))
                .thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(200)));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/user/{id}", "42");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link UserController#assignRole(String, String)}
     */
    @Test
    void testAssignRole() throws Exception {
        // Arrange
        when(userManagementService.assignRole(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(200)));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/user/assign-role/{id}", "42")
                .param("roleName", "foo");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link UserController#unassignRole(String, String)}
     */
    @Test
    void testUnassignRole() throws Exception {
        // Arrange
        when(userManagementService.unassignRole(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(200)));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/user/unassign-role/{id}", "42")
                .param("roleName", "foo");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
