package com.ninegold.ninegoldapi.entities;

import lombok.Getter;
import lombok.Setter;


/**
 * The user search criteria.
 */
@Getter
@Setter
public class UserSearchCriteria {
    /**
     * The name.
     */
    private String name;

    /**
     * The email.
     */
    private String email;
}

