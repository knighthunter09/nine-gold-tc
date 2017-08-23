package com.ninegold.ninegoldapi.controllers;

import com.ninegold.ninegoldapi.entities.*;
import com.ninegold.ninegoldapi.exceptions.AccessDeniedException;
import com.ninegold.ninegoldapi.exceptions.ConfigurationException;
import com.ninegold.ninegoldapi.exceptions.EntityNotFoundException;
import com.ninegold.ninegoldapi.exceptions.NineGoldException;
import com.ninegold.ninegoldapi.services.StripeService;
import com.ninegold.ninegoldapi.services.UserService;
import com.ninegold.ninegoldapi.services.UserSubscriptionService;
import com.ninegold.ninegoldapi.services.springdata.StripeRegistrationResponse;
import com.ninegold.ninegoldapi.utils.Helper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The User REST controller. Is effectively thread safe.
 */
@RestController
@RequestMapping("/users")
@NoArgsConstructor
public class UserController extends BaseEmailController {
    /**
     * The user service used to perform operations. Should be non-null after injection.
     */
    @Autowired
    private UserService userService;

    /**
     * The stripe service used to perform stripe related operations. Should be
     * not-null after injection.
     */
    @Autowired
    private StripeService stripeService;


    @Autowired
    private AuthController authController;

    @Autowired
    private UserSubscriptionService userSubscriptionService;

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        super.checkConfiguration();
        Helper.checkConfigNotNull(userService, "userService");
        Helper.checkConfigNotNull(stripeService, "stripeService");
    }


    /**
     * This method is used to retrieve an entity.
     *
     * @param id the id of the entity to retrieve
     * @return the match entity
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws NineGoldException if any other error occurred during operation
     */
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public User get(@PathVariable long id) throws NineGoldException {
        return userService.get(id);
    }

    /**
     * This method is used to create an entity.
     *
     * @param entity the entity to create
     * @return the created entity
     * @throws IllegalArgumentException if entity is null or not valid
     * @throws NineGoldException if any other error occurred during operation
     */
    @Transactional
    @RequestMapping(value = "{planId}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@PathVariable String planId, @RequestBody User entity) throws NineGoldException {
        User user = null;
        try {
            StripeRegistrationResponse stripeRegistrationResponse = stripeService.registerCustomer(planId, entity);
            UserSubscription subscription = new UserSubscription();
            subscription.setUser(entity);
            subscription.setSubscriptionId(stripeRegistrationResponse.getSubscription().getId());
            subscription.setSubscriptionType(stripeRegistrationResponse.getPlan().getId());
            entity.setSubscriptions(Collections.singletonList(subscription));
            entity.setCustomerId(stripeRegistrationResponse.getCustomer().getId());
            user = userService.create(entity);
        } catch (Exception e) {
            throw new NineGoldException(e.getMessage());
        }
        return user;
    }

    /**
     * This method is used to update an entity.
     *
     * @param id the id of the entity to update
     * @param entity the entity to update
     * @return the updated entity
     * @throws IllegalArgumentException if id is not positive or entity is null or id of entity is not positive
     * or id of  entity not match id or entity is invalid
     * @throws EntityNotFoundException if the entity does not exist
     * @throws NineGoldException if any other error occurred during operation
     */
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @Transactional
    public User update(@PathVariable long id, @RequestBody User entity) throws NineGoldException {
        return userService.update(id, entity);
    }

    /**
     * This method is used to delete an entity.
     *
     * @throws NineGoldException if any other error occurred during operation
     */
    @Transactional
    @RequestMapping(value = "terminate", method = RequestMethod.DELETE)
    public void delete(HttpServletRequest request, HttpServletResponse response) throws NineGoldException {
        try {
            User user = Helper.getAuthUser();
            List<UserSubscription> subscriptions = userSubscriptionService.findByUserId(user.getId());
            stripeService.cancelSubscriptions(user, subscriptions);
            userService.delete(user.getId());

            //force logout once account is terminated
            authController.logout(request, response);
        } catch (Exception e) {
            throw new NineGoldException(e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "me")
    public User getMe() throws NineGoldException {
        return userService.getMe();
    }

    /**
     * This method is used to start the forgot password process.
     *
     * @param email the user email.
     * @throws IllegalArgumentException if email is null or empty or not email address
     * @throws EntityNotFoundException if the entity does not exist
     * @throws AccessDeniedException if does not allow to perform action
     * @throws NineGoldException if any other error occurred during operation
     */
    @Transactional(rollbackOn = NineGoldException.class)
    @RequestMapping(value = "forgotPassword", method = RequestMethod.PUT)
    public void forgotPassword(@RequestParam(value = "email") String email) throws NineGoldException {
        Helper.checkEmail(email, "email");
        UserSearchCriteria criteria = new UserSearchCriteria();
        criteria.setEmail(email);
        List<User> users = userService.search(criteria);
        if (users.size() == 0) {
            throw new EntityNotFoundException("No user found with email " + email);
        }
        User user = users.get(0);
        long userId = user.getId();
        ForgotPassword forgotPassword = userService.forgotPassword(userId);
        Map<String, Object> model = new HashMap<>();
        model.put("user", user);
        model.put("forgotPassword", forgotPassword);
        sendEmail(email, "forgotPassword", model);
    }


    /**
     * This method is used to update the password.
     *
     * @param newPassword the new password request
     * @return true if update the password successfully otherwise false
     * @throws IllegalArgumentException if newPassword is null or invalid
     * @throws NineGoldException if any other error occurred during operation
     */
    @Transactional
    @RequestMapping(value = "updatePassword", method = RequestMethod.PUT)
    public boolean updatePassword(@RequestBody NewPassword newPassword) throws NineGoldException {
        return userService.updatePassword(newPassword);
    }

    @Transactional
    @RequestMapping(value = "updatePayment", method = RequestMethod.PUT)
    public boolean updatePayment(@RequestBody StripToken token) throws NineGoldException {
        User currentUser = Helper.getAuthUser();
        try {
            stripeService.updatePayment(currentUser, token.getToken());
        } catch (Exception e) {
            throw new NineGoldException(e.getMessage());
        }
        return true;
    }

}
