package com.ninegold.ninegoldapi.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninegold.ninegoldapi.entities.AuditableUserEntity;
import com.ninegold.ninegoldapi.entities.IdentifiableEntity;
import com.ninegold.ninegoldapi.entities.User;
import com.ninegold.ninegoldapi.exceptions.ConfigurationException;
import com.ninegold.ninegoldapi.security.CustomUserDetails;
import com.ninegold.ninegoldapi.security.UserAuthentication;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.Date;

/**
 * Helper static methods
 */
public class Helper {
    /**
     * Represents the utf8 encoding name.
     */
    public static final String UTF8 = "UTF-8";

    /**
     * The object mapper.
     */
    public static final ObjectMapper MAPPER = new Jackson2ObjectMapperBuilder()
            .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).build();

    /**
     * Check if the configuration is null or not.
     *
     * @param object the object
     * @param name the name
     * @throws ConfigurationException if the configuration is null
     */
    public static void checkConfigNotNull(Object object, String name) {
        if (object == null) {
            throw new ConfigurationException(name + " should be provided");
        }
    }

    /**
     * It checks whether a given string is valid email address.
     *
     * @param str the string to be checked
     * @return true if a given string is valid email address
     */
    public static boolean isEmail(String str) {
        return new EmailValidator().isValid(str, null);
    }

    /**
     * Check if the value is valid email.
     *
     * @param value the value to be checked
     * @param name the name of the value, used in the exception message
     * @throws IllegalArgumentException if the value is not email
     */
    public static void checkEmail(String value, String name) {
        checkNullOrEmpty(value, name);
        if (!isEmail(value)) {
            throw new IllegalArgumentException(name + " should be valid email address");
        }
    }

    /**
     * It checks whether a given string is null or empty.
     *
     * @param str the string to be checked
     * @param name the name of the string, used in the exception message
     * @throws IllegalArgumentException the exception thrown when the given string is null or empty
     */
    public static void checkNullOrEmpty(String str, String name) throws IllegalArgumentException {
        if (isNullOrEmpty(str)) {
            throw new IllegalArgumentException(name + " should be valid string(not null and not empty)");
        }
    }

    /**
     * It checks whether a given string is null or empty.
     *
     * @param str the string to be checked
     * @return true if a given string is null or empty
     * @throws IllegalArgumentException throws if string is null or empty
     */
    public static boolean isNullOrEmpty(String str) throws IllegalArgumentException {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Check if the configuration state is true.
     *
     * @param state the state
     * @param message the error message if the state is not true
     * @throws ConfigurationException if the state is not true
     */
    public static void checkConfigState(boolean state, String message) {
        if (!state) {
            throw new ConfigurationException(message);
        }
    }

    /**
     * It checks whether a given object is null.
     *
     * @param object the object to be checked
     * @param name the name of the object, used in the exception message
     * @throws IllegalArgumentException the exception thrown when the object is null
     */
    public static void checkNull(Object object, String name) throws IllegalArgumentException {
        if (object == null) {
            throw new IllegalArgumentException(name + " should be provided");
        }
    }

    /**
     * Get password encoder.
     *
     * @return the BC crypt password encoder
     */
    public static PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Encode password for user.
     * @param user the user entity.
     * @param isUpdating the updating flag.
     * @return the user with encrypted password field.
     */
    public static User encodePassword(User user, boolean isUpdating) {
        Helper.checkNull(user, "user");
        String rawPassword = user.getPassword();
        boolean checkPassword = !isUpdating || rawPassword != null;
        if (checkPassword) {
            Helper.checkNullOrEmpty(rawPassword, "user.password");
            PasswordEncoder encoder = getPasswordEncoder();
            user.setPassword(encoder.encode(rawPassword));
        }
        return user;
    }

    /**
     * Check if the value is positive.
     *
     * @param value the value to be checked
     * @param name the name of the value, used in the exception message
     * @throws IllegalArgumentException if the value is not positive
     */
    public static void checkPositive(long value, String name) {
        if (value <= 0) {
            throw new IllegalArgumentException(name + " should be positive");
        }
    }

    /**
     * Audit with entity with created by and createdOn, last modified on/by information.
     * @param entity the  entity
     * @param <T> the auditable entity
     */
    public static <T extends AuditableUserEntity> void audit(T entity)  {
        User user = getAuthUser();
        if (user != null) {
            Date now = new Date();
            entity.setCreatedOn(now);
            entity.setLastModifiedOn(now);
            entity.setCreatedBy(user.getId());
            entity.setLastModifiedBy(user.getId());
        }
    }

    /**
     * Check id and entity for update method.
     *
     * @param id the id of the entity to update
     * @param entity the entity to update
     * @param <T> the entity class
     * @throws IllegalArgumentException if id is not positive or entity is null or id of entity is not positive
     * or id of  entity not match id
     */
    public static <T extends IdentifiableEntity> void checkUpdate(long id, T entity) {
        checkPositive(id, "id");
        checkNull(entity, "entity");
        checkPositive(entity.getId(), "entity.id");
        if (entity.getId() != id) {
            throw new IllegalArgumentException("id and id of passed entity should be same");
        }
    }

    /**
     * Check if the configuration is positive or not.
     *
     * @param value the configuration  value
     * @param name the name
     * @throws ConfigurationException if the configuration value is  not positive
     */
    public static void checkConfigPositive(long value, String name) {
        if (value <= 0) {
            throw new ConfigurationException(name + " should be positive");
        }
    }

    /**
     * Get user from authentication.
     * @return user if exists valid user authentication otherwise null
     */
    public static User getAuthUser()  {
        User user = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof UserAuthentication) {
            UserAuthentication userAuth = (UserAuthentication) auth;
            user = (User) userAuth.getPrincipal();
        } else if (auth != null  && auth.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            user = userDetails.getUser();
        }
        return user;
    }

    /**
     * Build like predicate for string value.
     *
     * @param val the value
     * @param path the path
     * @param cb the criteria builder.
     * @return the match predicate
     */
    public static Predicate buildLike(String val, Path<String> path, CriteriaBuilder cb) {
        return cb.like(path, "%" + val + "%");
    }

    /**
     * Build like predicate for string value.
     *
     * @param val the value
     * @param pd the predicate
     * @param path the path
     * @param cb the criteria builder.
     * @return the match predicate
     */
    public static Predicate buildLikePredicate(String val, Predicate pd, Path<String> path, CriteriaBuilder cb) {
        if (!isNullOrEmpty(val)) {
            return cb.and(pd, buildLike(val, path, cb));
        }
        return pd;
    }

    /**
     * Build equal predicate for object value.
     *
     * @param val the value
     * @param pd the predicate
     * @param path the path
     * @param cb the criteria builder.
     * @return the match predicate
     */
    public static Predicate buildEqualPredicate(Object val, Predicate pd, Path<?> path, CriteriaBuilder cb) {
        if (val != null) {
            return cb.and(pd, cb.equal(path, val));
        }
        return pd;
    }
}
