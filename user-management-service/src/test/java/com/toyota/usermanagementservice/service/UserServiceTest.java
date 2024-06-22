package com.toyota.usermanagementservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.toyota.usermanagementservice.dao.UserRepository;
import com.toyota.usermanagementservice.domain.Role;
import com.toyota.usermanagementservice.domain.User;
import com.toyota.usermanagementservice.dto.UserDto;
import com.toyota.usermanagementservice.dto.UserResponse;
import com.toyota.usermanagementservice.exception.*;
import com.toyota.usermanagementservice.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Test class for UserService.
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto userDto;
    private User user;

    /**
     * Sets up the test data before each test method.
     */
    @BeforeEach
    public void setUp() {
        userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setUsername("testuser");
        userDto.setFirstName("Test");
        userDto.setLastName("User");

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setUsername("testuser");
        user.setFirstName("Test");
        user.setLastName("User");
    }

    /**
     * Tests if an InvalidEmailFormatException is thrown for invalid email format.
     */
    @Test
    public void testCreateUser_InvalidEmailFormat() {
        userDto.setEmail("invalidemail");

        InvalidEmailFormatException exception = assertThrows(InvalidEmailFormatException.class, () -> {
            userService.createUser(userDto);
        });

        assertEquals("Invalid email format: invalidemail", exception.getMessage());
    }

    /**
     * Tests if an EmailAlreadyExistsException is thrown for existing email.
     */
    @Test
    public void testCreateUser_EmailAlreadyExists() {
        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(true);

        EmailAlreadyExistsException exception = assertThrows(EmailAlreadyExistsException.class, () -> {
            userService.createUser(userDto);
        });

        assertEquals("Email already exists: test@example.com", exception.getMessage());
    }

    /**
     * Tests if a UsernameAlreadyExistsException is thrown for existing username.
     */
    @Test
    public void testCreateUser_UsernameAlreadyExists() {
        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(userDto.getUsername())).thenReturn(true);

        UsernameAlreadyExistsException exception = assertThrows(UsernameAlreadyExistsException.class, () -> {
            userService.createUser(userDto);
        });

        assertEquals("Username already exists: testuser", exception.getMessage());
    }

    /**
     * Tests if a user is created successfully.
     */
    @Test
    public void testCreateUser_Success() {
        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(userDto.getUsername())).thenReturn(false);

        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto createdUser = userService.createUser(userDto);

        assertEquals(userDto.getEmail(), createdUser.getEmail());
        assertEquals(userDto.getUsername(), createdUser.getUsername());
    }

    /**
     * Tests if a UserNotFoundException is thrown when the user is not found.
     */
    @Test
    public void testUpdateUser_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.updateUser(1L, userDto);
        });

        assertEquals("User not found", exception.getMessage());
    }

    /**
     * Tests if a UsernameAlreadyExistsException is thrown when the username already exists.
     */
    @Test
    public void testUpdateUser_UsernameAlreadyExists() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        userDto.setUsername("newuser");

        UsernameAlreadyExistsException exception = assertThrows(UsernameAlreadyExistsException.class, () -> {
            userService.updateUser(1L, userDto);
        });

        assertEquals("Username is already taken: newuser", exception.getMessage());
    }

    /**
     * Tests if an EmailAlreadyExistsException is thrown when the email already exists.
     */
    @Test
    public void testUpdateUser_EmailAlreadyExists() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        userDto.setEmail("newemail@example.com");

        EmailAlreadyExistsException exception = assertThrows(EmailAlreadyExistsException.class, () -> {
            userService.updateUser(1L, userDto);
        });

        assertEquals("Email address is already registered: newemail@example.com", exception.getMessage());
    }

    /**
     * Tests if an InvalidEmailFormatException is thrown when the email format is invalid.
     */
    @Test
    public void testUpdateUser_InvalidEmailFormat() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        userDto.setEmail("invalidemail");

        InvalidEmailFormatException exception = assertThrows(InvalidEmailFormatException.class, () -> {
            userService.updateUser(1L, userDto);
        });

        assertEquals("Invalid email address format: invalidemail", exception.getMessage());
    }

    /**
     * Tests if a user is updated successfully.
     */
    @Test
    public void testUpdateUser_Success() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        userDto.setUsername("newuser");
        userDto.setEmail("newemail@example.com");

        UserDto updatedUser = userService.updateUser(1L, userDto);

        assertEquals(userDto.getEmail(), updatedUser.getEmail());
        assertEquals(userDto.getUsername(), updatedUser.getUsername());
        assertEquals(userDto.getFirstName(), updatedUser.getFirstName());
        assertEquals(userDto.getLastName(), updatedUser.getLastName());

        verify(userRepository, times(1)).save(any(User.class));
    }

    /**
     * Tests if a UserNotFoundException is thrown when the user is not found.
     */
    @Test
    public void testGetUser_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getUser(1L);
        });

        assertEquals("User not found", exception.getMessage());
    }

    /**
     * Tests if a user is retrieved successfully.
     */
    @Test
    public void testGetUser_Success() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        UserDto retrievedUser = userService.getUser(1L);

        assertEquals(user.getId(), retrievedUser.getId());
        assertEquals(user.getEmail(), retrievedUser.getEmail());
        assertEquals(user.getUsername(), retrievedUser.getUsername());
        assertEquals(user.getFirstName(), retrievedUser.getFirstName());
        assertEquals(user.getLastName(), retrievedUser.getLastName());

        verify(userRepository, times(1)).findById(anyLong());
    }

    /**
     * Tests if a UserNotFoundException is thrown when the user is not found during delete operation.
     */
    @Test
    public void testDeleteUser_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUser(1L);
        });

        assertEquals("User not found with ID: 1", exception.getMessage());
    }

    /**
     * Tests if a user is marked as deleted successfully.
     */
    @Test
    public void testDeleteUser_Success() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        assertTrue(user.isDeleted());
        verify(userRepository, times(1)).save(user);
    }

    /**
     * Tests the successful addition of a role to a user.
     *
     * @throws UserNotFoundException      If the user with the given ID is not found.
     * @throws RoleAlreadyExistsException If the user already has the specified role.
     */
    @Test
    public void testAddRole_Success() {
        // Mock data
        Long userId = 1L;
        Role role = Role.CASHIER;
        Set<Role> roles = new HashSet<>(Collections.singleton(Role.ADMIN));
        User user = new User(userId, "testuser", "Test",
                "User", "test@example.com", false, roles);

        // Mock behavior
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call the method
        UserResponse response = userService.addRole(userId, role);

        // Verify the results
        assertNotNull(response);
        assertEquals(2, response.getRole().size());
        assertTrue(response.getRole().contains(role));
    }

    /**
     * Tests the scenario where the user is not found during role addition.
     *
     * @throws UserNotFoundException If the user with the given ID is not found (expected).
     */
    @Test
    public void testAddRole_UserNotFound() {
        Long userId = 1L;
        Role role = Role.ADMIN;

        // Mock behavior
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Call the method and expect UserNotFoundException
        assertThrows(UserNotFoundException.class, () -> userService.addRole(userId, role));
    }

    /**
     * Tests the scenario where the role to be added already exists for the user.
     *
     * @throws UserNotFoundException      If the user with the given ID is not found.
     * @throws RoleAlreadyExistsException If the user already has the specified role (expected).
     */
    @Test
    public void testAddRole_RoleAlreadyExists() {
        Long userId = 1L;
        Role role = Role.ADMIN;
        User user = new User(userId, "testuser", "Test",
                "User", "test@example.com", false, Collections.singleton(Role.ADMIN));

        // Mock behavior
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Call the method and expect RoleAlreadyExistsException
        assertThrows(RoleAlreadyExistsException.class, () -> userService.addRole(userId, role));
    }

    /**
     * Tests the removeRole method in the UserService class.
     * <p>
     * This test checks that a role is successfully removed from a user
     * who has multiple roles.
     */
    @Test
    public void testRemoveRole_Success() {
        // Mock data
        Long userId = 1L;
        Role roleToRemove = Role.ADMIN;
        Role otherRole = Role.CASHIER;
        User user = new User(userId, "testuser", "Test",
                "User", "test@example.com", false, new HashSet<>(Arrays.asList(roleToRemove, otherRole)));

        // Mock behavior
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call the method
        UserResponse response = userService.removeRole(userId, roleToRemove);

        // Verify the results
        assertNotNull(response);
        assertEquals(1, response.getRole().size());
        assertFalse(response.getRole().contains(roleToRemove));
        assertTrue(response.getRole().contains(otherRole));
    }

    /**
     * Tests that an exception is thrown when trying to remove a role
     * that the user does not have.
     *
     * @throws UserNotFoundException If the user with the given ID is not found.
     * @throws RoleNotFoundException If the user does not have the specified role.
     */
    @Test
    public void testRemoveRole_RoleNotPresent() {
        // Mock data
        Long userId = 1L;
        Role roleToRemove = Role.CASHIER; // The role that the user does not have
        User user = new User(userId, "testuser", "Test",
                "User", "test@example.com", false,
                new HashSet<>(Arrays.asList(Role.ADMIN, Role.STORE_MANAGER)));

        // Mock behavior
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Call the method and expect an exception
        assertThrows(RoleNotFoundException.class, () -> {
            userService.removeRole(userId, roleToRemove);
        });

    }




    /**
     * Tests the scenario where the user is not found during role removal.
     *
     * @throws UserNotFoundException If the user with the given ID is not found (expected).
     */
    @Test
    public void testRemoveRole_UserNotFound() {
        Long userId = 1L;
        Role role = Role.ADMIN;

        // Mock behavior
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Call the method and expect UserNotFoundException
        assertThrows(UserNotFoundException.class, () -> userService.removeRole(userId, role));
    }

    /**
     * Tests the scenario where the role to be removed does not exist for the user.
     *
     * @throws UserNotFoundException If the user with the given ID is not found.
     * @throws RoleNotFoundException If the user does not have the specified role (expected).
     */
    @Test
    public void testRemoveRole_RoleNotFound() {
        Long userId = 1L;
        Role role = Role.CASHIER;
        User user = new User(userId, "testuser",
                "Test", "User", "test@example.com",
                false, Collections.singleton(Role.ADMIN));

        // Mock behavior
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Call the method and expect SingleRoleRemovalException
        assertThrows(SingleRoleRemovalException.class, () -> userService.removeRole(userId, role));
    }

    /**
     * Tests the scenario where the user has only one role and cannot remove it.
     *
     * @throws UserNotFoundException If the user with the given ID is not found.
     * @throws RoleNotFoundException If the user does not have the specified role.
     * @throws SingleRoleRemovalException If the user has only one role and cannot remove it (expected).
     */
    @Test
    public void testRemoveRole_SingleRoleRemoval() {
        Long userId = 1L;
        Role role = Role.ADMIN;
        User user = new User(userId, "testuser", "Test",
                "User", "test@example.com",
                false, Collections.singleton(Role.ADMIN));

        // Mock behavior
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Call the method and expect SingleRoleRemovalException
        assertThrows(SingleRoleRemovalException.class, () -> userService.removeRole(userId, role));
    }

    @Test
    public void testGetAll() {
        // Prepare test data

        // Mock dependencies

        // Define input parameters for testing
        String firstname = "John";
        String lastname = null;
        String username = null;
        String email = "john@example.com";
        int page = 0;
        int size = 10;
        List<String> sortList = Arrays.asList("firstname");
        String sortOrder = "ASC";

        // Mock expected results from userRepository.getUsersFiltered()
        List<User> userListAsc = Arrays.asList(
                new User(1L, "John", "John", "john.doe@example.com", "johndoe", false, Collections.singleton(Role.ADMIN)),
                new User(2L, "Smith", "Smith", "smith.doe@example.com", "smithdoe", false, Collections.singleton(Role.CASHIER))
        );
        Page<User> mockedPageAsc = new PageImpl<>(userListAsc);

        List<User> userListDesc = Arrays.asList(
                new User(2L, "Smith", "Smith", "smith.doe@example.com", "smithdoe", false, Collections.singleton(Role.CASHIER)),
                new User(1L, "John", "John", "john.doe@example.com", "johndoe", false, Collections.singleton(Role.ADMIN))
        );
        Page<User> mockedPageDesc = new PageImpl<>(userListDesc);

        // Mock userRepository behavior for ascending order
        when(userRepository.getUsersFiltered(eq(firstname), eq(lastname), eq(email), eq(username), any(Pageable.class)))
                .thenReturn(mockedPageAsc);

        // Call the service method for ascending order
        Page<UserResponse> resultPageAsc = userService.getAll(firstname, lastname, username, email, page, size, sortList, sortOrder);

        // Assertions for ascending order
        assertEquals(2, resultPageAsc.getTotalElements());
        assertEquals("John", resultPageAsc.getContent().get(0).getFirstname());
        assertEquals("Smith", resultPageAsc.getContent().get(1).getFirstname());

        // Verify interactions for ascending order
        verify(userRepository, times(1)).getUsersFiltered(eq(firstname), eq(lastname), eq(email), eq(username), any(Pageable.class));

        // Mock userRepository behavior for descending order
        sortOrder = "DESC";
        when(userRepository.getUsersFiltered(eq(firstname), eq(lastname), eq(email), eq(username), any(Pageable.class)))
                .thenReturn(mockedPageDesc);

        // Call the service method for descending order
        Page<UserResponse> resultPageDesc = userService.getAll(firstname, lastname, username, email, page, size, sortList, sortOrder);

        // Assertions for descending order
        assertEquals(2, resultPageDesc.getTotalElements());
        assertEquals("Smith", resultPageDesc.getContent().get(0).getFirstname());
        assertEquals("John", resultPageDesc.getContent().get(1).getFirstname());

        // Verify interactions for descending order
        verify(userRepository, times(2)).getUsersFiltered(eq(firstname), eq(lastname), eq(email), eq(username), any(Pageable.class));
    }













}

