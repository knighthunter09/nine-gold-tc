package com.ninegold.ninegoldapi.services.springdata;

import com.ninegold.ninegoldapi.services.IActor;
import com.ninegold.ninegoldapi.services.IActorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ActorFactory implements IActorFactory {

    @Autowired
    private ApplicationContext applicationContext;

    private static final Map<String, String> EVENT_ACTOR_MAP = new HashMap<String, String>() {{
        put("invoice.payment_succeeded", "paymentSuccessActor");
        put("invoice.payment_failed", "paymentFailureActor");
    }};

    public IActor getActor(String eventType) {
        return EVENT_ACTOR_MAP.get(eventType) != null ? (IActor) applicationContext.getBean(EVENT_ACTOR_MAP.get(eventType)) : null;
    }
}
