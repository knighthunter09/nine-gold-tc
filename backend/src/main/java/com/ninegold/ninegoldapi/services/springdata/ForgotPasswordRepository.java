package com.ninegold.ninegoldapi.services.springdata;

import com.ninegold.ninegoldapi.entities.ForgotPassword;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The ForgotPassword repository.
 */
public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Long> {
    /**
     * This method is used to get the ForgotPassword by token.
     * @param token the reset password token
     * @return the forgot password
     */
    ForgotPassword findByToken(String token);


    /**
     * Delete all forgot passwords by user id.
     * @param userId the user id.
     */
    void deleteByUserId(long userId);
}

