package com.ninegold.ninegoldapi.services.springdata;

import com.stripe.model.Customer;
import com.stripe.model.Plan;
import com.stripe.model.Subscription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Encapsulated response of plan, customer and subscription of Stripe
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StripeRegistrationResponse {
    private Plan plan;

    private Customer customer;

    private Subscription subscription;
}
