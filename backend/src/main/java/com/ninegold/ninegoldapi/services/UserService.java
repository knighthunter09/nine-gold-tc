package com.ninegold.ninegoldapi.services;


import com.ninegold.ninegoldapi.entities.ForgotPassword;
import com.ninegold.ninegoldapi.entities.NewPassword;
import com.ninegold.ninegoldapi.entities.User;
import com.ninegold.ninegoldapi.entities.UserSearchCriteria;
import com.ninegold.ninegoldapi.exceptions.AccessDeniedException;
import com.ninegold.ninegoldapi.exceptions.EntityNotFoundException;
import com.ninegold.ninegoldapi.exceptions.NineGoldException;

/**
 * The user service. Extends generic service interface.Implementation should be effectively thread-safe.
 */
public interface UserService extends GenericService<User, UserSearchCriteria> {

    /**
     * This method is used to create the forgot password entity for the given user.
     *
     * @param userId the user id.
     * @return the created forgot password entity
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws AccessDeniedException if does not allow to perform action
     * @throws NineGoldException if any other error occurred during operation
     */
    ForgotPassword forgotPassword(long userId) throws NineGoldException;

    /**
     * This method is used to update the forgot password entity for the given token.
     *
     * @param newPassword the newPassword request.
     * @return true if update the password successfully otherwise false
     * @throws IllegalArgumentException if newPassword is null or invalid
     * @throws NineGoldException if any other error occurred during operation
     */
    boolean updatePassword(NewPassword newPassword) throws NineGoldException;


    /**
     * Gets my profile.
     * @return my profile content.
     * @throws NineGoldException if there are any errors.
     */
    User getMe() throws NineGoldException;

    User findByCustomerId(String customerId) throws NineGoldException;
}

