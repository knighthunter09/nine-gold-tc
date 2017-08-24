package com.ninegold.ninegoldapi.services.springdata;

import com.ninegold.ninegoldapi.entities.User;
import com.ninegold.ninegoldapi.entities.UserSubscription;
import com.ninegold.ninegoldapi.services.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Customer;
import com.stripe.model.Plan;
import com.stripe.model.Subscription;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StripeServiceImpl implements StripeService {

    /**
     * Api Key of Stripe
     */
    @Value("${stripe.apiKey}")
    private String apiKey;

    @Value("${stripe.plan.name}")
    private String planName;

    @Value("${stripe.plan.type}")
    private String planType;

    @Value("${stripe.plan.interval}")
    private String planInterval;

    @Value("${stripe.plan.currency}")
    private String planCurrency;

    @Value("${stripe.plan.amount}")
    private Long planAmount;

    @PostConstruct
    public void init() {
        Stripe.apiKey = apiKey;
    }

    public StripeRegistrationResponse registerCustomer(String planId, User user) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
        Plan plan = Plan.retrieve(planId);
        Customer customer = createCustomer(user);
        Subscription subscription = createSubscription(plan, customer);
        return new StripeRegistrationResponse(plan, customer, subscription);
    }

    public Customer createCustomer(User user) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("email", user.getEmail());

        return Customer.create(params);
    }

    public Subscription retrieveSubscription(User user) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
        UserSubscription userSubscription = user.getSubscriptions().get(0);
        return Subscription.retrieve(userSubscription.getSubscriptionId());
    }

    public Plan createPlan() throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", planName);
        params.put("id", planType);
        params.put("interval", planInterval);
        params.put("currency", planCurrency);
        params.put("amount", planAmount);

        return Plan.create(params);
    }

    public Subscription createSubscription(Plan plan, Customer customer) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("customer", customer.getId());
        params.put("plan", plan.getId());

        return Subscription.create(params);
    }

    @Override
    public Customer updatePayment(User user, String token) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
        String customerId = user.getCustomerId();
        Customer customer = Customer.retrieve(customerId);
        Map<String, Object> updateParams = new HashMap<>();
        updateParams.put("source", token);
        return customer.update(updateParams);
    }

    @Override
    public void cancelSubscriptions(User user, List<UserSubscription> userSubscriptions) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
        String customerId = user.getCustomerId();
        Customer customer = Customer.retrieve(customerId);
        for (UserSubscription s : userSubscriptions) {
            Subscription subscription = customer.getSubscriptions().retrieve(s.getSubscriptionId());
            subscription.cancel(null);
        }
    }
}
