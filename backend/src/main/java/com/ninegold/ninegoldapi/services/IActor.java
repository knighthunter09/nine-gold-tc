package com.ninegold.ninegoldapi.services;

import com.ninegold.ninegoldapi.exceptions.NineGoldException;
import com.stripe.exception.*;
import com.stripe.model.Event;

import java.io.IOException;

/**
 * Interface to handle various events from webhooks
 */
public interface IActor {
    void handleEvent(Event event) throws IOException, CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException, NineGoldException;
}
