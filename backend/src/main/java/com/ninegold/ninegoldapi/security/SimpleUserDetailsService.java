package com.ninegold.ninegoldapi.security;


import com.ninegold.ninegoldapi.entities.User;
import com.ninegold.ninegoldapi.entities.UserSearchCriteria;
import com.ninegold.ninegoldapi.exceptions.ConfigurationException;
import com.ninegold.ninegoldapi.services.UserService;
import com.ninegold.ninegoldapi.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The simple implementation of the UserDetailsService.
 */
@Service
public class SimpleUserDetailsService implements UserDetailsService {
    /**
     * The UserService to load user by username.
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
     * Locates the user based on the username.
     *
     * @param email the user email.
     * @return the UserDetails
     * @throws UsernameNotFoundException if there is no match or invalid user found.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            UserSearchCriteria criteria = new UserSearchCriteria();
            criteria.setEmail(email);
            List<User> users = userService.search(criteria);
            if (users.size() == 0) {
                throw new UsernameNotFoundException("No user found with username " + email);
            }
            User user = users.get(0);
            List<GrantedAuthority> authorities = buildUserAuthority(user);
            return buildUserForAuthentication(user, authorities);
        } catch (UsernameNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new UsernameNotFoundException("Failed to get user data", e);
        }
    }

    /**
     * Build the user authority.
     *
     * @param user
     * @return the authority
     */
    private List<GrantedAuthority> buildUserAuthority(User user) {
        //setting roles of every user to "SYSTEM_ADMIN" for now.
        List<String> roles = new ArrayList<String>(){{ add("ROLE_USER"); }};

        Set<GrantedAuthority> auths = new HashSet<>();
        for (String role : roles) {
            auths.add(new SimpleGrantedAuthority(role));
        }
        return new ArrayList<>(auths);
    }

    /**
     * Build the user details entity.
     *
     * @param user the user
     * @param authorities the authorities
     * @return user details entity
     */
    private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        // will check terminated/non-terminated status here
        return new CustomUserDetails(user, !user.isTerminated(), true, true, true, authorities);
    }
}
