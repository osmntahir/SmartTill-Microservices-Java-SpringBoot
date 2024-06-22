package com.toyota.usermanagementservice.dao;

import com.toyota.usermanagementservice.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing User entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Retrieves a page of users filtered by optional criteria.
     *
     * @param firstname The first name to filter by (can be null)
     * @param lastname  The last name to filter by (can be null)
     * @param email     The email address to filter by (can be null)
     * @param username  The username to filter by (can be null)
     * @param pageable  The pagination information
     * @return A page of users matching the specified criteria
     */
    @Query("SELECT u FROM User u WHERE " +
            "(:firstname is null or u.firstName like %:firstname%) and " +
            "(:lastname is null or u.lastName like %:lastname%) and " +
            "(:email is null or u.email like %:email%) and " +
            "(:username is null or u.username like %:username%)")
    Page<User> getUsersFiltered(String firstname, String lastname, String email, String username, Pageable pageable);

    /**
     * Checks if a user with the given username exists.
     *
     * @param username The username to check
     * @return True if a user with the username exists, false otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Checks if a user with the given email exists.
     *
     * @param email The email address to check
     * @return True if a user with the email exists, false otherwise
     */
    boolean existsByEmail(String email);
}
