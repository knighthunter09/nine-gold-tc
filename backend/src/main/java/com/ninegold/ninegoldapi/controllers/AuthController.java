package com.ninegold.ninegoldapi.controllers;

import com.ninegold.ninegoldapi.entities.User;
import com.ninegold.ninegoldapi.entities.UserSearchCriteria;
import com.ninegold.ninegoldapi.exceptions.AccessDeniedException;
import com.ninegold.ninegoldapi.exceptions.ConfigurationException;
import com.ninegold.ninegoldapi.exceptions.NineGoldException;
import com.ninegold.ninegoldapi.services.UserService;
import com.ninegold.ninegoldapi.utils.Helper;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * The login REST controller. Is effectively thread safe.
 */
@RestController
@RequestMapping("/auth")
@NoArgsConstructor
public class AuthController {

    /**
     * The user service used to perform operations. Should be non-null after injection.
     */
    @Autowired
    private UserService userService;


    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        Helper.checkConfigNotNull(userService, "userService");
    }

    /**
     * This method is used to login with basic auth and return JWT token with expired time.
     *
     * @return the User entity
     * @throws AccessDeniedException if does not allow to perform action
     * @throws NineGoldException if any other error occurred during operation
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public User login() throws NineGoldException {
        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();
        UserSearchCriteria criteria = new UserSearchCriteria();
        criteria.setEmail(authentication.getName());
        List<User> users = userService.search(criteria);
        // validate valid user exists in SimpleUserDetailsService already
        User user = users.get(0);
        User target = new User();
        BeanUtils.copyProperties(user, target);
        target.setLastLoginOn(new Date());
        target.setPassword(null);
        user = userService.update(user.getId(), target);
        return user;
    }

    @RequestMapping(value="logout", method = RequestMethod.GET)
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
    }
}
