package com.ninegold.ninegoldapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Entity to hold the Subscriptions of Stripe customer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_subscription")
public class UserSubscription extends IdentifiableEntity {

    /**
     * The user id.
     */
    @Column(name = "user_id", insertable = false, updatable = false)
    private long userId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "subscription_id")
    private String subscriptionId;

    @Column(name = "subscription_type")
    private String subscriptionType;
}
