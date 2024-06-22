package com.toyota.usermanagementservice.dao;

import com.toyota.usermanagementservice.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @Test
    public void testGetUsersFiltered() {
        // Test data
        String firstname = "John";
        String lastname = null;
        String email = "john.doe@example.com";
        String username = null;
        Pageable pageable = mock(Pageable.class);
        Page<User> mockPage = mock(Page.class);

        // Mock behavior
        when(userRepository.getUsersFiltered(eq(firstname), eq(lastname), eq(email), eq(username), eq(pageable)))
                .thenReturn(mockPage);

        // Call repository method
        Page<User> result = userRepository.getUsersFiltered(firstname, lastname, email, username, pageable);

        // Verify
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockPage, result);

        // Verify that getUsersFiltered method is called once
        verify(userRepository, times(1)).getUsersFiltered(firstname, lastname, email, username, pageable);
    }

    @Test
    public void testExistsByUsername() {
        // Test data
        String username = "johndoe";

        // Mock behavior
        when(userRepository.existsByUsername(username))
                .thenReturn(true);

        // Call repository method
        boolean result = userRepository.existsByUsername(username);

        // Verify
        Assertions.assertTrue(result);

        // Verify that existsByUsername method is called once
        verify(userRepository, times(1)).existsByUsername(username);
    }

    @Test
    public void testExistsByEmail() {
        // Test data
        String email = "john.doe@example.com";

        // Mock behavior
        when(userRepository.existsByEmail(email))
                .thenReturn(true);

        // Call repository method
        boolean result = userRepository.existsByEmail(email);

        // Verify
        Assertions.assertTrue(result);

        // Verify that existsByEmail method is called once
        verify(userRepository, times(1)).existsByEmail(email);
    }
}
