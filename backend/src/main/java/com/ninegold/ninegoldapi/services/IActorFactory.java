package com.ninegold.ninegoldapi.services;

/**
 * Factory to return various kinds of Actors
 */
public interface IActorFactory {
    IActor getActor(String eventType);
}
