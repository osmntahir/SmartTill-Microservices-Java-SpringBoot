package com.toyota.usermanagementservice.dao;

import com.toyota.usermanagementservice.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE " +
            "(:firstname is null or u.firstName like %:firstname%) and " +
            "(:lastname is null or u.lastName like %:lastname%) and " +
            "(:email is null or u.email like %:email%) and " +
            "(:username is null or u.username like %:username%)")
    Page<User> getUsersFiltered(String firstname, String lastname, String email, String username, Pageable pageable);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
