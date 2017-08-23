package com.ninegold.ninegoldapi.services.springdata;

import com.ninegold.ninegoldapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The User repository.
 */
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findByCustomerId(String customerId);
}

