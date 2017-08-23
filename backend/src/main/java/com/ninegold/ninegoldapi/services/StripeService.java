package com.ninegold.ninegoldapi.services;

import com.ninegold.ninegoldapi.entities.User;
import com.ninegold.ninegoldapi.entities.UserSubscription;
import com.ninegold.ninegoldapi.services.springdata.StripeRegistrationResponse;
import com.stripe.exception.*;
import com.stripe.model.Customer;
import com.stripe.model.Plan;
import com.stripe.model.Subscription;

import java.util.List;

public interface StripeService {

    StripeRegistrationResponse registerCustomer(String planId, User user) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException;

    void cancelSubscriptions(User user, List<UserSubscription> userSubscriptions) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException;

    Customer createCustomer(User user) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException;

    Subscription retrieveSubscription(User user) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException;

    Plan createPlan() throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException;

    Subscription createSubscription(Plan plan, Customer customer) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException;

    Customer updatePayment(User user, String token) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException;

}
