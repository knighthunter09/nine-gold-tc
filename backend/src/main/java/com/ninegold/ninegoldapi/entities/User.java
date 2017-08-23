package com.ninegold.ninegoldapi.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.InheritanceType.JOINED;
import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * The user.
 */
@Getter
@Setter
@Entity
@Table(name = "`user`")
@Inheritance(strategy = JOINED)
public class User extends AuditableEntity {
    /**
     * The password (hashed).
     */
    @JsonProperty(access = WRITE_ONLY)
    private String password;

    /**
     * The first name.
     */
    private String firstName;

    /**
     * The last name.
     */
    private String lastName;

    /**
     * The email.
     */
    private String email;

    /**
     * The Stripe Customer Id
     */
    private String customerId;

    /**
     * Expires in millis.
     */
    @Transient
    @JsonInclude(NON_NULL)
    private Long expires;


    @Column(name = "is_terminated")
    private boolean isTerminated;

    /**
     * The subscriptions.
     */
    @OneToMany(mappedBy = "userId", cascade = ALL)
    private List<UserSubscription> subscriptions;

    /**
     * The last login date.
     */
    @Temporal(TIMESTAMP)
    private Date lastLoginOn;
}

