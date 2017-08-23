package com.ninegold.ninegoldapi.controllers;

import com.ninegold.ninegoldapi.exceptions.NineGoldException;
import com.ninegold.ninegoldapi.services.IActor;
import com.ninegold.ninegoldapi.services.IActorFactory;
import com.ninegold.ninegoldapi.services.StripeService;
import com.stripe.model.Event;
import com.stripe.model.Plan;
import com.stripe.model.StripeObject;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

/**
 * Controller for Stripe related activities
 */
@RestController
@RequestMapping("/stripe")
@NoArgsConstructor
public class StripeController {

    @Autowired
    private StripeService stripeService;

    @Autowired
    private IActorFactory actorFactory;

    /**
     * API to create a plan
     * @return the plan Id
     * @throws NineGoldException
     */
    @RequestMapping(value = "plan", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String createPlan() throws NineGoldException {
        try {
            Plan plan = stripeService.createPlan();
            return plan.getId();
        } catch (Exception e) {
            throw new NineGoldException(e.getMessage());
        }
    }

    /**
     * API to handle webhooks from stripe
     * @param request
     * @param response
     * @throws NineGoldException
     */
    @RequestMapping(value = "handleEvent", method = RequestMethod.POST)
    public void handleEvent(HttpServletRequest request, HttpServletResponse response) throws NineGoldException {
        try {
            String jsonStr = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            Event event = StripeObject.PRETTY_PRINT_GSON.fromJson(jsonStr, Event.class);
            IActor actor = actorFactory.getActor(event.getType());
            if (actor != null) {
                actor.handleEvent(event);
            }
        } catch (Exception e) {
            throw new NineGoldException(e.getMessage());
        }
    }
}
