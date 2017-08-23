package com.ninegold.ninegoldapi.services.springdata;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninegold.ninegoldapi.controllers.BaseEmailController;
import com.ninegold.ninegoldapi.entities.User;
import com.ninegold.ninegoldapi.exceptions.NineGoldException;
import com.ninegold.ninegoldapi.services.IActor;
import com.ninegold.ninegoldapi.services.StripeService;
import com.ninegold.ninegoldapi.services.UserService;
import com.stripe.exception.*;
import com.stripe.model.Event;
import com.stripe.model.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Actor to handle payment failures from Stripe
 */
@Service
public class PaymentFailureActor extends BaseEmailController implements IActor {

    @Autowired
    private UserService userService;

    @Autowired
    private StripeService stripeService;

    @Value("${payment.failure.terminate.account}")
    private boolean terminateAccountOnPaymentFailure;

    @Override
    public void handleEvent(Event event) throws IOException, CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException, NineGoldException {
        String eventDataJson = event.getData().getObject().toJson();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonObj = objectMapper.readTree(eventDataJson);
        String customerId = jsonObj.get("customer").asText();
        User user = userService.findByCustomerId(customerId);
        if (user != null && user.getSubscriptions().size() > 0) {
            Subscription subscription = stripeService.retrieveSubscription(user);
            Map<String, Object> model = new HashMap<String, Object>(){{
                put("amount", subscription.getPlan().getAmount() + " " + subscription.getPlan().getCurrency());
            }};
            sendEmail(user.getEmail(), "paymentFailure", model);

            if (terminateAccountOnPaymentFailure) {
                userService.delete(user.getId());
            }
        }
    }
}
